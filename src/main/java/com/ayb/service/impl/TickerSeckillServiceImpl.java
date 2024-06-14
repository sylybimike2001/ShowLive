package com.ayb.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ayb.entity.AudienceInfo;
import com.ayb.entity.DTO.Result;
import com.ayb.entity.DTO.TicketOrderDTO;
import com.ayb.entity.DTO.UserDTO;
import com.ayb.entity.TicketOrder;
import com.ayb.mapper.TicketOrderMapper;
import com.ayb.service.AudienceInfoService;
import com.ayb.service.TicketSeckillService;
import com.ayb.service.TicketService;
import com.ayb.uitls.RedisIdWorker;
import com.ayb.uitls.RegexUtils;
import com.ayb.uitls.UserHolder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class TickerSeckillServiceImpl extends ServiceImpl<TicketOrderMapper, TicketOrder> implements TicketSeckillService {

    private TickerSeckillServiceImpl proxy;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisIdWorker redisIdWorker;

    @Autowired
    AudienceInfoService audienceInfoService;

    @Autowired
    TicketService ticketService;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setResultType(Long.class);
        SECKILL_SCRIPT.setLocation(new ClassPathResource("ticketkill.lua"));
    }

    private BlockingQueue<TicketOrder> orderTasks = new ArrayBlockingQueue<>(1024*1024);
    static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();
    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    private void init(){
        SECKILL_ORDER_EXECUTOR.submit(new TicketOrderTask());
    }
    private class TicketOrderTask implements Runnable {
        @Override
        public void run() {
            while (true){
                try {
                    System.out.println("工作线程启动");
                    TicketOrder ticketOrder = orderTasks.take();
                    ticketOrderHandler(ticketOrder);
                } catch (InterruptedException e) {
//                    log.error("处理订单异常",e);
                }
            }
        }
    }


    public void ticketOrderHandler(TicketOrder ticketOrder) {
        RLock lock = redissonClient.getLock("lock:" + ticketOrder.getUserID());

        boolean isLock = lock.tryLock();
        if (!isLock){
            log.error("同一时间只允许一名用户下单");
            return;
        }
        try {
            proxy.createTicketOrder(ticketOrder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }


    /***
     * 创建事务，查询订单是否存在，如果不存在：1.创建订单并且保存到数据库 2.扣减数据库库存
     * @param ticketOrder
     */
    @Transactional
    public void createTicketOrder(TicketOrder ticketOrder){
        Long orderId = ticketOrder.getOrderId();
        Integer amount = ticketOrder.getAmount();
        List<AudienceInfo> audienceInfo = ticketOrder.getAudienceInfo();
        List<String> ids = new ArrayList<>();
        for (AudienceInfo info : audienceInfo) {
            ids.add(info.getIdCard());
        }
        Integer count = audienceInfoService.query().eq("order_id", orderId).in("id_card", ids).count();
        if(count > 0){
            log.error("订单中的用户已经购买过该场次的票");
            return;
        }
        //扣库存
        boolean update = ticketService.update().setSql("stock = stock - " + amount)
                .eq("show_id", ticketOrder.getShowId())
                .eq("level", ticketOrder.getLevel())
                .gt("stock",0).update();
        if (!update){
            return;
        }
        //创建订单，保存订单，返回订单id
        audienceInfoService.saveBatch(audienceInfo);
        save(ticketOrder);
    }

    @Override
    public Result seckill(TicketOrderDTO ticketOrderDTO) {
        UserDTO user = UserHolder.getUser();
        String showID = String.valueOf(ticketOrderDTO.getShowId());

        //查询订单中用户是否为空
        List<AudienceInfo> audienceInfo = ticketOrderDTO.getAudienceInfo();
        if (audienceInfo.isEmpty()){
            return Result.fail("请选择购票人！");
        }

        //整理前端信息中携带的用户身份证信息
        List<String> ids = new ArrayList<>(ticketOrderDTO.getAmount());
        for (AudienceInfo info : audienceInfo) {
            String idCard = info.getIdCard();
            if(RegexUtils.isIdCardInvalid(idCard)){
                return Result.fail("用户:"+info.getName()+" 的身份证格式不正确");

            }
            ids.add(idCard);
        }
        //1.执行lua脚本，内部会在缓存中减少票数量，增加票购买用户id
        String level = ticketOrderDTO.getLevel().toString();
        String amount = String.valueOf(ids.size());
        ticketOrderDTO.setAmount(ids.size());
        Long result = stringRedisTemplate.execute(SECKILL_SCRIPT, Collections.emptyList(),
                showID,level,amount,String.join(",", ids));
        if (result == null) {
            return Result.fail("未知异常");
        }
        int res = result.intValue();
        if (res == 1){
            return Result.fail("库存不足");
        }
        if (res == 2){
            return Result.fail("订单中有一名用户已经够买过一张票了，请重新确认用户");
        }
        //2.到这步 证明没买过票且库存充足，创建订单并放入消息队列中
        TicketOrder ticketOrder = BeanUtil.copyProperties(ticketOrderDTO, TicketOrder.class);
        ticketOrder.setPayValue(0L);
        ticketOrder.setStatus(0);
        long ID = redisIdWorker.nextId("ticket:order:");
        ticketOrder.setOrderId(ID);
        //多线程任务无法通过UserHolder拿到用户，因此要提前存入userID
        ticketOrder.setUserID(user.getId());
        for (AudienceInfo info : audienceInfo) {
            info.setOrderId(ID);
        }
        ticketOrder.setAudienceInfo(audienceInfo);
        orderTasks.add(ticketOrder);
        proxy = (TickerSeckillServiceImpl) AopContext.currentProxy();
        return Result.ok();
    }
}

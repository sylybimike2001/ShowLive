package com.ayb.service.impl;

import com.ayb.entity.DTO.Result;
import com.ayb.entity.DTO.TicketOrderDTO;
import com.ayb.service.TicketSeckillService;
import com.ayb.uitls.UserHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

@Service
public class TickerSeckillServiceImpl implements TicketSeckillService {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setResultType(Long.class);
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
    }
    @Override
    public Result seckill(TicketOrderDTO ticketOrder) {
        System.out.println(ticketOrder.toString());
        String ticketId = String.valueOf(ticketOrder.getShowId());
        //1.执行lua脚本
        Long result = stringRedisTemplate.execute(SECKILL_SCRIPT, Collections.emptyList(),
                ticketId, UserHolder.getUser().getId().toString());
        if (result == null) {
            return Result.fail("未知异常");
        }
        int res = result.intValue();
        if (res == 1){
            return Result.fail("库存不足");
        }
        if (res == 2){
            return Result.fail("已经够买过一张票了");
        }
        //2.到这步 证明没买过票且库存充足，创建订单并放入消息队列中

        return null;
    }
}

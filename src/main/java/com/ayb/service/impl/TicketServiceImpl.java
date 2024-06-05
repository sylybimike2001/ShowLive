package com.ayb.service.impl;

import cn.hutool.json.JSONUtil;
import com.ayb.entity.DTO.Result;
import com.ayb.entity.Show;
import com.ayb.entity.Ticket;
import com.ayb.mapper.ShowMapper;
import com.ayb.mapper.TicketMapper;
import com.ayb.service.ShowService;
import com.ayb.service.TicketService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result addTicket(Ticket ticket) {
        //在数据库中查找是不是已经存在该种种类的票，如果存在，原始库存基础上加
        Ticket one = query().eq("show_id", ticket.getShowId()).eq("level", ticket.getLevel()).one();
        Long stock = 0L;
        if (one != null) {
            //更新库存
            UpdateWrapper<Ticket> updateWrapper = new UpdateWrapper<>();
            stock = ticket.getStock() + one.getStock();
            updateWrapper.eq("show_id", ticket.getShowId()).eq("level", ticket.getLevel()).set("stock", ticket.getStock() + one.getStock());
            update(null, updateWrapper);
        }else{
            //保存到数据库
            boolean saved = save(ticket);
            if (!saved) {
                return Result.fail("保存到数据库失败");
            }
            one = ticket;
            stock = ticket.getStock();
        }
        stringRedisTemplate.opsForValue().set("ticket:stock:" + one.getId(), String.valueOf(stock));
        return Result.ok();
    }
}

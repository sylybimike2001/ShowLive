package com.ayb.mapper;

import com.ayb.entity.Show;
import com.ayb.entity.Ticket;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TicketMapper extends BaseMapper<Ticket> {
}

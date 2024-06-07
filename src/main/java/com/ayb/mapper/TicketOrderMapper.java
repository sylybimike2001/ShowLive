package com.ayb.mapper;

import com.ayb.entity.Ticket;
import com.ayb.entity.TicketOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TicketOrderMapper extends BaseMapper<TicketOrder> {
}

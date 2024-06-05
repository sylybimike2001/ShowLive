package com.ayb.service;

import com.ayb.entity.DTO.Result;
import com.ayb.entity.DTO.TicketOrderDTO;
import org.springframework.stereotype.Service;

@Service
public interface TicketSeckillService {
    Result seckill(TicketOrderDTO ticketId);
}

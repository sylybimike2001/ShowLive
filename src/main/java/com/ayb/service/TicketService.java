package com.ayb.service;

import com.ayb.entity.DTO.Result;
import com.ayb.entity.Show;
import com.ayb.entity.Ticket;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface TicketService extends IService<Ticket> {
    Result addTicket(Ticket ticket);
}

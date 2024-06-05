package com.ayb.controller;

import com.ayb.entity.DTO.Result;
import com.ayb.entity.DTO.TicketOrderDTO;
import com.ayb.service.TicketSeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketSeckillController {
    @Autowired
    private TicketSeckillService ticketSeckillService;

    @PostMapping("/seckill")
    public Result SeckillTicket(@RequestBody TicketOrderDTO ticketOrderDTO) {
        return ticketSeckillService.seckill(ticketOrderDTO);
    }


}

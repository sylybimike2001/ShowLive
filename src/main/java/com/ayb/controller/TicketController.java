package com.ayb.controller;

import com.ayb.entity.DTO.Result;
import com.ayb.entity.Show;
import com.ayb.entity.Ticket;
import com.ayb.service.ShowService;
import com.ayb.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/add-ticket")
    public Result addTicket(@RequestBody Ticket ticket){
        return ticketService.addTicket(ticket);
    }
}

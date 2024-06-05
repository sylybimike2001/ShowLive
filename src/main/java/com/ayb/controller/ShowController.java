package com.ayb.controller;

import com.ayb.entity.DTO.Result;
import com.ayb.entity.Show;
import com.ayb.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/show")
public class ShowController {
    @Autowired
    private ShowService showService;

    @PostMapping("/add-show")
    public Result addShow(@RequestBody Show show){
        return showService.addShow(show);
    }
}

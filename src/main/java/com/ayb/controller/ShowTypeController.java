package com.ayb.controller;

import com.ayb.entity.DTO.Result;
import com.ayb.service.ShowTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/show-type")
public class ShowTypeController {

    @Resource
    private ShowTypeService showTypeService;

    @GetMapping("/list")
    public Result queryShowType(){
        return showTypeService.queryShowType();
    }
}

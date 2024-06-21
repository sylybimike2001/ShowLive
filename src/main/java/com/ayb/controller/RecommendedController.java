package com.ayb.controller;

import com.ayb.entity.DTO.Result;
import com.ayb.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommend")
public class RecommendedController {

    @Autowired
    RecommendService recommendService;

    @GetMapping("/of/type")
    public Result recommend(
                            @RequestParam(value = "typeId",defaultValue = "1") Integer typeId,
                            @RequestParam(value = "quantity", defaultValue = "9") Integer quantity,
                            @RequestParam(value = "x",required = false) Double x,
                            @RequestParam(value = "y",required = false) Double y
    ){
        return recommendService.autoFillRecommend(typeId,quantity,x,y);
    }
}

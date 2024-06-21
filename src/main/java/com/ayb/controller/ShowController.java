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

    /**
     * 
     * @param type 查询类型
     * @param page 页码
     * @param size 每页显示的条目量
     * @param sort 按照哪个字段排序
     * @param order 升序或降序
     * @return 查询结果
     */
    @GetMapping("/list")
    public Result queryShowsListByType(
            @RequestParam(value = "type", defaultValue = "1") Integer type,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order) {
        return showService.queryShowsListByType(type,page,size,sort,order);
    }
}

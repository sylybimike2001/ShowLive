package com.ayb.service;

import com.ayb.entity.DTO.Result;
import com.ayb.entity.Show;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface ShowService extends IService<Show> {
    Result addShow(Show show);

    Result queryShowsListByType(Integer type, Integer page, Integer size, String sort, String order);
}

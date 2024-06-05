package com.ayb.service;

import com.ayb.entity.DTO.Result;
import com.ayb.entity.ShowType;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface ShowTypeService extends IService<ShowType> {
    Result queryShowType();
}

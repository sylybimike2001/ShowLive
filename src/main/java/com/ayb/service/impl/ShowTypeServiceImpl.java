package com.ayb.service.impl;

import cn.hutool.json.JSONUtil;
import com.ayb.entity.DTO.Result;
import com.ayb.entity.ShowType;
import com.ayb.mapper.ShowTypeMapper;
import com.ayb.service.ShowTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ayb.uitls.UserConstants.SHOW_TYPE;

@Service
public class ShowTypeServiceImpl extends ServiceImpl<ShowTypeMapper, ShowType> implements ShowTypeService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryShowType() {
        String showType = stringRedisTemplate.opsForValue().get(SHOW_TYPE);
        if (showType != null) {
            List<ShowType> showTypes = JSONUtil.toList(showType, ShowType.class);
            return Result.ok(showTypes);
        }
        List<ShowType> showsList = query().list();
        if (showsList == null) {
            return Result.fail("数据不存在");
        }
        //存入redis
        String showsListJson = JSONUtil.toJsonStr(showsList);
        stringRedisTemplate.opsForValue().set(SHOW_TYPE,showsListJson,24L, TimeUnit.HOURS);
        return Result.ok(showsListJson);
    }
}

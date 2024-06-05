package com.ayb.service.impl;

import cn.hutool.json.JSONUtil;
import com.ayb.entity.DTO.Result;
import com.ayb.entity.Show;
import com.ayb.entity.ShowType;
import com.ayb.mapper.ShowMapper;
import com.ayb.mapper.ShowTypeMapper;
import com.ayb.service.ShowService;
import com.ayb.service.ShowTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ayb.uitls.UserConstants.SHOW_TYPE;

@Service
public class ShowServiceImpl extends ServiceImpl<ShowMapper, Show> implements ShowService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result addShow(Show show) {
        //保存到数据库
        boolean saved = save(show);
        if (!saved) {
            return Result.fail("保存到数据库失败");
        }
        //保存到redis
        stringRedisTemplate.opsForValue().set("show:"+show.getShowId(), JSONUtil.toJsonStr(show));
        return Result.ok();
    }
}
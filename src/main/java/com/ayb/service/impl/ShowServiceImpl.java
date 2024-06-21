package com.ayb.service.impl;

import cn.hutool.json.JSONUtil;
import com.ayb.entity.DTO.Result;
import com.ayb.entity.Show;
import com.ayb.mapper.ShowMapper;
import com.ayb.service.ShowService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShowServiceImpl extends ServiceImpl<ShowMapper, Show> implements ShowService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ShowMapper showMapper;

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

    @Override
    public Result queryShowsListByType(Integer type, Integer page, Integer size, String sort, String order) {
        Page<Show> showPage = new Page<>(page,size);
        QueryWrapper<Show> queryWrapper = new QueryWrapper<>();
        if (!type.equals(10)) {
            if (order.equals("asc")) {
                queryWrapper.eq("type", type).orderByAsc(sort);
            }else {
                queryWrapper.eq("type", type).orderByDesc(sort);
            }
        }
        else{
            if (order.equals("asc")) {
                queryWrapper.orderByAsc(sort);
            }else {
                queryWrapper.orderByDesc(sort);
            }
        }
        Page<Show> res = showMapper.selectPage(showPage, queryWrapper);
        return Result.ok(res);
    }

}

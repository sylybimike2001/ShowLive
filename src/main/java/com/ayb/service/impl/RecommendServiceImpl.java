package com.ayb.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.ayb.entity.DTO.Result;
import com.ayb.entity.Show;
import com.ayb.service.RecommendService;
import com.ayb.service.ShowService;
import com.ayb.uitls.SysConstants;
import com.ayb.uitls.UserConstants;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.ayb.uitls.SysConstants.AUTOFILL_DISTANCE;
import static com.ayb.uitls.SysConstants.NEARBY_DISTANCE;


@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    ShowServiceImpl showService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result pageSelectionByType(Integer typeId, Integer current, Double x, Double y) {
        //判断是否根据地理位置查询
        if (x == null|| y == null){
            Page<Show> page = showService.query()
                    .eq("type_id", typeId)
                    .page(new Page<>(current, SysConstants.DEFAULT_PAGE_SIZE));
            return Result.ok(page.getRecords());
        }
        //计算分页参数
        int from = (current - 1) * SysConstants.DEFAULT_PAGE_SIZE;
        int end = current * SysConstants.DEFAULT_PAGE_SIZE;
        List<Show> shows = radiusSearch(typeId, x, y, from, end,NEARBY_DISTANCE,null);
        return Result.ok(shows);
    }

    /**
     *
     * @param typeId 类型
     * @param quantity 查询的数量
     * @param x 精度
     * @param y 维度
     * @return 返回查询到500km内的演出，长度可能不足quantity
     */
    @Override
    public List<Show> recommendByType(Integer typeId, Integer quantity, Double x, Double y) {
        //判断是否根据地理位置查询
        if (x == null|| y == null){
            List<Show> shows = showService.query().eq("type", typeId).list();
            return ListUtil.sub(shows,0,quantity);
        }
        //计算分页参数
        int from = 0;
        int end = quantity;
        List<Show> shows = radiusSearch(typeId, x, y, from, end,AUTOFILL_DISTANCE,Metrics.KILOMETERS);
        return ListUtil.sub(shows,0,quantity);
    }

    @Override
    public Result autoFillRecommend(Integer typeId,Integer quantity, Double x, Double y) {
        List<Show> shows = recommendByType(typeId, quantity, x, y);
        return Result.ok(shows);
    }

    private List<Show> radiusSearch(Integer typeId, Double x, Double y,int from,int end,Long radius,Metrics metrics){
        //查询redis，获取商户id，地理位置,默认先查找500km范围内的推荐演出
        String key = UserConstants.SHOW_GEO_KEY + typeId;
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = stringRedisTemplate.opsForGeo().radius(key,
                new Circle(new Point(x,y),new Distance(radius,metrics)), RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(end));
        if (results == null){
            return Collections.emptyList();
        }
        //切片，把ID和距离分别放到两个序列中，序列长度代表查询本页的演出数量
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = results.getContent();
        List<Long> showIDs = new ArrayList<>(list.size());
        Map<String,Distance> distanceMap = new HashMap<>(list.size());
        list.stream().skip(from).forEach(result->{
            String showID = result.getContent().getName();
            showIDs.add(Long.valueOf(showID));
            Distance distance = result.getDistance();
            distanceMap.put(showID, distance);
        });

        //到数据库中把这些演出的具体信息查出来
        if (showIDs.isEmpty()){
            return Collections.emptyList();
        }
        String joinIds = StrUtil.join(",",showIDs);
        List<Show> shows = showService.query().in("show_id", showIDs).last("ORDER BY FIELD(show_id," + joinIds + ")").list();
        for (Show show : shows) {
            show.setDistance(distanceMap.get(show.getShowId().toString()).getValue());
        }
        return shows;
    }
}

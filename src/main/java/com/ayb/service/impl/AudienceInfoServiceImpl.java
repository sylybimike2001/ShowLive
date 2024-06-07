package com.ayb.service.impl;

import cn.hutool.json.JSONUtil;
import com.ayb.entity.AudienceInfo;
import com.ayb.entity.DTO.Result;
import com.ayb.entity.Show;
import com.ayb.mapper.AudienceInfoMapper;
import com.ayb.mapper.ShowMapper;
import com.ayb.service.AudienceInfoService;
import com.ayb.service.ShowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AudienceInfoServiceImpl extends ServiceImpl<AudienceInfoMapper, AudienceInfo> implements AudienceInfoService {

}

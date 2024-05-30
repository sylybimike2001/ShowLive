package com.ayb.mapper;

import com.ayb.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

package com.ayb.config;

import com.ayb.interceptor.LoginInterceptor;
import com.ayb.interceptor.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //刷新拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);
        //登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/code",
                        "/user/login",
                        "/show-type/list",
                        "/user/modify",
//                        "/ticket/seckill",
                        "/show/add-show",
                        "/ticket/add-ticket",
                        "/recommend/of/type",
                        "/user/register/phone/code",
                        "/user/register/mail/code"
                ).order(1);
    }
}


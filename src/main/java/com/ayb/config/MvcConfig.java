package com.ayb.config;

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
//        registry.addInterceptor(new LoginInterceptor())
//                .excludePathPatterns(
//                        "/user/code",
//                        "/user/login",
//                        "/blog/hot",
//                        "/shop/**",
//                        "/shop-type/**",
//                        "/voucher/**"
//                ).order(1);
    }
}

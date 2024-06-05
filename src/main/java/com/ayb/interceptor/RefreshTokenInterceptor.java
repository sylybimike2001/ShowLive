package com.ayb.interceptor;

import cn.hutool.json.JSONUtil;
import com.ayb.entity.DTO.UserDTO;
import com.ayb.uitls.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.concurrent.TimeUnit;

import static com.ayb.uitls.UserConstants.LOGIN_TOKEN_PREFIX;
import static com.ayb.uitls.UserConstants.LOGIN_TTL;

public class RefreshTokenInterceptor implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("authorization");
        //如果校验token为空，什么都不做，放行
        if (token == null){
            return true;
        }

        //到redis种查询token是否存在
        String key = LOGIN_TOKEN_PREFIX + token;
        String userJson = stringRedisTemplate.opsForValue().get(key);
        //如果redis中不存在token，什么都不做，放行
        if (userJson == null){
            return true;
        }
        //把查询到的用户信息放到线程池中
        UserDTO userDTO = JSONUtil.toBean(userJson, UserDTO.class);
        UserHolder.setUser(userDTO);
        //刷新token
        stringRedisTemplate.expire(key,LOGIN_TTL, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}

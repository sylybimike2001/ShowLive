package com.ayb.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.ayb.entity.LoginInfo;
import com.ayb.entity.Result;
import com.ayb.entity.User;
import com.ayb.entity.UserDTO;
import com.ayb.mapper.UserMapper;
import com.ayb.service.UserService;
import com.ayb.uitls.RegexUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static com.ayb.uitls.UserConstants.USER_CODE_PREFIX;
import static com.ayb.uitls.UserConstants.*;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result sendCode(String phone) {
        //1.校验手机号是否合法
        boolean phoneInvalid = RegexUtils.isPhoneInvalid(phone);
        //1.1不合法 返回错误信息
        if (phoneInvalid){
            return Result.fail("手机号不合法");
        }
        //1.2合法 生成code
        String code = RandomUtil.randomNumbers(6);
        //1.3将验证码储存到redis用于后续查验
        String key = USER_CODE_PREFIX+phone;
        stringRedisTemplate.opsForValue().set(key,code);
        //1.4返回code
        return Result.ok(code);
    }

    @Override
    public Result login(LoginInfo loginInfo) {
        //1.校验手机号是否合理
        boolean phoneInvalid = RegexUtils.isPhoneInvalid(loginInfo.getPhone());
        String phone = loginInfo.getPhone();
        //1.1如果不合理，返回错误信息
        if (phoneInvalid){
            return Result.fail("手机号不合法");
        }
        //1.2如果合理
        //2.查验验证码是否匹配
        String code = stringRedisTemplate.opsForValue().get(USER_CODE_PREFIX+phone);
        //2.1 如果不匹配，返回错误信息
        if (code == null || !code.equals(loginInfo.getCode())){
            return Result.fail("验证码不匹配");
        }
        //2.2 如果匹配，到数据库查用户存不存在，如果不存在创建用户并保存
        User user = query().eq("phone", phone).one();
        if (user == null){
            //2.2.1创建用户
            user = createUserWithPhone(phone);
            //2.2.2用户保存到数据库中
            save(user);
        }
        //3 将用户转化为信息安全的前端格式
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        String userJson = JSONUtil.toJsonStr(userDTO);
        //4 将用户信息保存到redis中
        String token = UUID.randomUUID().toString(true);
        String key = LOGIN_TOKEN_PREFIX + token;
        stringRedisTemplate.opsForValue().set(key,userJson);
        stringRedisTemplate.expire(key,10L, TimeUnit.MINUTES);
        return Result.ok(userDTO);
    }

    private User createUserWithPhone(String phone) {
        User user = new User();
        user.setPhone(phone);

        String password = RandomStringUtils.randomAlphanumeric(10);
        user.setPassword(password);

        String nickName = RandomStringUtils.randomAlphanumeric(5);
        user.setNickName("ShowFans_"+nickName);

        return user;
    }
}

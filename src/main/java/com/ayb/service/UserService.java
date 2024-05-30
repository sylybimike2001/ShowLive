package com.ayb.service;

import com.ayb.entity.LoginInfo;
import com.ayb.entity.Result;
import com.ayb.entity.User;
import com.ayb.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends IService<User> {

    public Result sendCode(String phone);

    Result login(LoginInfo loginInfo);
}

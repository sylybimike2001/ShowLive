package com.ayb.service;

import com.ayb.entity.toolclass.LoginInfo;
import com.ayb.entity.DTO.Result;
import com.ayb.entity.User;
import com.ayb.entity.toolclass.UserMod;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends IService<User> {

    public Result sendPhoneCode(String phone);

    public Result sendMailCode(String mail);

    Result login(LoginInfo loginInfo);

    Result modify(UserMod user);
}

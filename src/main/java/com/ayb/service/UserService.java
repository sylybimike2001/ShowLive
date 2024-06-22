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

    /**
     *
     * @param loginInfo 前端传过来的登录信息
     * @param mode mode=1->电话登录;mode=2->邮箱登录
     * @return 验证码
     */
    Result login(LoginInfo loginInfo,int mode);

    Result modify(UserMod user);
}

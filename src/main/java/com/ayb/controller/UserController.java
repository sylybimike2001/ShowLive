package com.ayb.controller;

import com.ayb.entity.toolclass.LoginInfo;
import com.ayb.entity.DTO.Result;
import com.ayb.entity.toolclass.UserMod;
import com.ayb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Result helloWorld(){
        return Result.ok("hello world");
    }

    @PostMapping("/register/phone/code")
    public Result sendPhoneCode(@RequestParam("phone") String phone){
        return userService.sendPhoneCode(phone);
    }

    @PostMapping("/register/mail/code")
    public Result sendMailCode(@RequestParam("mail") String mail){
        return userService.sendMailCode(mail);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginInfo loginInfo){
        return userService.login(loginInfo,2);
    }

    @PostMapping("/modify")
    public Result modify(@RequestBody UserMod userMod){
        return userService.modify(userMod);
    }

}

package com.ayb.controller;

import com.ayb.entity.LoginInfo;
import com.ayb.entity.Result;
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

    @PostMapping("/code")
    public Result code(@RequestParam("phone") String phone){
        return userService.sendCode(phone);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginInfo loginInfo){
        return userService.login(loginInfo);
    }

}

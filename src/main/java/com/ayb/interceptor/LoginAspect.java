package com.ayb.interceptor;

import com.ayb.entity.DTO.Result;
import com.ayb.entity.toolclass.LoginInfo;
import com.ayb.uitls.RegexUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginAspect {
    @Pointcut("execution(* com.ayb.service.impl.UserServiceImpl.login(..))")
    public void modeSelection() {}

    @Around("modeSelection()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Match success,Selecting Mode.....");
        //获取参数并且增强参数
        Object[] args = joinPoint.getArgs();
        LoginInfo loginInfo = (LoginInfo) args[0];
        String phoneOrMail = loginInfo.getPhoneOrMail();
        if (RegexUtils.isPhoneOrMailInvalid(phoneOrMail)){
            return Result.fail("邮箱或手机号格式不正确");
        }
        if(!RegexUtils.isPhoneInvalid(phoneOrMail))  {
            args[1] = 1;
        }
        else if (!RegexUtils.isMailInvalid(phoneOrMail))  {
            args[1] = 2;
        }
        return joinPoint.proceed(args);
    }
}

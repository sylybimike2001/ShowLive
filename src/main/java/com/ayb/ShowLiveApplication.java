package com.ayb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class ShowLiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShowLiveApplication.class, args);
    }

}

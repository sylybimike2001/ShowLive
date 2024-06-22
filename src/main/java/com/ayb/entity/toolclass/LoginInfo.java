package com.ayb.entity.toolclass;

import lombok.Data;

@Data
public class LoginInfo {
    private String phoneOrMail;
    private String password;
    private String code;
}

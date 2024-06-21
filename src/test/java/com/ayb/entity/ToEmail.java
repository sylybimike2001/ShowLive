package com.ayb.entity;

import lombok.Data;

@Data
public class ToEmail {
    private String toEmail;
    private String subject;
    private String content;
}

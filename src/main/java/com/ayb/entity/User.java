package com.ayb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@TableName("tb_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String phone;
    private String password;
    private String nickName;
    private String icon = "";
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

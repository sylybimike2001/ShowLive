package com.ayb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@TableName("tb_show")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class Show {
    @TableId(value = "show_id", type = IdType.AUTO)
    private Integer showId;

    @TableField("name")
    private String name;

    @TableField("icon")
    private String icon;

    @TableField("star")
    private String star;

    @TableField("show_time")
    private LocalDateTime showTime;

    @TableField("ticket_time")
    private LocalDateTime ticketTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("ticket_levels")
    private Integer ticketLevels;
}

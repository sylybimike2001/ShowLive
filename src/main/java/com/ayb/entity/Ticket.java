package com.ayb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("tb_ticket_stock")
public class Ticket {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("show_id")
    private Long showId;

    @TableField("stock")
    private Long stock;

    @TableField("level")
    private Integer level;

    @TableField("single_price")
    private Long singlePrice;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}

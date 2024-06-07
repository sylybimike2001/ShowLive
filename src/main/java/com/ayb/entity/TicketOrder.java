package com.ayb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("tb_ticket_order")
public class TicketOrder {
    @TableField("order_id")
    private Long orderId;

    @TableField("show_id")
    private Long showId;

    @TableField("pay_value")
    private Long payValue;

    @TableField("level")
    private Integer level;

    @TableField("amount")
    private Integer amount;

    @TableField("status")
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    @TableField(exist = false)
    private List<AudienceInfo> audienceInfo;

    /**
     * 生效时间
     */
    @TableField(exist = false)
    private LocalDateTime beginTime;

    /**
     * 失效时间
     */
    @TableField(exist = false)
    private LocalDateTime endTime;
}

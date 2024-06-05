package com.ayb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_audience_info")
public class AudienceInfo {
    @TableField("order_id")
    private Long orderId;

    private String name;

    @TableField("id_card")
    private String idCard;

}

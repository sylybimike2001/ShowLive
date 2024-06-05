package com.ayb.entity.DTO;

import com.ayb.entity.AudienceInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TicketOrderDTO {
    private Long showId;

    private Long singlePrice;

    private Integer amount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    @TableField(exist = false)
    private List<AudienceInfo> audienceInfo;
}

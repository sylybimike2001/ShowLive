package com.ayb.entity.toolclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class UserMod {
    private Long id;
    private String phone;
    private String oldPassword;
    private String newPassword;
    private String nickname;
}

package com.ayb.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Boolean isSuccess;
    private String errorMsg;
    private Integer code;
    private Object data;

    public static Result ok() {
        return new Result(true,null,null,null);
    }
    public static Result ok(Object data) {
        return new Result(true,null,null,data);
    }
    public static Result fail(String msg) {
        return new Result(false,msg,null,null);
    }
}

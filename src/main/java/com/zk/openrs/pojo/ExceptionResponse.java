package com.zk.openrs.pojo;

import lombok.Data;

@Data
public class ExceptionResponse {
    int code;
    String errMsg;

    public ExceptionResponse(int code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }
}

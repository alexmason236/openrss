package com.zk.openrs.pojo;

import java.io.Serializable;

public class SimpleResponse implements Serializable {
    String msg;
    Object object;

    public SimpleResponse(String msg) {
        this.msg = msg;
    }

    public SimpleResponse(String msg, Object object) {
        this.msg = msg;
        this.object = object;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}

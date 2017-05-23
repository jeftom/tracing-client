package com.bdfint.bdtrace.bean;

/**
 * @author heyb
 * @date 2017/5/15.
 * @desriptioin
 */
public enum StatusEnum {
    OK(200, "OK"),
    ERROR(500, "ERROR");

    private int code;
    private String desc;

    StatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

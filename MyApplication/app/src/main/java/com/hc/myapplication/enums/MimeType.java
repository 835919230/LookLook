package com.hc.myapplication.enums;

/**
 * Created by 何肸 on 2016/7/1.
 * Mime类型的枚举类
 */
public enum MimeType {
    CSS("text/css"),
    JAVASCRIPT("text/javascript"),
    HTML("text/html"),
    ICO("image/x-icon"),
    JSON("application/json"),
    SWF("application/octet-stream");
    private String type;
    MimeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

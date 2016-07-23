package com.hc.myapplication.server.enums;

/**
 * Created by 何肸 on 2016/7/1.
 * Mime类型的枚举类
 */
public enum MimeType {
    CSS("css","text/css"),
    JAVASCRIPT("js","text/javascript"),
    HTML("html","text/html"),
    ICO("ico","image/x-icon"),
    JSON("json","application/json"),
    SWF("swf","application/x-shockwave-flash"),
    GIF("gif","image/gif"),
    JPG("jpg","image/jpeg"),
    JPEG("jpeg","image/jpeg");
    private String type;
    private String key;
    MimeType(String key,String type) {
        this.key = key;
        this.type = type;
    }

    public static String get(String key){
        for (MimeType k:values()){
            if (k.getKey().equals(key))
                return k.getType();
        }
        return "";
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

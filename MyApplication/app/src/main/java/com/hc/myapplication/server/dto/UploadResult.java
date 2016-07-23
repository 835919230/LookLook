package com.hc.myapplication.server.dto;

/**
 * Created by 诚 on 2016/7/3.
 */
public class UploadResult {
    private boolean isSucceed;

    private long elapsedTime;

    private String message;

    public long getElaspedTime() {
        return elapsedTime;
    }

    public void setElaspedTime(long elaspedTime) {
        this.elapsedTime = elaspedTime;
    }

    public void setSucceed(boolean succeed) {
        isSucceed = succeed;
    }

    public boolean getSucceed(){
        return this.isSucceed;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public UploadResult(){}

    public UploadResult(boolean isSucceed, long elaspedTime, String message) {
        this.isSucceed = isSucceed;
        this.elapsedTime = elaspedTime;
        this.message = message;
    }
}

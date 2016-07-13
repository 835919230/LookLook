package com.hc.myapplication.dto;

/**
 * Created by 诚 on 2016/7/7.
 */
public class RenameResult {
    private boolean isSucceed;
    private String message;

    public boolean isSucceed() {
        return isSucceed;
    }

    public void setSucceed(boolean succeed) {
        isSucceed = succeed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RenameResult(boolean isSucceed,String message){
        this.isSucceed = isSucceed;
        this.message = message;
    }
}

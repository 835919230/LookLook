package com.hc.myapplication.ui.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by 诚 on 2016/7/20.
 */
public class PhotoItem {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mPath;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getPath() {
        return mPath;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public PhotoItem(){
        mId = UUID.randomUUID();
    }
}

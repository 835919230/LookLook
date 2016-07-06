package com.hc.myapplication.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 诚 on 2016/7/2.
 */
public class FileModel {
    private String name;
    private String lastModified;
    private String size;
    private int type;//0代表文件夹，1代表文件
    private String path;

    public FileModel(){}

    public FileModel(String name, long last,String size,int type,String path){
        this.lastModified = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(last));
        this.name = name;
        this.size = size;
        this.type = type;
        this.path = path;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public int getType() {
        return type;
    }

    public String getPath() {
        return path;
    }
}

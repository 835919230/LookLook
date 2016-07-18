package com.hc.myapplication;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/**
 * Created by caizejian on 16/7/16.
 */
public class Image {
    private Uri imageUri;

    private Bitmap bitmap;

    public Image(Uri imageUri , Bitmap bitmap){

        this.imageUri = imageUri;

        this.bitmap = bitmap;
    }

    public Uri getImageUri(){

        return imageUri;
    }

    public Bitmap getBitmap(){

        return bitmap;
    }

    protected void deleteImage() {
        File file = new File(this.getImageUri().getPath());
        file.delete();
    }
}

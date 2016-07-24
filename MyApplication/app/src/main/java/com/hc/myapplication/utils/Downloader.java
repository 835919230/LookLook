package com.hc.myapplication.utils;


import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by 诚 on 2016/7/23.
 */
public class Downloader{
    private static final String TAG = "Downloader";

    public static void downFile(final String urlSpec) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlSpec);
                    Log.i(TAG, "run: urlSpec"+urlSpec);
                    int i = urlSpec.lastIndexOf(".");
                    String type = urlSpec.substring(i);
                    if (!(type.trim().equals(".jpg")
                            || type.trim().equals(".png")
                            || type.trim().equals(".jpeg"))) {
                        return;
                    }
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream in = connection.getInputStream();
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                        throw new IOException(connection.getResponseMessage()+" with :"+urlSpec);

                    int byteRead = 0;
                    byte[] buffer = new byte[10240];
                    while ((byteRead = in.read(buffer)) != -1) {
                        out.write(buffer,0,byteRead);
                    }
                    byte[] bytes = out.toByteArray();
                    File file = new File(FileManager.getDownloadFile(),new Date().getTime()+type);
                    FileUtils.writeByteArrayToFile(file,bytes);
                } catch (MalformedURLException e) {
                    Log.e(TAG, "run: ", e);
                } catch (IOException e) {
                    Log.e(TAG, "run: ", e);
                }
            }
        }).start();
    }
}

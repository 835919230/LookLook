package com.hc.myapplication.utils;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.hc.myapplication.R;
import com.hc.myapplication.server.MultipartServer;
import com.hc.myapplication.ui.model.PhotoItem;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by 诚 on 2016/7/19.
 */
public class FileManager {
    public static final String JQUERY = "jquery.js";
    public static final String BOOTSTRAP = "bootstrap.css";
    public static final String INDEX = "index.html";
    public static final String FAVICON = "favicon.ico";
    public static final String JQEURY_FORM = "jquery_form.js";
    public static final String UPLOADER = "uploader.swf";
    public static final String APP = "app.js";
    public static final String TAG = "FileManager";
    private static final String mCurrentDir = Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/youqubao";
    private static final File htmlFile = new File(mCurrentDir+"/html");
    private static final File downloadFile = new File(mCurrentDir+"/download");

    public static File getDownloadFile() {
        return downloadFile;
    }

    public static String getmCurrentDir() {
        return mCurrentDir;
    }
    public static boolean judgeFileExsits() {
        File bootstrapFile = new File(htmlFile.toString()+"/"+BOOTSTRAP);
        File indexFile = new File(htmlFile.toString()+"/"+INDEX);
        File jqueryFile = new File(htmlFile.toString()+"/"+JQUERY);
        if (bootstrapFile.exists() &&
                indexFile.exists() &&
                jqueryFile.exists())
        {
            Log.i(TAG, "judgeFileExsits: "+true);
            return true;
        }
        else {
            Log.i(TAG, "judgeFileExsits: "+false);return false;}
    }
    public static void writeFile(final Activity activity, int id, String filename, MultipartServer server){
        InputStream in = activity.getResources().openRawResource(id);
        File file = new File(htmlFile.toString()+"/"+filename);
        try {
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            String str = new String(buffer);
            FileUtils.writeStringToFile(file,str,"utf-8");
            if (id == R.raw.hello)
                server.IndexCacheString = str;
            else if (id == R.raw.jquery)
                server.JqueryCacheString = str;
            else if (id == R.raw.bootstrap)
                server.BootstrapCacheString = str;
            else if (id == R.raw.jquery_form)
                server.JqueryFormCacheString = str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.exists())
            System.out.println("yes it is");
    }

    public static void addPhotoItems(File root, List<PhotoItem> mItems){
        File[] allFileList = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                File f = new File(file,s);
                //Log.i(TAG, "accept: 扫描到的文件名："+f.getName());
                return !f.getName().equals("html");
            }
        });
        if (allFileList.length <= 0)
            return;

        for (File file : allFileList) {
            if (!file.isDirectory()) {
                PhotoItem item = new PhotoItem();
                item.setTitle(file.getName());
                item.setDate(new Date(file.lastModified()));
                item.setPath(file.getPath());
                //Log.i(TAG, "addPhotoItems: 文件Path:"+item.getPath());
                mItems.add(item);
            } else {
                addPhotoItems(file, mItems);
            }
        }
    }


    public static void initFiles(final Activity activity, final MultipartServer mServer){
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeFile(activity,R.raw.hello,FileManager.INDEX,mServer);
                FileManager.writeFile(activity,R.raw.bootstrap,FileManager.BOOTSTRAP,mServer);
                FileManager.writeFile(activity,R.raw.jquery,FileManager.JQUERY,mServer);
                FileManager.writeFile(activity,R.raw.favicon,FileManager.FAVICON,mServer);
                FileManager.writeFile(activity,R.raw.jquery_form,FileManager.JQEURY_FORM,mServer);
                FileManager.writeFile(activity,R.raw.uploader,FileManager.UPLOADER,mServer);
                FileManager.writeFile(activity,R.raw.app,FileManager.APP,mServer);
                FileManager.judgeFileExsits();
            }
        }).start();
    }
}

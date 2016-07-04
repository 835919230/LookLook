package com.hc.myapplication.utils;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.hc.myapplication.R;
import com.hc.myapplication.Server.MultipartServer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 诚 on 2016/7/2.
 */
public class FileManager {
    public static final String JQUERY = "jquery.js";
    public static final String BOOTSTRAP = "bootstrap.css";
    public static final String INDEX = "index.html";
    public static final String FAVICON = "favicon.ico";
    public static final String JQEURY_FORM = "jquery_form.js";
    public static final String UPLOADER = "uploader.swf";

    public static final String TAG = "FileManager";

    private static final String mCurrentDir = Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/youqubao";
    private static final File htmlFile = new File(mCurrentDir+"/html");

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
}

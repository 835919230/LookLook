package com.hc.myapplication.Server;

import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.hc.myapplication.dto.UploadResult;
import com.hc.myapplication.enums.MimeType;
import com.hc.myapplication.model.FileModel;
import com.hc.myapplication.utils.FileManager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by 诚 on 2016/6/4.
 */
public class MultipartServer extends NanoHTTPD {
    private final String TAG = "upload";

    private final String filenameParam = "filename";
    protected final String fileUploadPath = "/uploadfile";

    public static String JqueryCacheString;
    public static String JqueryFormCacheString;
    public static String BootstrapCacheString;
    public static String IndexCacheString;

    private String mCurrentDir = Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/youqubao";
    public File htmlFile = new File(mCurrentDir+"/html");
    public MultipartServer(){
        this(8080);
    }

    public MultipartServer(int port) {
        super(port);
        File f = new File(mCurrentDir);
        if (f.exists()&& f.isDirectory())
            Log.i(TAG,"dir exists");
        if (f.exists()&&!f.isDirectory()) {
            Log.i(TAG,"file exists but not a directory");
            f.delete();
            f.mkdir();
        }
        if (!f.exists()){f.mkdir(); Log.i(TAG,"dir make");}
        if (!htmlFile.exists())htmlFile.mkdir();
    }

    @Override
    public Response serve(IHTTPSession session) {
        return dispatcherRequest(session);
    }

    /**
     * 处理请求的方法
     * @Author HC
     * @param session
     * @return Response
     */
    private Response dispatcherRequest(IHTTPSession session){
        Map<String, String> parms = session.getParms();

        Method method = session.getMethod();
        String uri = session.getUri();

        Log.i(TAG, "dispatcherRequest: uri:"+uri);

        Map<String, String> files = new HashMap<>();

        if (fileUploadPath.equalsIgnoreCase(uri)&&(Method.POST.equals(method) || Method.PUT.equals(method))) {
            return doUploadFile(session,parms,files);
        }else if (uri.equalsIgnoreCase("/bootstrap.css")){
            if (BootstrapCacheString!=null)
                return new Response(Response.Status.OK,MimeType.CSS.getType(),BootstrapCacheString);
            return responseFile(FileManager.BOOTSTRAP);
        }else if (uri.equalsIgnoreCase("/jquery.js")) {
            if (JqueryCacheString != null)
                return new Response(Response.Status.OK,MimeType.JAVASCRIPT.getType(),JqueryCacheString);
            return responseFile(FileManager.JQUERY);
        } else if (uri.equalsIgnoreCase("/favicon.ico"))
            return responseFile("favicon.ico");
        else if (uri.equalsIgnoreCase("/ajax")&&Method.POST.equals(method)){
            return responseJsonString(session,parms);
        }else if (uri.equalsIgnoreCase("/jquery_form.js")){
            if (JqueryFormCacheString != null)
                return new Response(Response.Status.OK,MimeType.JAVASCRIPT.getType(),JqueryFormCacheString);
            return responseFile("jquery_form.js");
        }else if (uri.equals("/uploader.swf")){
            return new Response(Response.Status.OK,MimeType.SWF.getType(),FileManager.UPLOADER);
        }else if (uri.contains(".")&&uri.lastIndexOf(".")!=uri.length()-1){
            return doDownloadFile(session);
        }else {
            return indexResponse();
        }
    }

    private Response doDownloadFile(IHTTPSession session) {
        String uri = session.getUri();
        String mimeTypeForFile = getMimeTypeForFile(uri);
        Map<String,String> header = session.getHeaders();
        Response res = null;
        File file = new File(FileManager.getmCurrentDir(),uri);
        Log.i(TAG, "doDownloadFile: 请求文件路径："+file.getAbsolutePath());
        if (!file.exists())
            return new Response(Response.Status.NOT_FOUND,NanoHTTPD.MIME_PLAINTEXT,"404,该文件不存在");
        String etag = Integer.toHexString((file.getAbsolutePath() + file.lastModified() + "" + file.length()).hashCode());
        long startFrom = 0;
        long endAt = -1;
        String range = header.get("range");
        if (range != null){
            if (range.startsWith("bytes=")){
                range = range.substring("bytes=".length());
                int minus = range.indexOf('-');
                if (minus > 0){
                    startFrom = Long.parseLong(range.substring(0,minus));
                    endAt = Long.parseLong(range.substring(minus+1));
                }
            }
        }

        long fileLen = file.length();
        if (range != null &&startFrom >=0){
            if (startFrom >= fileLen){
                res = new Response(Response.Status.RANGE_NOT_SATISFIABLE,
                        NanoHTTPD.MIME_PLAINTEXT,"");
                res.addHeader("Content-Range","bytes 0-0/"+fileLen);
                res.addHeader("Etag",etag);
            } else {
              if (endAt < 0){
                  endAt = fileLen -1;
              }
                long newLen = endAt - startFrom + 1;
                if (newLen < 0){
                    newLen = 0;
                }

                final long dataLen = newLen;
                try {
                    FileInputStream fis = new FileInputStream(file){
                        @Override
                        public int available() throws IOException {
                            return (int)dataLen;
                        }
                    };

                    fis.skip(startFrom);

                    res = new Response(Response.Status.PARTIAL_CONTENT,mimeTypeForFile,fis);
                    res.addHeader("Content-Length", "" + dataLen);
                    res.addHeader("Content-Range", "bytes " + startFrom + "-" + endAt + "/" + fileLen);
                    res.addHeader("ETag", etag);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    res = new Response(Response.Status.FORBIDDEN,NanoHTTPD.MIME_PLAINTEXT,"FORBIDDEN: Reading file failed.");
                } catch (IOException e) {
                    e.printStackTrace();
                    res = new Response(Response.Status.FORBIDDEN,NanoHTTPD.MIME_PLAINTEXT,"FORBIDDEN: Reading file failed.");
                }
            }
        }else {
            if (etag.equals(header.get("if-none-match")))
                res = new Response(Response.Status.NOT_MODIFIED,mimeTypeForFile,"");
            else {
                try {
                    res = new Response(Response.Status.OK,mimeTypeForFile,new FileInputStream(file));
                    res.addHeader("Content-Length", "" + fileLen);
                    res.addHeader("ETag", etag);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    res = new Response(Response.Status.FORBIDDEN,NanoHTTPD.MIME_PLAINTEXT,"FORBIDDEN: Reading file failed.");
                }
            }
        }
        return res;
    }

    private String getMimeTypeForFile(String uri) {
        int dot = uri.lastIndexOf('.');
        String MIME_DEFAULT_BINARY = "application/octet-stream";
        String mimeTypeForFile = null;
        if (dot > 0){
            mimeTypeForFile = MimeType.get(uri.substring(dot+1).toLowerCase());
        }
        return mimeTypeForFile == null ? MIME_DEFAULT_BINARY : mimeTypeForFile;
    }

    /**
     * 主页的响应。
     * @return Response
     */
    private Response indexResponse(){
        if (IndexCacheString != null)
            return new Response(Response.Status.OK,MimeType.HTML.getType(),IndexCacheString);
        return responseFile(FileManager.INDEX);
    }

    /**
     * 通过Ajax获取文件目录的方法
     * @Author HC
     * @param params
     * @return Response
     */
    private Response responseJsonString(IHTTPSession session,Map<String,String> params){
        try {
            session.parseBody(new HashMap<String, String>());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        String filePath = params.get("filePath");
        Log.i(TAG, "responseJsonString: filePath:"+filePath);
        File file = new File(mCurrentDir+"/"+filePath);
        List<FileModel> fileModels = new ArrayList<>(20);
        Log.i(TAG, "responseJsonString: file:"+file.toString());
        List<File> directories = Arrays.asList(file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                File d = new File(dir,filename);
                return d.isDirectory() && !d.getName().equals("html");
            }
        }));

        if (directories!=null&&directories.size()>0){
            for (File directory : directories){
                long len = directory.length();
                String size = "";
                if (len < 1024)
                    size = len +"bytes";
                else if (len < 1024*1024)
                    size = len / 1024 +"."+(len % 1024 / 10 %1024)+"KB";
                else size = (len / (1024*1024))+"."+(len %(1024*1024)/10%1024)+"MB";
                fileModels.add(new FileModel(directory.getName(),directory.lastModified(),size,0,directory.getPath().substring(directory.getPath().indexOf("youqubao")+"youqubao".length())));
            }
        }

        List<File> files = Arrays.asList(file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                return new File(dir,filename).isFile();
            }
        }));

        if (files != null && files.size() > 0){
            for (File f : files){
                long len = f.length();
                String size = "";
                if (len < 1024)
                    size = len +"bytes";
                else if (len < 1024*1024)
                    size = len / 1024 +"."+(len % 1024 / 10 %1024)+"KB";
                else size = (len / (1024*1024))+"."+(len %(1024*1024)/10%1024)+"MB";
                fileModels.add(new FileModel(f.getName(),f.lastModified(),size,1,f.getPath().substring(f.getPath().indexOf("youqubao")+"youqubao".length())));
            }
        }

        String json = JSON.toJSONString(fileModels);
        return new Response(Response.Status.OK,MimeType.JSON.getType(),json);
    }

    /**
     * 默认的响应方法，调试用
     * @return Response
     */
    @Deprecated
    public Response defaultResponse(){

        StringBuffer sb = new StringBuffer();

        sb.append("<html>")
                .append("<head>")
                .append("<meta charset='utf-8'/>")
                .append("</head>")
                .append("<body>");

        sb.append("<form action='"+fileUploadPath+"' method = 'post' enctype='multipart/form-data'>")
                .append("<input type='file' name='"+filenameParam+"'>")
                .append("<input type='submit' value='提交'>")
                .append("</form>");
        sb.append("</html>");
        return new Response(sb.toString());
    }

    /**
     * 处理上传文件请求的方法
     * @Author HC
     * @param session  对应serve里面的参数 session
     * @param params   对应serve里面的参数 parms
     * @param files    对应serve里面的参数 files
     * @return Response
     */
    private Response doUploadFile(IHTTPSession session,Map<String,String> params,Map<String,String> files){
        long startTime = System.currentTimeMillis();
        try {
            session.parseBody(files);
        } catch (IOException ioe) {
            //return getResponse("Internal Error IO Exception: " + ioe.getMessage());
            return new Response("Internal Error IO Exception: " + ioe.getMessage());
        } catch (ResponseException re) {
            return new Response(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
        }
        String filename = params.get(filenameParam);
        String tmpFilePath = files.get(filenameParam);
        Log.i(TAG,"filename:"+filename);
        Log.i(TAG,"tmpFilePath:"+tmpFilePath);
        if (null == filename || null == tmpFilePath) {
            return new Response(Response.Status.OK,
                    MimeType.JSON.getType(),
                    JSON.toJSONString(new UploadResult(false,System.currentTimeMillis() - startTime,"不能空文件哦！")));
        }
        File dst = new File(mCurrentDir,filename);
        if (dst.exists()) {
            // Response for confirm to overwrite
            return new Response(Response.Status.OK,
                    MimeType.JSON.getType(),
                    JSON.toJSONString(new UploadResult(false,System.currentTimeMillis() - startTime,"是重复文件哦！")));
        }

        File src = new File(tmpFilePath);
        try {
            //commons-io下的FileUtils，比较稳定
            FileUtils.copyFile(src,dst);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new Response(Response.Status.OK,
                    MimeType.JSON.getType(),
                    JSON.toJSONString(new UploadResult(false,
                            System.currentTimeMillis() - startTime,
                            ioe.getMessage())));
        }

        long endTime = System.currentTimeMillis();
        // Response for success
        return new Response(Response.Status.OK,
                MimeType.JSON.getType(),
                JSON.toJSONString(new UploadResult(true,endTime - startTime,"上传成功！")));
    }

    /**
     * 处理CSS，JavaScript，HTML文件请求的方法
     * @param filename 请求的文件名
     * @return Response
     */
    private Response responseFile(String filename){
        try {
            File file = new File(htmlFile.toString() + "/"+filename);
            if (!file.exists()) return new Response("");
            byte[] tmp = FileUtils.readFileToByteArray(file);
            String str = new String(tmp);
            Log.i(TAG, "responseFile: tmp.length："+tmp.length);
            String mimeType = MimeType.HTML.getType();
            if (filename.indexOf(".css")!=-1)
                mimeType = MimeType.CSS.getType();
            else if (filename.indexOf(".js")!=-1)
                mimeType = MimeType.JAVASCRIPT.getType();
            else if (filename.indexOf(".ico")!=-1)
                mimeType = MimeType.ICO.getType();
            return new Response(Response.Status.OK,mimeType,str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Response("");
    }
}

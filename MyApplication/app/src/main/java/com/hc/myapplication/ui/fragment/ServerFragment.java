package com.hc.myapplication.ui.fragment;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hc.myapplication.R;
import com.hc.myapplication.server.MultipartServer;
import com.hc.myapplication.utils.FileManager;

import java.io.IOException;

/**
 * Created by 诚 on 2016/7/19.
 */
public class ServerFragment extends Fragment {
    public final static int id = 0;

    private String TAG = "ServerFragment";

    private MultipartServer mServer = new MultipartServer();

    public static ServerFragment newInstance(){
        return new ServerFragment();
    }

    private Button mButton;
    private ImageView ivServerBg;

    public void startServer(View view){
        if (!mServer.isAlive()){
            try {
                mServer.start();
                updateView();
            } catch (IOException e) {
                Log.e(TAG, "startServer: ", e);
                updateView();
            }
        } else {

        }
        Snackbar.make(getView(),"服务器已启动",Snackbar.LENGTH_SHORT).show();
    }

    private void initFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.writeFile(getActivity(),R.raw.hello,FileManager.INDEX,mServer);
                FileManager.writeFile(getActivity(),R.raw.bootstrap,FileManager.BOOTSTRAP,mServer);
                FileManager.writeFile(getActivity(),R.raw.jquery,FileManager.JQUERY,mServer);
                FileManager.writeFile(getActivity(),R.raw.favicon,FileManager.FAVICON,mServer);
                FileManager.writeFile(getActivity(),R.raw.jquery_form,FileManager.JQEURY_FORM,mServer);
                FileManager.writeFile(getActivity(),R.raw.uploader,FileManager.UPLOADER,mServer);
                FileManager.writeFile(getActivity(),R.raw.app,FileManager.APP,mServer);
                FileManager.judgeFileExsits();
            }
        }).start();
    }

    private void updateView(){
        Log.i(TAG, "updateView: 服务器的运行状态:"+(mServer.isAlive()?"还活着":"死了"));
        if (mServer == null || !mServer.isAlive())
            return;
        mButton.setText("http".toLowerCase()+"://"+getLocalIpStr(getContext())+":"+mServer.mPort);
        mButton.setEnabled(false);
        Log.i(TAG, String.valueOf("updateView: "+mButton == null));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: Fragment Create");
        updateView();
        initFiles();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: 锁屏回来");
        updateView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);

        mButton = (Button) view.findViewById(R.id.btn_ip);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startServer(view);
            }
        });
        ivServerBg = (ImageView) view.findViewById(R.id.iv_server_bg);
        Glide.with(this).load(R.mipmap.server_bg).into(ivServerBg);
        return view;
    }

    /**
     * 拿到手机连接wifi后的IP地址
     * @param context
     * @author HC
     * @return
     */
    private String getLocalIpStr(Context context) {
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return intToIpAddr(wifiInfo.getIpAddress());
    }
    /**
     * 拿到IP地址后输出
     * @author HC
     * @param ip
     * @return
     */
    private String intToIpAddr(int ip) {
        return (ip & 0xff) + "." + ((ip>>8)&0xff) + "." + ((ip>>16)&0xff) + "." + ((ip>>24)&0xff);
    }
}

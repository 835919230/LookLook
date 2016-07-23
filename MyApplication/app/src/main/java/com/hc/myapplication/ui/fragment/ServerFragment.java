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
import com.hc.myapplication.utils.WiFiUtils;

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
                Snackbar.make(getView(),"服务器已启动",Snackbar.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e(TAG, "startServer: ", e);
                mServer = new MultipartServer(++mServer.mPort);
                startServer(view);
            }
        } else {

        }
    }

    private void initFiles() {
        FileManager.initFiles(getActivity(),mServer);
    }

    private void updateView(){
        Log.i(TAG, "updateView: 服务器的运行状态:"+(mServer.isAlive()?"还活着":"死了"));
        if (mServer == null || !mServer.isAlive())
            return;
        mButton.setText("http".toLowerCase()+"://"+ WiFiUtils.getLocalIpStr(getContext())+":"+mServer.mPort);
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

}

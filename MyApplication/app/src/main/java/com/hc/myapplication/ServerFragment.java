package com.hc.myapplication;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hc.myapplication.Server.MultipartServer;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 诚 on 2016/7/19.
 */
public class ServerFragment extends Fragment {


    private String TAG = "ServerFragment";

    private MultipartServer mServer = new MultipartServer();

    public static ServerFragment newInstance(){
        return new ServerFragment();
    }

    private Button mButton;

    //@OnClick(R.id.btn_ip)
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
        Toast.makeText(getActivity(),"Click",Toast.LENGTH_SHORT).show();
    }

    private void updateView(){
        Log.i(TAG, "updateView: 服务器的运行状态:"+(mServer.isAlive()?"还活着":"死了"));
        if (mServer == null || !mServer.isAlive())
            return;
        mButton.setText("http://"+getLocalIpStr(getContext())+":"+mServer.mPort);
        mButton.setEnabled(false);
        Log.i(TAG, String.valueOf("updateView: "+mButton == null));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: Fragment Create");
        updateView();
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

//        ButterKnife.bind(this,view);
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

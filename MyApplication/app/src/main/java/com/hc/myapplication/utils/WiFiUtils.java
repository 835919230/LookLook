package com.hc.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by 诚 on 2016/7/23.
 */
public class WiFiUtils {

    /**
     * 判断WiFi是否连接的方法
     * @author HC
     * @param context 系统的上下文对象，负责拿到WiFi的状态
     * @return 是否连接WiFi的布尔值
     */
    public static boolean isWiFiActive(Context context){
        boolean flag = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo.State state = manager
                                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                                .getState();
        if (state == NetworkInfo.State.CONNECTED) {
            flag = true;
        }
        return flag;
    }

    /**
     * 拿到手机连接wifi后的IP地址
     * @param context
     * @author HC
     * @return
     */
    public static String getLocalIpStr(Context context) {
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
    public static String intToIpAddr(int ip) {
        return (ip & 0xff) + "." + ((ip>>8)&0xff) + "." + ((ip>>16)&0xff) + "." + ((ip>>24)&0xff);
    }
}

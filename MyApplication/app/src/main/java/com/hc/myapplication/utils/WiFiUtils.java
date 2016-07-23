package com.hc.myapplication.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by 诚 on 2016/7/23.
 */
public class WiFiUtils {

    public static boolean isWiFiActive(){
        // TODO: 2016/7/23 判断WiFi状态的方法
        return true;
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

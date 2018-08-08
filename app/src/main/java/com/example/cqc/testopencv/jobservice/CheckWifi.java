package com.example.cqc.testopencv.jobservice;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.WIFI_SERVICE;

public class CheckWifi {


    public static boolean isWifiSetPortal() {
        String mWalledGardenUrl = "http://g.cn/generate_204";
        // 设置请求超时
        int WALLED_GARDEN_SOCKET_TIMEOUT_MS = 6000;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(mWalledGardenUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setConnectTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
            urlConnection.setReadTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
            urlConnection.setUseCaches(false);
            urlConnection.getInputStream();
            // 判断返回状态码是否204
            return urlConnection.getResponseCode()!=204;
        } catch (IOException e) {
            //   e.printStackTrace();
            return false;
        } finally {
            if (urlConnection != null) {
                //释放资源
                urlConnection.disconnect();
            }
        }
    }
    public static boolean isZzuwlan(Context context){
        //获取当前连接的wifi信息
        WifiManager wifiManager= (WifiManager)context.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = null;
        if (wifiManager != null) {
            wifiInfo = wifiManager.getConnectionInfo();
            Log.d("我的 checkwifi：",""+wifiInfo.getSSID());
            if(wifiInfo.getSSID().contains("zzuwlan")){
                return true;
            }else {
                Log.d("我的，判断","当前wifi不是zzu的");
                return false;
            }
        }else{
            return false;
        }
    }
}

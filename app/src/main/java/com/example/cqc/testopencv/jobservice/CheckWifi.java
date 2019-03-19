package com.example.cqc.testopencv.jobservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckWifi {

    public  String getWiFiNameInP(Context context){
        String wifiName = "unknown wifi";
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo netwifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        wifiName = netwifiInfo.getExtraInfo();
        if (wifiName.startsWith("\"")) {
            wifiName = wifiName.substring(1, wifiName.length());
        }
        if (wifiName.endsWith("\"")) {
            wifiName = wifiName.substring(0, wifiName.length() - 1);
        }
        Log.d("我的 checkwifi(android P)",wifiName);
        return wifiName;
    }

    public  boolean isWifiSetPortal() {
        String mWalledGardenUrl = "http://g.cn/generate_204";
        // 设置请求超时
        int WALLED_GARDEN_SOCKET_TIMEOUT_MS = 3000;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(mWalledGardenUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setConnectTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
            urlConnection.setReadTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
            urlConnection.setUseCaches(false);
            //urlConnection.getInputStream();
            // 判断返回状态码是否204
            Log.d("Checkwifi:","是否需要认证："+(urlConnection.getResponseCode()!=204));
            return urlConnection.getResponseCode()!=204;
        } catch (IOException e) {
            Log.v("Checkwifi",e.getMessage());
               e.printStackTrace();

            return false;
        } finally {
            if (urlConnection != null) {
                //释放资源
                urlConnection.disconnect();
            }
        }
    }
    public  static boolean isZzuwlan(Context context){

       String wifiName = getSSID(context);
        Log.d("我的 checkwifi：",""+wifiName);
       if(wifiName.contains("zzuwlan")){
           return true;
       }else{
           return false;
       }
    }
 private static String getSSID(Context context){

     String ssid="unknown id";

     if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O|| Build.VERSION.SDK_INT== Build.VERSION_CODES.P) {

         WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

         assert mWifiManager != null;
         WifiInfo info = mWifiManager.getConnectionInfo();
         return info.getSSID().replace("\"", "");
     } else if (Build.VERSION.SDK_INT== Build.VERSION_CODES.O_MR1){

         ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
         assert connManager != null;
         NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
         if (networkInfo.isConnected()) {
             if (networkInfo.getExtraInfo()!=null){
                 return networkInfo.getExtraInfo().replace("\"","");
             }
         }
     }
     Log.d("我的 checkwifi：",""+ssid);
     return ssid;

 }

}

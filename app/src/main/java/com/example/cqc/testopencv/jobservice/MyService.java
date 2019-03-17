package com.example.cqc.testopencv.jobservice;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.cqc.testopencv.CheckInfo;
import com.example.cqc.testopencv.SaveRefererActivity;

public class MyService extends Service {
    private CheckInfo checkInfo;
private boolean isChanged=false;
    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(),"服务已创建", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),"服务已启动", Toast.LENGTH_SHORT).show();
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (CheckWifi.isZzuwlan(getApplicationContext())) {
                        Log.d("我的job", "当前wifif是zzu");
                        //获取ip地址
                        if (!getlocalip().equals("")) {
                            //将ip存入data文件
                            checkInfo = new CheckInfo(getApplicationContext());
                            Log.d("我的ip", getlocalip());
                            checkInfo.saveString("ip", getlocalip());
                            //判断当前网络是否需要认证
                            boolean flag = CheckWifi.isWifiSetPortal();
                            if (flag) {//需要认证
                                Log.d("我的job", "开启自动认证");
                                Intent intents = new Intent(getApplicationContext(), SaveRefererActivity.class);
                                startActivity(intents);
                            } else {
                                Log.d("我的job", "已经认证过了");
                            }
                        }
                    }else{
                        isChanged=true;
                    }
                }
            });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onDestroy();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if(isChanged){
            isChanged=false;
            Toast.makeText(getApplicationContext(),"当前网络不是郑州大学的", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getApplicationContext(),"服务已关闭", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private String getlocalip(){

        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager==null){
            return "";
        }else {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            if (ipAddress == 0) return "未连接wifi";
            return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                    + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
        }
    }
}

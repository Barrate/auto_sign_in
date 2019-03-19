package com.example.cqc.testopencv;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.cqc.testopencv.jobservice.CheckWifi;

/**
 * Implementation of App Widget functionality.
 */
public class StartActivity extends AppWidgetProvider {
    private static boolean isConnected = false;
    protected static boolean flag = false;
    protected static boolean isZzuwlan = false;
    //protected static RemoteViews views;

   // static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
     //                           int appWidgetId) {
    //}
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            //updateAppWidget(context, appWidgetManager, appWidgetId);
            CharSequence widgetText = context.getString(R.string.appwidget_text);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.start_service);
            views.setTextViewText(R.id.appwidget_text, widgetText);
            startActivity(views,context);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
    @Override
    public void onEnabled(Context context) { }
    @Override
    public void onDisabled(Context context) { }


    public  void startActivity(RemoteViews views , Context context){
        Toast.makeText(context,"开始判断是否需要认证当前WiFi",Toast.LENGTH_SHORT).show();


        try {
          //  Thread thread = new Thread(new Runnable() {
          //      @Override
           //     public void run() {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    CheckWifi checkWifi = new CheckWifi();
                    flag = checkWifi.isWifiSetPortal();

                }
            });
            thread.start();
            thread.join();
            CheckInfo checkInfo;
            if (CheckWifi.isZzuwlan(context)) {
                Log.v("StartActivity", "当前wifif是zzu");
                        //获取ip地址
                if (!getLocalIp(context).equals("")) {
                            //将ip存入data文件
                    checkInfo = new CheckInfo(context);
                    Log.v("StartActivity", getLocalIp(context));
                    checkInfo.saveString("ip", getLocalIp(context));
                    //判断当前网络是否需要认证

                    Log.v("StartActivity", "开启自动认证:" + flag);
                    if (flag) {//需要认证
                        Log.v("StartActivity", "开启自动认证");
                        // Intent intents = new Intent(getApplicationContext(), SaveRefererActivity.class);
                        // startActivity(intents);
                        Intent intent = new Intent(context.getApplicationContext(), SaveRefererActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, 0);
                        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
                    } else {
                        // Toast.makeText(context,"当前网络目前不需要认证！",Toast.LENGTH_SHORT).show();
                        Log.v("StartActivity", "已经认证过了");
                    }
                }
            } else {
                isConnected = true;
                Log.v("StartActivity:", "当前网络不是校园网");
            }
           //     }
          //  });
          // thread.start();
          // thread.join();
           if(isConnected){
               Toast.makeText(context,"当前网络目前不需要认证！",Toast.LENGTH_SHORT).show();
               isConnected = false;
           }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //获取wifi分配的ip地址
    private static String getLocalIp(Context context){

        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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


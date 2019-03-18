package com.example.cqc.testopencv;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.cqc.testopencv.jobservice.AlwaysRunningService;
import com.example.cqc.testopencv.jobservice.CheckWifi;
import com.example.cqc.testopencv.jobservice.MyService;

/**
 * Implementation of App Widget functionality.
 */
public class StartService extends AppWidgetProvider {
    private static CheckInfo checkInfo;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.start_service);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        /*
        Intent intentb = new Intent(context.getApplicationContext(), AlwaysRunningService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            PendingIntent pendingIntent =    PendingIntent.getForegroundService(context.getApplicationContext(),0,intentb,0);
           // PendingIntent pendingIntent =    PendingIntent.getService(context.getApplicationContext(),0,intentb,0);
            views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
        }else{
            Intent intenta = new Intent(context.getApplicationContext(), MyService.class);
            PendingIntent pendingIntent =    PendingIntent.getService(context.getApplicationContext(),0,intenta,0);
            views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
        }
        */
        startActivity(views,context);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override
    public void onEnabled(Context context) { }
    @Override
    public void onDisabled(Context context) { }


    public static void startActivity(RemoteViews views,Context context){

        if (CheckWifi.isZzuwlan(context)) {
            Log.d("我的job", "当前wifif是zzu");
            //获取ip地址
            if (!getLocalIp(context).equals("")) {
                //将ip存入data文件
                checkInfo = new CheckInfo(context);
                Log.d("我的ip", getLocalIp(context));
                checkInfo.saveString("ip", getLocalIp(context));
                //判断当前网络是否需要认证
                boolean flag = CheckWifi.isWifiSetPortal();
                if (flag) {//需要认证
                    Log.d("我的job", "开启自动认证");
                   // Intent intents = new Intent(getApplicationContext(), SaveRefererActivity.class);
                   // startActivity(intents);
                    Intent intent = new Intent(context.getApplicationContext(), SaveRefererActivity.class);
                    PendingIntent pendingIntent =    PendingIntent.getActivity(context.getApplicationContext(),0,intent,0);
                    views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
                } else {
                    Log.d("我的job", "已经认证过了");
                }
            }
        }else{

            Log.d("StartService:","当前网络不是校园网");
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


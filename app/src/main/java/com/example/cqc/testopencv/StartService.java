package com.example.cqc.testopencv;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.cqc.testopencv.jobservice.ForegroundService;

/**
 * 桌面小部件
 */
public class StartService extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {

        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            //updateAppWidget(context, appWidgetManager, appWidgetId);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.start_service);
            startService(views, context, appWidgetManager, appWidgetId,R.id.appwidget_text);
        }
    }

    public void startService(RemoteViews views, Context context, AppWidgetManager appWidgetManager, int appWidgetId,int rsID) {
        Toast.makeText(context, "开始判断是否需要认证当前WiFi", Toast.LENGTH_SHORT).show();

        Log.v("StartService", "开启自动认证");
        Intent intent = new Intent(context.getApplicationContext(), ForegroundService.class);
        intent.setAction("open");
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getForegroundService(context.getApplicationContext(), 0, intent, 0);
        }else{
            pendingIntent = PendingIntent.getService(context.getApplicationContext(), 0, intent, 0);
        }

        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }


}

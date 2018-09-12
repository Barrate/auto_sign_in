package com.example.cqc.testopencv;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.cqc.testopencv.jobservice.AlwaysRunningService;

/**
 * Implementation of App Widget functionality.
 */
public class StartService extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.start_service);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent intentb = new Intent(context.getApplicationContext(),AlwaysRunningService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            PendingIntent pendingIntent =    PendingIntent.getForegroundService(context.getApplicationContext(),0,intentb,0);
            views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
        }else{
            PendingIntent pendingIntent =    PendingIntent.getService(context.getApplicationContext(),0,intentb,0);
            views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
        }
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

}


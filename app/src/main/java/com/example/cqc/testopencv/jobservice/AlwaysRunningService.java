package com.example.cqc.testopencv.jobservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.cqc.testopencv.CheckInfo;
import com.example.cqc.testopencv.R;
import com.example.cqc.testopencv.SaveRefererActivity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AlwaysRunningService extends JobIntentService {
    private  CheckInfo checkInfo;
    private PowerManager.WakeLock mWakeLock;
    private boolean isChanged= false;

    static void enqueueWork(Context context) {
        enqueueWork(context, AlwaysRunningService.class, 123, new Intent());
    }

    @Override
    public void onCreate() {
        //如果API在26以上即版本为O则调用startForefround()方法启动服务
        Toast.makeText(getApplicationContext(),"服务已创建",Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setForegroundService();
        }
      //  Toast.makeText(getApplicationContext(),"服务开启",Toast.LENGTH_LONG).show();
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // onHandleWork(intent);

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
                    onDestroy();
                }
            });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     //   onDestroy();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.O)


    public void  setForegroundService()
    {
        //安卓8.0及其以上的通知建立方式
        //设定的通知渠道名称
        String channelName = "wifi认证";
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_MIN;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel("100", channelName, importance);
        channel.setDescription("abcdef");
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "100");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground) //设置通知图标
                .setContentTitle("wifi认证服务")//设置通知标题
                .setContentText("running.....")//设置通知内容
                .setAutoCancel(true) //用户触摸时，自动关闭
                .setOngoing(true);//设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(100,builder.build());
    }





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //获取wifi分配的ip地址
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
 /**
          * 同步方法   得到休眠锁
          * @param context
          * @return
          */
          synchronized private void getLock(Context context){
            if(mWakeLock==null){
            PowerManager mgr=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
            mWakeLock=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,AlwaysRunningService.class.getName());
            mWakeLock.setReferenceCounted(true);
            Calendar c= Calendar.getInstance();
            c.setTimeInMillis((System.currentTimeMillis()));
            int hour =c.get(Calendar.HOUR_OF_DAY);
            if(hour>=23||hour<=6){
            mWakeLock.acquire(5000);
            }else{
            mWakeLock.acquire(300000);
        }
        }
          }
    synchronized private void releaseLock()
    {
        if(mWakeLock!=null){
            if(mWakeLock.isHeld()) {
                mWakeLock.release();
            }
            mWakeLock=null;
        }
    }
    @Override
    public void onDestroy() {
        Log.d("我的service","销毁");
        if(isChanged){
            isChanged=false;
            Toast.makeText(getApplicationContext(),"当前网络不是郑州大学的",Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(true);
        }
        super.onDestroy();
    }


}

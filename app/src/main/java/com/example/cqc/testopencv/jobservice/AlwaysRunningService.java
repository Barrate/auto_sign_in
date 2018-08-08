package com.example.cqc.testopencv.jobservice;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.cqc.testopencv.CheckInfo;
import com.example.cqc.testopencv.SaveRefererActivity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AlwaysRunningService extends Service {
    private TimerTask timerTask = null;
    private Timer timer = null;
    private  CheckInfo checkInfo;
    private PowerManager.WakeLock mWakeLock;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getLock(getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                timer = new Timer();
                timerTask = new TimerTask() {
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
                                    Intent intent = new Intent(getApplicationContext(), SaveRefererActivity.class);
                                    startActivity(intent);
                                }else{Log.d("我的job","已经认证过了");}
                            }
                        }else {
                            Log.d("我的job", "当前wifif不是zzu");
                        }
                    }
                };
                timer.schedule(timerTask,0,15*1000);//每十五秒检测一次

            }
        }).start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        releaseLock();
        openJobService();
        if (timer!=null){
            timer.cancel();
            timer = null;
        }


        Log.d("我的service","销毁");
        super.onDestroy();
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
    public  void openJobService(){

        JobInfo.Builder builder = new JobInfo.Builder(1,new ComponentName(this,Myjob.class));
        //其实就是监测wifi
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        // builder.setBackoffCriteria(1000,JobInfo.BACKOFF_POLICY_LINEAR);
        builder.setBackoffCriteria(10*1000,JobInfo.BACKOFF_POLICY_LINEAR);
            builder.setPeriodic(5000);
        JobScheduler jobScheduler =(JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(builder.build());
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

}

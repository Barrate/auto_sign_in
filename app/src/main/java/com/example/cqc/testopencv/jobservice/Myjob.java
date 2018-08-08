package com.example.cqc.testopencv.jobservice;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.cqc.testopencv.CheckInfo;
import com.example.cqc.testopencv.MainActivity;
import com.example.cqc.testopencv.SaveRefererActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Myjob extends JobService {
    private CheckInfo checkInfo;
    @Override
    public boolean onStartJob( JobParameters params) {
        Log.d("我的job","进行");

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
                                    Intent intent = new Intent(getApplicationContext(), AlwaysRunningService.class);
                                    startService(intent);
                                }else{Log.d("我的job","已经认证过了");}
                            }
                        }else {
                            Log.d("我的job", "当前wifif不是zzu");
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
            //        jobFinished(params,true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
Log.d("我的job","执行完毕");
     return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("我的job","结束");
        return false;
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


//    private class MonitorAsyncTask extends AsyncTask<JobParameters,Void,String>{
//        private JobParameters mJobParameters;
//
//        @Override
//        protected String doInBackground(JobParameters... jobParameters) {
//            android.util.Log.d("我的job", "doInBackground: running");
//            mJobParameters = jobParameters[0];
//            //耗时操作
//            if(wifiManager!=null) {
//               WifiInfo wifiInfo =  wifiManager.getConnectionInfo();
//                while (wifiInfo.getSSID().contains("zzuwlan")){//如果当前wifi是zzuwlan
//
//                }
//            }
//            return null;
//        }
//        //完成之后的处理工作
//        @Override
//        protected void onPostExecute(String s) {
//            android.util.Log.d("我的job线程", "onPostExecute: running");
//            super.onPostExecute(s);
//            //如果不调用此方法，任务只会执行一次
//            jobFinished(mJobParameters,false);
//        }
//    }
}

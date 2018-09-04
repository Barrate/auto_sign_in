package com.example.cqc.testopencv;

import android.Manifest;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cqc.testopencv.jobservice.AlwaysRunningService;
import com.example.cqc.testopencv.jobservice.Myjob;

public class WelocmeIndex extends AppCompatActivity {
    private    Intent intent;
    private Button save ,openService;
    private EditText username,userpsw;
    private String id,psw;
    private CheckInfo  checkInfo ;
    private CopyFile copyFile ;
    private TextView about_tv,donate_tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        copyFile = new CopyFile();
        //按钮
        checkInfo = new CheckInfo(getApplicationContext());
        save = findViewById(R.id.save);
        openService = findViewById(R.id.openService);
        username = findViewById(R.id.username);
        userpsw = findViewById(R.id.userpsw);
        about_tv = findViewById(R.id.about_tv);
   //     about_tv.setTextIsSelectable(true);
        donate_tv = findViewById(R.id.donate_tv);
    //    donate_tv.setTextIsSelectable(true);
        //初始化控件
        initView();
        //申请权限
        openPermission();
        Button closeBtn = findViewById(R.id.close_service);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(WelocmeIndex.this,AlwaysRunningService.class);
                stopService(stopIntent);
                Toast.makeText(getApplicationContext(),"已关闭服务",Toast.LENGTH_SHORT).show();
                JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                jobScheduler.cancel(1);
            }
        });
        Button about_btn = findViewById(R.id.about_btn);
        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about_tv.setText(R.string.about);
                //donate_tv.setText(R.string.donate);
            }
        });
    }

    @Override
    protected void onResume() {
//            webView.loadUrl("http://baidu.com");
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//
//            webView.setWebViewClient(new WebViewClient(){
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                    return super.shouldOverrideUrlLoading(view,request);
//                }
//
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    refererUrl = url;
//                    super.onPageFinished(view, url);
//                }
//            });
        super.onResume();
    }

    private  void initView(){


           save.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   id = username.getText().toString();
                   psw = userpsw.getText().toString();
                   if("".equals(id)&&"".equals(psw)){
                       Toast.makeText(getApplicationContext(),"请先输入学号，密码（^ v ^ )",Toast.LENGTH_SHORT).show();
                   }else {
                       checkInfo.saveString("username",id);
                       checkInfo.saveString("userpsw",psw);
                           openService.setClickable(true);
                           Toast.makeText(getApplicationContext(),"信息已保存到（^ v ^ )",Toast.LENGTH_SHORT).show();
//                       }else{
//                           Toast.makeText(getApplicationContext(),"您当前使用的网络已经不需要认证了",Toast.LENGTH_SHORT).show();
//                       }

                   }
               }
           });
           if(openService.isClickable()){
               openService.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Log.d("我的log","服务正在开启");
                    // openService();
                       Toast.makeText(getApplicationContext(),"已开启服务",Toast.LENGTH_SHORT).show();
                            checkInfo.saveBoolean("receiverisopen",true);
                       AlwaysRunningService service = new AlwaysRunningService();
                        intent = new Intent(WelocmeIndex.this,service.getClass());
                      if(!isMyServiceRunning(service.getClass())){
                          startService(intent);
                      }
                   }
               });

           }
    }

//    public  void openService(){
//        JobInfo.Builder builder = new JobInfo.Builder(1,new ComponentName(this,Myjob.class));
//        //其实就是监测wifi
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
//       // builder.setBackoffCriteria(1000,JobInfo.BACKOFF_POLICY_LINEAR);
//        builder.setBackoffCriteria(10*1000,JobInfo.BACKOFF_POLICY_LINEAR);
//        builder.setMinimumLatency(100);
//        builder.setOverrideDeadline(150);
//        JobScheduler jobScheduler =(JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        if (jobScheduler != null) {
//            jobScheduler.schedule(builder.build());
//        }
//    }

    //申请读写权限
    public void  openPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 判断是否有这个权限，是返回PackageManager.PERMISSION_GRANTED，否则是PERMISSION_DENIED
            // 这里我们要给应用授权所以是!= PackageManager.PERMISSION_GRANTED
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 如果应用之前请求过此权限但用户拒绝了请求,且没有选择"不再提醒"选项 (后显示对话框解释为啥要这个权限)，此方法将返回 true。
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    // requestPermissions以标准对话框形式请求权限。123是识别码（任意设置的整型），用来识别权限。应用无法配置或更改此对话框。
                    //当应用请求权限时，系统将向用户显示一个对话框。当用户响应时，系统将调用应用的 onRequestPermissionsResult() 方法，向其传递用户响应。您的应用必须替换该方法，以了解是否已获得相应权限。回调会将您传递的相同请求代码传递给 requestPermissions()。
                    ActivityCompat.requestPermissions(this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.INTERNET},
                            123);
                }
            }else {

                Log.d("我的Welcome",String.valueOf(checkInfo.getBoolean("saved")));
                if(!checkInfo.getBoolean("saved")){//如果用户之前没保存过信息的话，open不可用
                    openService.setClickable(false);
                    checkInfo.saveBoolean("saved",true);
                    //将识别库保存到手机上
                    copyFile.copy(this);
                }else{//保存过
                    openService.setClickable(true);
                    save.setText("你已经保存过信息了，再次点击会覆盖");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(!checkInfo.getBoolean("saved")){//如果用户之前没保存过信息的话，open不可用
            openService.setClickable(false);
            checkInfo.saveBoolean("saved",true);
            //将识别库保存到手机上
            copyFile.copy(this);
        }else{//保存过
            openService.setClickable(true);
            save.setText("你已经保存过信息了，再次点击会覆盖");
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    private void initHandler(){
//        handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what){
//                    case  1:
//                        Toast.makeText(WelocmeIndex.this,"当前认证路由器无法连接到网络",Toast.LENGTH_LONG).show();
//                        break;
//                        case 2:
//                            Toast.makeText(WelocmeIndex.this,"当前认证路由器无法连接到网络",Toast.LENGTH_LONG).show();
//                            break;
//                    case 3:
//                        refererUrl = msg.getData().getString("url");
//                        checkInfo.saveString("refererUrl",refererUrl);
//                        Toast.makeText(WelocmeIndex.this,refererUrl+"信息获取完毕，可以开启服务",Toast.LENGTH_LONG).show();
//                        break;
//
//                }
//            }
//        };
//
//    }
    //判断当前服务是否已经存在
private boolean isMyServiceRunning(Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.getName().equals(service.service.getClassName())) {
            Log.i ("isMyServiceRunning?", true+"");
            return true;
        }
    }
    Log.i ("isMyServiceRunning?", false+"");
    return false;
}

}

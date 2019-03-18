package com.example.cqc.testopencv;

import android.Manifest;
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

public class WelocmeIndex extends AppCompatActivity {
    private Button save ;
    private EditText username,userpsw;
    private String id,psw;
    private CheckInfo checkInfo ;
    private CopyFile copyFile ;
    private TextView about_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        copyFile = new CopyFile();
        //按钮
        checkInfo = new CheckInfo(getApplicationContext());
        save = findViewById(R.id.save);
        username = findViewById(R.id.username);
        userpsw = findViewById(R.id.userpsw);
        about_tv = findViewById(R.id.about_tv);
        about_tv.setTextIsSelectable(true);
        //初始化控件
        initView();
        //申请权限
        openPermission();

        Button about_btn = findViewById(R.id.about_btn);
        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about_tv.setText(R.string.about);
                //donate_tv.setText(R.string.donate);
            }
        });
    }

    private  void initView(){
           save.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   id = username.getText().toString();
                   psw = userpsw.getText().toString();
                   if("".equals(id)&&"".equals(psw)){
                       Toast.makeText(getApplicationContext(),"请先输入学号，密码（^ v ^ )", Toast.LENGTH_SHORT).show();
                   }else {
                       checkInfo.saveString("username",id);
                       checkInfo.saveString("userpsw",psw);
                       //    openService.setClickable(true);
                           Toast.makeText(getApplicationContext(),"信息已保存（^ v ^ )", Toast.LENGTH_SHORT).show();
                       save.setText("已保存");

                   }
               }
           });

    }

    //申请读写权限
    public void  openPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 判断是否具有读写内存的权限和定位权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                   //请求权限
                    ActivityCompat.requestPermissions(this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.FOREGROUND_SERVICE},
                            1);
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.FOREGROUND_SERVICE},
                            2);
            }else {
                Log.d("我的Welcome", String.valueOf(checkInfo.getBoolean("saved")));
                if(!checkInfo.getBoolean("saved")){//如果用户之前没保存过信息的话，open不可用
                    //openService.setClickable(false);
                    checkInfo.saveBoolean("saved",true);
                    //将识别库保存到手机上
                    copyFile.copy(this);
                }else{//保存过
                  //  openService.setClickable(true);
                    save.setText("你已经保存过信息了，再次点击会覆盖");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(!checkInfo.getBoolean("saved")){//如果用户之前没保存过信息的话，open不可用
            checkInfo.saveBoolean("saved",true);
            //将识别库保存到手机上
            copyFile.copy(this);
        }else{//保存过
            // openService.setClickable(true);
            save.setText("你已经保存过信息了，再次点击会覆盖");
        }
     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}

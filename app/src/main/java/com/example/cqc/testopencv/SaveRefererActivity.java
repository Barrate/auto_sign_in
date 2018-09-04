package com.example.cqc.testopencv;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SaveRefererActivity extends AppCompatActivity {
    private CheckInfo checkInfo;
   private  WebView webView;
   private String sUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        openPermission();
        checkInfo = new CheckInfo(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index2);

         webView = findViewById(R.id.index2_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.baidu.com");

    }

    @Override
    protected void onResume() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //refererUrl = url;
                if(!url.equals("http://www.baidu.com/")) {
                    checkInfo.saveString("referer", url);
                    Log.d("我的SaveRefererActivity:", url);
                    sUrl = url;
                    finish();
                }
                super.onPageFinished(view, url);

             //   finish();
            }
        });
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
      //  Toast.makeText(this,"活动已经销毁",Toast.LENGTH_LONG).show();
    }
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
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        checkInfo.saveString("referer",sUrl);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

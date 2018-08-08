package com.example.cqc.testopencv;

import android.Manifest;

import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.cqc.testopencv.jobservice.Myjob;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private  CheckInfo checkInfo;
    //账号
    private String name ;
    //http请求的referer信息
    private String referer;
    //url
    private String mainactivity_url;
    //结果页url
    private  String resultUrl;
    //密码
    private String psw;
    //验证码识别结果
    private String result=null;
    private WebView webView= null;
    private Map <String ,String> map = new HashMap<>();
    //验证码处理工具类
    private  UseImagetool  useImagetool;
    private  StringUtil stringUtil = new StringUtil();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //加载类库
        checkInfo = new CheckInfo(getApplicationContext());
        loadLibs();
        //WEBVIEW
        webView = findViewById(R.id.web_view);
        name = checkInfo.getString("username");
         psw = checkInfo.getString("userpsw");
        //按钮
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("已点击");
                finish();
                Log.d("我的网页浏览器","图片已识别:"+result);
            }
        });
    }
//oncreate结束
    @Override
    protected void onStart() {
        useImagetool = new UseImagetool();
       // openPermission();
        super.onStart();
    }
    @Override
    protected void onResume() {
        initWebView();
        super.onResume();
    }
    //自定义内容
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.d("我的", "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    //进行网页设置
    public void initWebView(){
        String ip = checkInfo.getString("ip");
        String refererUrl = checkInfo.getString("referer");
        Log.d("我的refererurl","  "+ refererUrl);
        referer =stringUtil.getReferer(refererUrl, ip);
        mainactivity_url = stringUtil.getUrl(referer);
        resultUrl = stringUtil.getResult(referer);
        map.put("Referer",referer);
        Log.d("我的request头：",referer+" | 请求页"+mainactivity_url+"  |referer:"+checkInfo.getString("referer"));
        webView.loadUrl(mainactivity_url,map);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                     if(request.getUrl().toString().equals(resultUrl)){
                       view.loadUrl(mainactivity_url,map);
                    }
                return super.shouldInterceptRequest(view, request);
            }
            @Override
            //网页加载完毕
            public void onPageFinished( WebView view, String url) {
                Log.d("我的网页，已加载完毕","当前url："+url);
                super.onPageFinished(view, url);
                if(url.contains(mainactivity_url)) {//仅在登陆界面执行该脚本
                   String code =  useImagetool.toDotool();
                     String js = "javascript: document.getElementsByName('uid').item(0).value = '" +
                            name
                            + "';document.getElementsByName('upw').item(0).value='" +
                            psw
                            + "';document.getElementsByName('ver6').item(0).value='" +
                            code
                            + "';document.getElementsByName('smbtn').item(0).click();" ;
                     Log.d("我的js",js);
                            //执行脚本，在这之前要对验证吧码识别，并传入到js中,要在这之前进行验证码的识别
                            view.evaluateJavascript(js, new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    Log.d("网页登陆1","----------");
                                }
                            });
                }
                //自动点击界面按钮
                String js2 = "javascript:document.getElementsByName('btn2').item(0).click();document.getElementById(\"btn_2\").click();";
                view.evaluateJavascript(js2, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("网页结果2","++++++++++++++++++++");
                    }
                });
//                else if(url.contains("https://edu3.v.zzu.edu.cn/ssss/wlogin.dll/login")){//验证界面
//
//                }
                 if(url.contains(resultUrl+"login2.zzj?id=")) {//验证成功页面
                    //关闭窗口
                    Toast.makeText(MainActivity.this,"已经成功认证",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }//网页设置结束
//加载c库
    public void loadLibs(){
        if (!OpenCVLoader.initDebug()) {
            Log.d("我的", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("我的", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"认证程序已经关闭",Toast.LENGTH_SHORT).show();
    }
}

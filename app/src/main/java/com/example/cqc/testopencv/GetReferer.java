package com.example.cqc.testopencv;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GetReferer {
    private android.os.Handler handler;
    private Context context;
    private Image image = null;
    private String url = "http://www.baidu.com";
    public  GetReferer(Handler handler, Context context){
     this.handler = handler;
     this.context = context;
 }
        public void send(){


            //向表格中插入数据
//            FormBody formBody = new FormBody.Builder().add("name","123").build();
//            builder.addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
//            builder.addHeader("connection", "keep-alive");
//            builder.addHeader("content-length", "96");
//            builder.addHeader("cache-control", "max-age=0");
//            builder.addHeader("accept-encoding","gzip, deflate, br");
//
//            //将请求头以键值对形式添加，可添加多个请求头
//            builder.post(formBody);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    Request.Builder builder = new Request.Builder().url(url);
                    Request request = builder.build();

                    OkHttpClient client = new OkHttpClient.Builder()
                            .readTimeout(30, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .build(); //设置各种超时时间
                    Call call = client.newCall(request);
                    try {
                        Response response = call.execute();

                        if (response != null) {
                            String url =  response.request().url().toString();
                            Log.d("我的OKHTTP；",url);
                            Message message = handler.obtainMessage(3);
                            Bundle bundle = new Bundle();
                            bundle.putString("url",url);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        } else {
                            handler.sendEmptyMessage(1);
                        }
                    } catch (IOException e) {
                        handler.sendEmptyMessage(2);
                        e.printStackTrace();
                    }
//                    try {
//                        URL uurl = new URL(url);
//                        HttpURLConnection connection = (HttpURLConnection) uurl.openConnection();
//                          String ss=  connection.getURL().toString();
//                          Log.d("我的getreferer",ss);
//                        Message message = handler.obtainMessage(3);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("url",ss);
//                            message.setData(bundle);
//                            handler.sendMessage(message);
//                    }  catch (MalformedURLException e) {
//                        handler.sendEmptyMessage(1);
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        handler.sendEmptyMessage(2);
//                        e.printStackTrace();
//                    }

                }
            }).start();


        }
}

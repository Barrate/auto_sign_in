package com.example.cqc.testopencv;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoadImage  {

     //    private Handler handler;
        private String strurl;
        private boolean flag = false;

        private static final String TAG = "我的网页浏览器：";

        public LoadImage(String strurl){
         //   this.handler = handler;
            this.strurl = strurl;
        }


        public void run() {
            URL url = null;
            HttpURLConnection con = null;

            try {
                url = new URL(strurl);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setReadTimeout(5000);
                con.setDoInput(true);
                //InputStream in = con.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/MyWebView/temp");
                if(!file.exists()&&!file.isDirectory()){
                    file.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/MyWebView/temp/yanzhengma.jpg");
                InputStream in = con.getInputStream();


                byte ch[] = new byte[2 * 1024];
                int len;

                    while ((len = in.read(ch)) != -1) {
                        fos.write(ch, 0, len);
                    }
                    fos.flush();
                    in.close();
                    fos.close();
                    flag = true;
                Log.d(TAG,  "下载完毕！");
//                final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

              //  handler.sendEmptyMessage(1);//通知程序已经下载完毕
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (Exception e){
                e.printStackTrace();

            }
        }

        public boolean isFinished(){
            //当下载结束时，标记为false，便于使用while循环阻塞，防止获取空对象
            return flag;
        }
    }



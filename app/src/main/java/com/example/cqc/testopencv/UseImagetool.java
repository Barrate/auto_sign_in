package com.example.cqc.testopencv;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UseImagetool  {
    /**
     * 下载图像并存储识别，返回识别结果
     * 对简单的噪点图片进行去噪
     * 图片识别步骤：1. 图片预处理，去噪  2.进行识别（可以调用百度的文字识别库）
     * 这里仅为预处理过程
     * 首先要对图片进行灰度化处理，其次进行二值化，最后才能进行去噪
     * 对于较小的噪点可以使用8邻域降噪，较大点的噪点使用连通域降噪
     *
     */
               // Bitmap图像OCR识别结果
    private TessBaseAPI tessBaseAPI;
    private  String result;

    public  String toDotool() {
            Thread thread =new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = "http://202.196.64.132:8080/sss/zzjlogin3d.dll/zzjgetimg";
                    LoadImage loadImage = new LoadImage(url);
                    Log.d("网页","开始下载---");
                    loadImage.run();
                    //最新图片已下载完
                    Log.d("我的图片：","正在处理图片");
                    File imgFile = new File(Environment.getExternalStorageDirectory().getPath()+"/MyWebView/temp/yanzhengma.jpg");
                    String dest =Environment.getExternalStorageDirectory().getPath()+"/MyWebView/temp/yanzhengma1.jpg";
                    //需要二值化的图片的Mat对象
                    Mat src = Imgcodecs.imread(imgFile.toString());
                    //中间类
                    Mat gray = new Mat(src.width(),src.height(),CvType.CV_8UC1);
                    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY,1);
                    //放大图片
                    ImageUtils imageutils = new ImageUtils(gray);
                    //二值化处理
                    imageutils.binaryzation();
                    //降噪
                    imageutils.contoursRemoveNoise(15);
                    //已处理过的gray图
                    Mat src1 = imageutils.getResult();
                    Size size = new Size(src1.width()*4,src1.height()*4);
                    Imgproc.resize(src1, src, size);
                    ImageUtils imageUtils1 = new ImageUtils(src);
                    imageUtils1.saveImg(dest);
                    //预处理后的图像存入sdcard/MyWebView/temp/yanzhengma1.jpg
                    Log.d("我的图片：","处理图片完成");

                    String datapath =Environment.getExternalStorageDirectory().getPath()+"/tesseract/";
                    File dir = new File(datapath+"tessdata/");
                    if (!dir.exists()) {
                        if( !dir.mkdirs()){
                            //文件夹创建失败
                        }
                    }
                    //图像识别
                    tessBaseAPI = new TessBaseAPI();
                    tessBaseAPI.init(datapath,"ma");
                    tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
                    String dest1 =Environment.getExternalStorageDirectory().getPath()+"/MyWebView/temp/yanzhengma1.jpg";
                    File file = new File(dest1);
                    tessBaseAPI.setImage(file);
                    result = tessBaseAPI.getUTF8Text();
                    Log.d("我的线程","执行完毕！");
                }
            });
              thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}
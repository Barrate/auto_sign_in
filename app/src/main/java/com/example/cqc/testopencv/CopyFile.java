package com.example.cqc.testopencv;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class   CopyFile {

    public  void copy(Context context){
     //   File srcfile = new File(Environment.getExternalStorageDirectory().getPath()+"/tesseract");
        File dirfile = new File(Environment.getExternalStorageDirectory().getPath()+"/tesseract/tessdata");
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/tesseract/tessdata/ma.traineddata");
        if(!dirfile.exists()){
            //如果存在就结束当前方法
            try {
                boolean flag1 = dirfile.mkdirs();
                boolean flag2 = file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{

        InputStream is =context.getAssets().open("tessdata/ma.traineddata") ;

        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int byteCount=0;
        while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
        fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
        }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();
        Log.d("我的，文件复制","成功");
        }catch (Exception e){
        Log.d("我的，文件复制","失败");
        e.printStackTrace();
        }

    }

}

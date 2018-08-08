package com.example.cqc.testopencv;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Set;

public class CheckInfo {
   private boolean flag = false;
   private  Context context;
   private SharedPreferences sharedPreferences;
    public CheckInfo(Context context){
       this.context = context;
   }

    public boolean check(){
         sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
       flag = sharedPreferences.getBoolean("first",true);
        return  flag;
    }

    public void saveString(String key , String value){
        sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putString(key,value);
       editor.apply();
       editor.commit();

   }

    public void saveBoolean(String key , Boolean value){
        sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
        editor.commit();
    }
    public String getString(String key){
        sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
      return   sharedPreferences.getString(key,"-1");
    }

    public Boolean getBoolean(String key){
        sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        return   sharedPreferences.getBoolean(key,false);
    }
}

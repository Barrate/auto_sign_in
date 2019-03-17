package com.example.cqc.testopencv;

public class StringUtil {

    public String getReferer(String src, String ip) {
        int a =src.indexOf("=")+1;
        int b = src.indexOf("&");
        String temp="",temp1="";
        if(a>0&&b>0) {
            temp = src.substring(0, a);
            temp1= src.substring(b);
            System.out.println("temp:"+temp);
            System.out.println("temp:"+temp1);
        }
        return temp+ip+temp1;
    }
    public String getResult(String src){
        String temp ="" ;
        int a = src.indexOf("?");
        if(a>2) {
            temp = src.substring(0, src.indexOf("?"));
        }
        return temp;

    }
    public String getUrl(String src) {
        String temp ="" ;
        int a = src.indexOf("?");
        if(a>2) {
            temp = src.substring(0, src.indexOf("?")-1)+":8080/login0.htm";
        }
        return temp;
    }
}

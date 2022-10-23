package com.mybatisplus.utils;

public class MakeNeko {
    public static String MakePicture(String url){
        return "[CAT:image,file=" +url + "]";
    }

    public static String MakeAt(String qq){
        return "[CAT:at,code="+qq+"]";
    }

    //Voice 语音消息
    //[CAT:voice,id=af24eb245,file=/img/b.mp4,url=http://voice/b.mp4]
    public static String MakeVoice(String url){
        return "[CAT:voice,url="+url+"]";
    }

    //File 文件消息
    //[CAT:file,id=af24eb245,file=/img/b.mp4,url=http://voice/b.mp4]
    public static String MakeFile (String url){
        return "[CAT:file,url="+url+"]";
    }
    //Share 分享
    //[CAT:share,title=标题,content=简述,url=http://forte.love,image=http://img/a.jpg]
    public static String MakeShare (String title,String content, String url){
        return "[CAT:share,title="+title+",content="+content+",url="+url+"]";
    }
    // nudge 戳一戳
    public static String Makepoke (String qq){
        return "[CAT:nudge,code="+qq+"]";
    }
}

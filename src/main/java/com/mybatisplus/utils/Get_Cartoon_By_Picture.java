package com.mybatisplus.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class Get_Cartoon_By_Picture {
    public String getCartoon(String url) throws Exception{
        /**
         * https://media.trace.moe/image/20519/%5BDHR%26Hakugetsu%5D%5BTamako%20Love%20Story%5D%5BMovie%5D%5BBIG5%5D%5B720P%5D%5BAVC_AAC%5D.mp4.jpg?t=4786.665&now=1663070400&token=QYDnO7TBVwHkzLLn2vEjWPv2Jvg±〖番剧编号〗
        **/
//        Connection.Response execute = Jsoup.connect("https://geoapi.qweather.com/v2/city/lookup?&key=e0757a5474934a88ae69a2e8f2746a83&location=新密")
//                .postDataCharset("UTF-8")
//                .ignoreContentType(true)
//                .execute();
//        String body = execute.body();
        Document document = Jsoup.connect("http://apii.gq/api/trace.php?type=text&url="+url+"&count=1").get();
        Elements body = document.select("body");
        String text = body.text();
        String replace = text.replace("〖", "\n〖");
        String[] split =replace.split("±img=");
        String[] split1 = split[1].split("±");
        String[] split2 = split1[1].split("±");
        StringBuilder stringBuilder = new StringBuilder();
       stringBuilder.append(MakeNekoPicture.MakePicture(split1[0])).append("\n").append(split2[0]);
        return stringBuilder.toString();
    }
}

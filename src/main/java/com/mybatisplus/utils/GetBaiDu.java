package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GetBaiDu {

    public String getChaXun(String text)throws Exception{
        //https://wenxin110.top/api/sg_encyclopedia?text=python&type=json
       // HashMap<String, String> map = new HashMap<>();
        Connection.Response execute = Jsoup.connect("http://apii.gq/api/baike.php?msg="+text)
                .postDataCharset("UTF-8")
                .ignoreContentType(true)
                .execute();
        String body = execute.body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject data = (JSONObject)jsonObject.get("data");
        String Msg = data.getString("Msg");
        String info = data.getString("info");
        String image = data.getString("image");
        String url = data.getString("url");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("您搜索的是:").append(Msg).append("\n")
                .append("查询内容是: ").append(info).append("\n")
                .append(MakeNeko.MakePicture(image)).append("\n")
                .append("查询的来源是: ").append(url);

        return stringBuilder.toString();
    }

    public HashMap getBaiDu(String text)throws Exception{
        //https://wenxin110.top/api/sg_encyclopedia?text=python&type=json
        HashMap<String, String> map = new HashMap<>();
        Connection.Response execute = Jsoup.connect("https://wenxin110.top/api/sg_encyclopedia?text="+text+"&type=json")
                .postDataCharset("UTF-8")
                .ignoreContentType(true)
                .execute();
        String body = execute.body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        String content1 = jsonObject.getString("content");
        String img = jsonObject.getString("img");
        String title = jsonObject.getString("title");
        map.put("title", title);
        map.put("img",img);
        map.put("content",content1);
        return map;
    }
}

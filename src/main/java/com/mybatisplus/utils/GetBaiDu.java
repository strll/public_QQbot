package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GetBaiDu {
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

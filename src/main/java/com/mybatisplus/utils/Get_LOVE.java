package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Get_LOVE {
    @Value("${tianxingkey}")
    String key;
    public String getQinghua(){
        String body="";
        try {
            Connection.Response response = Jsoup.connect("http://api.tianapi.com/saylove/index?key="+key)
                    //api地址 http://www.xiaoqiandtianyi.tk/doc/today.php
                    //.cookies(cookies)
                    .postDataCharset("UTF-8")
                    .ignoreContentType(true)
                    .execute();
            body = response.body(); // 获取html原始文本内容
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JSONObject jsonObject = JSONObject.parseObject(body);
       return jsonObject.getJSONArray("newslist").getJSONObject(0).get("content").toString();

    }
}

package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class HistoryTody {
    public String historytody(){
        String body="";
        try {
            Connection.Response response = Jsoup.connect("https://api.xiao-xin.top/API/today.php")
                    //.cookies(cookies)
                    .postDataCharset("UTF-8")
                    .ignoreContentType(true)
                    .execute();
            body = response.body(); // 获取html原始文本内容
            System.out.println(body);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray results =  jsonObject.getJSONArray("list");
        return   results.toJSONString();
    }
}

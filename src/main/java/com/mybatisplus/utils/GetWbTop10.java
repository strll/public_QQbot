package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GetWbTop10 {
    @Value("${tianxingkey}")
    String key;

    public String getWbTop10(){
        String body="";
        try {
            Connection.Response response = Jsoup.connect("http://api.tianapi.com/weibohot/index?key="+key)
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
        JSONArray newslist = jsonObject.getJSONArray("newslist");

        StringBuilder sb=new StringBuilder();
        sb.append("热搜的标签可能为空 end");
        for (int i = 0; i < newslist.size(); i++) {
            JSONObject json =(JSONObject) newslist.get(i);
            String hotword = json.getString("hotword");
            String hotwordnum = json.getString("hotwordnum");
            String hottag = json.getString("hottag");
            sb.append("热搜话题:\n").append(hotword).append("\n")
                    .append("热搜指数:\n").append(hotwordnum).append("\n")
                    .append("热搜标签:").append(hottag)
                    .append("end");
        }
        return String.valueOf(sb);
    }
}

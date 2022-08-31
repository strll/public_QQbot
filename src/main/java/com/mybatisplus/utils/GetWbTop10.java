package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class GetWbTop10 {
    public String getWbTop10(){
        String body="";
        try {
            Connection.Response response = Jsoup.connect("https://api.xiao-xin.top/API/wb_hot.php")
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
        String top = jsonObject.getString("Top");
        String top_1 = jsonObject.getString("Top_1");
        String top_2 = jsonObject.getString("Top_2");
        String top_3 = jsonObject.getString("Top_3");
        String top_4 = jsonObject.getString("Top_4");
        String top_5 = jsonObject.getString("Top_5");
        String top_6 = jsonObject.getString("Top_6");
        String top_7 = jsonObject.getString("Top_7");
        String top_8 = jsonObject.getString("Top_8");
        String top_9 = jsonObject.getString("Top_9");
        String top_10 = jsonObject.getString("Top_10");
        StringBuilder sb=new StringBuilder();
        sb.append("top:").append(top).append("\n")
                .append("top1:").append(top_1).append("\n")
                .append("top2:").append(top_2).append("\n")
                .append("top3:").append(top_3).append("\n")
                .append("top4:").append(top_4).append("\n")
                .append("top5:").append(top_5).append("\n")
                .append("top6:").append(top_6).append("\n")
                .append("top7:").append(top_7).append("\n")
                .append("top8:").append(top_8).append("\n")
                .append("top9:").append(top_9).append("\n")
                .append("top10:").append(top_10);

        return String.valueOf(sb);
    }
}

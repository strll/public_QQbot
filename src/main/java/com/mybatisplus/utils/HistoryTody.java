package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HistoryTody {
    @Value("${tianxingkey}")
    private String key;
    public String historytody(){
        String body="";
        try {
            Connection.Response response = Jsoup.connect("http://api.tianapi.com/lishi/index?key="+key)
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
        JSONArray newslist = jsonObject.getJSONArray("newslist");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < newslist.size(); i++) {

            JSONObject o =( JSONObject) newslist.get(i);
            String title = o.getString("title");
            String lsdate = o.getString("lsdate");
        stringBuffer.append("时间:").append(lsdate).append("\n")
                .append("历史事件: ").append(title).append("end");
        }
        return    stringBuffer.toString();
    }
}

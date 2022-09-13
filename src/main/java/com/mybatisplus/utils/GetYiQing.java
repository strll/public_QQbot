package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class GetYiQing {

    public String getyiqing(String city) throws IOException {
            String url="https://api.gt5.cc/api/yq?msg="+city.replace(" ","")+"&type=json";
        Connection.Response yq = Jsoup.connect(url)
                .postDataCharset("UTF-8")
                .ignoreContentType(true)
                .execute();
        String yqstring = yq.body();

        JSONObject yqJson = JSONObject.parseObject(yqstring);
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("查询地区: ").append( yqJson.getString("dq")).append("\n")
                .append("目前确诊: ").append( yqJson.getString("mqqz")).append("\n")
                .append("目前死亡: ").append( yqJson.getString("mqsw")).append("\n")
                .append("目前治愈: ").append( yqJson.getString("mqzy")).append("\n")
                .append("更新时间: ").append( yqJson.getString("gxsj")).append("\n")
                .append("数据来源: ").append( yqJson.getString("sjly")).append("\n");
        return stringBuffer.toString();
    }
}

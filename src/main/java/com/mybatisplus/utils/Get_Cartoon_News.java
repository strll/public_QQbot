package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Get_Cartoon_News {
    @Value("${tianxingkey}")
    private String key;
    public String getNews() throws IOException {
        Connection.Response execute = Jsoup.connect("http://api.tianapi.com/dongman/index?key="+key+"&num=10")
                .postDataCharset("UTF-8")
                .ignoreContentType(true)
                .execute();
        String body = execute.body();
        JSONObject parse = JSONObject.parseObject(body);
        JSONArray newslist = parse.getJSONArray("newslist");
        StringBuffer stringBuffer = new StringBuffer();
        //newslist.size()
        for (int i = 0; i < newslist.size(); i++) {
            JSONObject o =(JSONObject) newslist.get(i);
            stringBuffer.append("时间: ").append(o.getString("ctime")).append("\n")
                    .append("新闻标题: ").append( o.getString("title")).append("\n")
                    .append("新闻内容:\n").append(o.getString("description")).append("\n")
                    .append("新闻来源:").append(o.getString("source")).append("\n")
                    .append("新闻详情:").append(o.getString("url")).append("\n")
               //     .append(MakeNeko.MakePicture(o.getString("picUrl")))
                   .append("end")
            ;
        }
        return  stringBuffer.toString();
    }
}

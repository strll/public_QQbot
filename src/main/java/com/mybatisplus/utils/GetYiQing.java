package com.mybatisplus.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GetYiQing {
    public String getyiqing(String city) throws IOException {
        Document document = Jsoup.connect("http://www.xiaoqiandtianyi.tk/api/yq.php?msg="+city).get();
        Elements body = document.select("body");
        String text = body.text();
        String s = text.replaceAll("\uD83C\uDF3E", "\n");
        return s.substring(1);
    }
}

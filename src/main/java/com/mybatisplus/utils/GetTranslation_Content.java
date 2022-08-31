package com.mybatisplus.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GetTranslation_Content {

    public String getTranslation_content(String translation) throws IOException {
        Document document = Jsoup.connect("http://ovoa.cc/api/ydfy.php?msg="+translation+"&type=text").get();
        Elements body = document.select("body");
        String text = body.text();
        String s = text.replaceAll("翻译:", "\n翻译:");
        return s;
    }

}

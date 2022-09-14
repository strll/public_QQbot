package com.mybatisplus.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class Get_Talk {
    //http://apii.gq/api/xiaoai.php?msg=hello

    public String get_talk(String msg) throws IOException {
        Document document = Jsoup.connect("http://apii.gq/api/xiaoai.php?msg="+msg)
                .get();
        Elements body = document.select("body");
        return body.text().replace("小爱","nana") ;

    }
}

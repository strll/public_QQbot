package com.mybatisplus.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Get_Chick_Dictionary {
    public String Get_Chick (String string) throws IOException {
        String URL = "https://jikipedia.com/search?phrase="+string;
        Document document = null;

            document = Jsoup.connect(URL)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .header("Token","fa5088821efeb05ff06bd30c7e06e07b213dca7b7af2ecf619347c583ec9dc4b")
                    .header("User-Agent"," Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36")
                    .get();

        Elements select = document.select("#__layout > div:nth-child(1) .search #search .feed-container .card-middle.cursor.card-middle-instance-");
        Element element = select.get(1);
        Element child = element.child(1);
        String href = child.attr("href");
        // https://jikipedia.com/definition/299871941
        Document document1 = Jsoup.connect(href).get();
        Elements select1 =   document1.select("#__layout .master > .main .definition .section.card-middle .content .brax-render.render ");
        //   Elements select1 = document.select("#__layout");
        StringBuilder stringBuilder = new StringBuilder();
        for (Element element1 : select1) {
            String replace =  element1.text().replace("‌‌", "");
            stringBuilder.append(replace);
        }
        return stringBuilder.toString();
    }
}

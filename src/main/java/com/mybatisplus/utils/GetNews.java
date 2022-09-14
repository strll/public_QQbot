package com.mybatisplus.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class GetNews {

    public String EveryDayNews() throws IOException {


        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readValue(new URL("https://www.zhihu.com/api/v4/columns/c_1261258401923026944/items"), JsonNode.class);
        String contentHtml = jsonNode.get("data").get(0).get("content").asText();
     //   System.out.println(contentHtml);
        Document parse = Jsoup.parse(contentHtml);
        StringBuilder result = new StringBuilder();
        Elements allElements = parse.getAllElements();
        for (Element element : allElements) {
            if("img".equals(element.tagName())){
                String url = element.attr("src");
               result.append("[CAT:image,file=").append(url).append("]\n");
            }else if("p".equals(element.tagName())) {
                String content = element.text().trim();
                result.append(content).append("\n");
            }
        }


        return result.toString();
    }
}

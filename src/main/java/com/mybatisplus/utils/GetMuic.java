package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
public class GetMuic {

    public String getMuic(String SongName){
        String URL = "https://music.cyrilstudio.top/search?keywords="+SongName;
        Document document = null;
        try {
            document = Jsoup.connect(URL).ignoreContentType(true).
                    userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element songsInfo = document.selectFirst("body");
        String songsInfoString = songsInfo.ownText();
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(songsInfoString);
        com.alibaba.fastjson.JSONObject result = (com.alibaba.fastjson.JSONObject)jsonObject.get("result");
        JSONArray songs = (JSONArray) result.get("songs");
        com.alibaba.fastjson.JSONObject song = (com.alibaba.fastjson.JSONObject)songs.get(0);
        String id = song.get("id").toString();
        String songName = song.get("name").toString();
        String artistsName = "";

        JSONArray artists = (JSONArray) song.get("artists");
        int size = artists.size();
        for(int i = 0;i<size;i++){
            com.alibaba.fastjson.JSONObject jsonObject1 = (com.alibaba.fastjson.JSONObject) artists.get(i);
            artistsName = artistsName + jsonObject1.get("name").toString()+" ";
        }

        String musicUrl ="https://music.163.com/song?id="+id;

        HashMap<String, String> cookie = convertCookie("NMTID=00O72vD_HhfGl67cEo3leuohqmjYvAAAAGC2RZ5XQ; _ntes_nnid=5859e74baef4f2a268c0a17a2c96a4b7,1661499699306; _ntes_nuid=5859e74baef4f2a268c0a17a2c96a4b7; WEVNSM=1.0.0; WNMCID=lchqpv.1661499700022.01.0; WM_NI=YnH%2BPxjoSz6R2SS3G6qvzYyqaYJfN3BZBQ9WTK1dLWQbS45%2BF5JSo%2Fj9K2A%2F9Zp1kS4X%2FGVaI5egyKn%2FZ%2BI0M0BXEfu4yP7qKpvrzPKlgr1mebwYtit%2FXkleRguYDUHLMzU%3D; WM_NIKE=9ca17ae2e6ffcda170e2e6eebac946f8b885b1f625e9bc8eb7d15e928a9eb0c449a7949cb3f753b7949d96ca2af0fea7c3b92a9bb288ccd374aab19d9bb55af28d9dd4c63eb3aab6bafc7ba3878687cd3982868eb2bc5d9895ff85cf5d93be9dd5b82186eb86b4ee25aae785d3c425fb86c0d2ea59f888e1a8d56b8bebab88fc41aef196cccc3c8ff08eb9c86eb7ecb7b1b67b89b0ba93f95c89b4a993b467a7beb9baf763b8f1988cef6f92b9bbbac75f8c94839bd837e2a3; WM_TID=qwXGjtQua6dFQAUQVQKEDLKWB5ZuPXre; JSESSIONID-WYYY=fnf8ouejDI%2Br1Gg1lBRnJf3hrHvgDum1uXgdVRTnJPy%2FWbD6o7c%5Cn8XJl%5CJ%2B22QsMuVD937jsAg2GjsWdvTzoP2arQ%5C6qNcvezrSaMjvOJt5%2B4CUM%2Fm2TMQtszdbgCyH%5Cy3cmocZgbVgtMRWsdEIGGvEW8%2Fibt7C3ZREy4hHk%5Cedne5n%3A1661502696899; _iuqxldmzr_=33; __snaker__id=LOvGdeq4KG29DD0v; gdxidpyhxdE=h0T%5CRifZYy6b6l7UEAQxQ3JXSTnDTGWXSlMeqA%2FfWNmXb579EYrf%5CQZ%2Bh2gA6f56ZmBgY3HG%2Ff3ospAKHS8WxYr6D%5CUW62NLyZBLOOntqG5kGIenJl%2FYSEn5DrB60THXAeM5%2F3deKDA%5CuuOHEo3al%2FD0NspTeGlm0iSliLSg4D1jvINO%3A1661501797511; _9755xjdesxxd_=32; YD00000558929251%3AWM_NI=vvO%2BKeAwFHyB026YZyfBa5GT5U6BZUS9b78i66Z7DYe6Mau0CttGcN2forE1A0FdLrnXmX4e1cjh5BJR9eivn%2F0ZPU16ArOgmZw642d7wFihsI4lEqTCWm2WmSO1%2FQOyTDg%3D; YD00000558929251%3AWM_NIKE=9ca17ae2e6ffcda170e2e6ee97e95b8e9cfca9f57fa89e8fb6c55f969b9b82d454a7bb98b5e7258da7baaab62af0fea7c3b92ab6aa81ccca6f83a98aa9fc4af4eefabad973a69eafd6c660f78bfaa3d95f91e7aed8bb63f6ea96d8c753a7ea8382f83c81acbca8e246f2baa193c83b83f08a85cf7aae88a3b9b76ff3bda689b84a8a8ef786ec7eaef100bbd35ff3ea8f93ce6daf86b9d7b73aa8e9b9a7ec64b197fc8dc87b989d998eb15daf8d81d2ae5b8da79ca5e637e2a3; YD00000558929251%3AWM_TID=rWQG637dP0dARUVBQALBDPKGHJ7O2%2BY%2F; __csrf=2d325ca8c524bb0b3577be171c24bdae; MUSIC_U=f3c1f102aa252b2481dbfa6c2a31e2227e7e569afc5c2bb055ee810ff4c82306993166e004087dd355f72f9717b7913afb8a4369d6d1d38623a645fefe1a99a0641db99340580f72a0d2166338885bd7");
        Document document1 = null;
        try {
            document1 = Jsoup.connect(musicUrl).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36").referrer("https://music.163.com/").cookies(cookie).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements element = document1.select("img.j-img");
        String src = element.attr("src");

        String resultInfo ="[CAT:music," +
                "kind=neteaseCloud," +
                "musicUrl=http://music.163.com/song/media/outer/url?id="+id+","+
                "title="+songName+"," +
                "jumpUrl=https://music.163.com/#/song?id="+id+"," +
                "pictureUrl="+src+"," +
                "summary="+artistsName+"]";
        return resultInfo;
    };

    private HashMap<String, String> convertCookie(String s) {
        String[] split = s.split(";");
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        for (String s1 : split) {
            String[] split1 = s1.split("=");
            stringStringHashMap.put(split1[0],split1[1]);


        }
        return stringStringHashMap;
    }

}

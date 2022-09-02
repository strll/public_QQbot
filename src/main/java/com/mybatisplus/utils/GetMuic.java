package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
public class GetMuic {
    @Value("${music_cookie}")
    String music_cookie;

    @Value("${userAgent}")
    String userAgent;


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

        HashMap<String, String> cookie = convertCookie(music_cookie);
        Document document1 = null;
        try {
            document1 = Jsoup.connect(musicUrl).ignoreContentType(true)
                    .userAgent(userAgent)
                    .referrer("https://music.163.com/").cookies(cookie).get();
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

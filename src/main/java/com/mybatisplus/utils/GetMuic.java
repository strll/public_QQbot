package com.mybatisplus.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
    @Value("${music_key}")
    String musicKey;




    public String getMuic(String SongName) throws IOException {
      //  String URL = "https://music.cyrilstudio.top/search?keywords="+SongName;
// 使用的是https://admin.alapi.cn/account/center 这个网站的接口
        String URL = "https://v2.alapi.cn/api/music/search?keyword="+SongName+"&token="+musicKey;
        Document document = Jsoup.connect(URL).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").get();
        Element songsInfo = document.selectFirst("body");
        String songsInfoString = songsInfo.ownText();
        JSONObject jsonObject = JSON.parseObject(songsInfoString);
        JSONObject data = (JSONObject)jsonObject.get("data");
        JSONArray songs = (JSONArray) data.get("songs");
        JSONObject song = (JSONObject)songs.get(0);
        String id = song.get("id").toString();
        String songName = song.get("name").toString();
        String artistsName = "";

        JSONArray artists = (JSONArray) song.get("artists");
        int size = artists.size();
        for(int i = 0;i<size;i++){
            JSONObject jsonObject1 = (JSONObject) artists.get(i);
            artistsName = artistsName + jsonObject1.get("name").toString()+" ";
        }

        String musicUrl ="https://music.163.com/song?id="+id;

        HashMap<String, String> cookie = convertCookie("NTES_P_UTID=fitikPGJiDgAUDYpYv51IAQU3jWtC99i|1660528401; P_INFO=ms1878784348@163.com|1660528401|1|mail163|00&99|jis&1659936255&gbox_lushi#jis&320100#10#0#0|&0||ms1878784348@163.com; nts_mail_user=ms1878784348@163.com:-1:1; _ntes_nuid=a4f4d9d9e45e5981d5f9b0f740e05b42; _ntes_nnid=a4f4d9d9e45e5981d5f9b0f740e05b42,1660528402907; NMTID=00OkaQQ07odzx9iX0HUrW3_xRND6NkAAAGCnzSuyQ; WEVNSM=1.0.0; WNMCID=qcwxlt.1660528403182.01.0; WM_TID=PkvgCsBnpKNBEBVUUQKUHZKvWzWeFVlr; WM_NI=QiufH00ICVaC9HUaI+PD6xDt5Glz1Cm0tiQfADJn0jutDvo1960N8L5yN/1pxXUtMUAKfvVpOwZxWWxy02ifVfiouAO2kXYo7bybbCMb5gSBIWeXdzN2rcwwEePd6P8oaUY=; WM_NIKE=9ca17ae2e6ffcda170e2e6ee9ad26f83eb83aff83bb2b08aa3c85f839f9bb0c1508bb6878dcd68eda69fd7aa2af0fea7c3b92a93b7bb87cb3cf4978db7e449fb989b82cc21949cfa99c780f799a49bb449aff0bc8ad46e839c868fb362b7ac8c97ee3bfbea00afbb48f7a782d0b47cbc889b95d05f8395aa97d57c8da99699d65dbca700d3ec6e96baa790f84b8deea5b9dc65f5beb783e1498fbefb88d24a8b89abb9e7728eeebe84f67d87bba085b55cfcb1afb6dc37e2a3; playerid=91609630; NTES_PASSPORT=6cVO6KmV7YzukmPFB4usjYnSw17TI.qt4r1ECAx5q.VdtMe.tG1Vzr4i2owxVyx9OXRW3tkvcnCibczcq42jVgNn4iFJHYJktIbab7tAeWcuy6c9PvooDlBxqcPHrFHmk25MdgOYFtNoCaaUJ3i1KTHIukzWNaCxyO.Hp8vywCMGyZKqcMud_qguO34dXDg5Z; S_INFO=1660745105|1|0&60##|ms1878784348; NTES_SESS=na.0pEWLMaROiR3EnkUhG9sjmEjoc3Eg.qxC6kCDpffJpvFRpWZ9_iVOEsoI9jIQAY4UPkbrrS9jAMI329OV4r5248EUYqTEkNfI.oX2DuhwYPyllaEViredTaJdSOjw0hccf7O5dK8c91lwdM77In16slTXh6e8OeRff_e1Eu3yDcCikPlS_hO5s2DqCZFoOyndN9wro_lEdXofi_hZEHh.s; ANTICSRF=b5dc0866e37e5f0b7fd2241741e4c397; JSESSIONID-WYYY=C\\YmVXJmiPFpog0cFshkztk5eRP5k7sltG+n80ed0YXUbYtgUX3VDaB9OAEZfgTiOtHhk+vr0\\i7jMxMNflN6R3Hd1IMVdrSVXnMUOdX8ZCzmNOj7ucda\\Zah+AJM2K9+y1WKhcHKXdMgiD4ux++/Y61YnJjZfurJOfv7x6Iu8kMu7pP:1660747837146; _iuqxldmzr_=33; MUSIC_U=82946624f323895edb05a889a6e357706e09d742c72ebdd21637c881e9e3dbe8993166e004087dd348020914b6f94ace4a57812c76aadaba635e6ef9ff450dcd39f8a1812d31fc3da0d2166338885bd7; __csrf=60766d25aa3b7d0420d07c3b952d3d35; ntes_kaola_ad=1");
        Document document1 = Jsoup.connect(musicUrl).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").referrer("https://music.163.com/").cookies(cookie).get();
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


//        Document document = null;
//        try {
//            document = Jsoup.connect(URL).ignoreContentType(true).
//                    userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Element songsInfo = document.selectFirst("body");
//        String songsInfoString = songsInfo.ownText();
//        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(songsInfoString);
//        com.alibaba.fastjson.JSONObject result = (com.alibaba.fastjson.JSONObject)jsonObject.get("result");
//        JSONArray songs = (JSONArray) result.get("songs");
//        com.alibaba.fastjson.JSONObject song = (com.alibaba.fastjson.JSONObject)songs.get(0);
//        String id = song.get("id").toString();
//        String songName = song.get("name").toString();
//        String artistsName = "";
//
//        JSONArray artists = (JSONArray) song.get("artists");
//        int size = artists.size();
//        for(int i = 0;i<size;i++){
//            com.alibaba.fastjson.JSONObject jsonObject1 = (com.alibaba.fastjson.JSONObject) artists.get(i);
//            artistsName = artistsName + jsonObject1.get("name").toString()+" ";
//        }
//
//        String musicUrl ="https://music.163.com/song?id="+id;
//
//        HashMap<String, String> cookie = convertCookie(music_cookie);
//        Document document1 = null;
//        try {
//            document1 = Jsoup.connect(musicUrl).ignoreContentType(true)
//                    .userAgent(userAgent)
//                    .referrer("https://music.163.com/").cookies(cookie).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Elements element = document1.select("img.j-img");
//        String src = element.attr("src");
//
//        String resultInfo ="[CAT:music," +
//                "kind=neteaseCloud," +
//                "musicUrl=http://music.163.com/song/media/outer/url?id="+id+","+
//                "title="+songName+"," +
//                "jumpUrl=https://music.163.com/#/song?id="+id+"," +
//                "pictureUrl="+src+"," +
//                "summary="+artistsName+"]";
//        return resultInfo;
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

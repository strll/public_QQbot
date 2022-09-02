package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;



@Component
public class GetNowWeather {

    @Value("${key}")
    String key;

    public String GetWeather(String city) throws IOException {
        Connection.Response execute = Jsoup.connect("https://geoapi.qweather.com/v2/city/lookup?&key="+key+"&location="+city)
                .postDataCharset("UTF-8")
                .ignoreContentType(true)
                .execute();
        String body = execute.body();

        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray location = jsonObject.getJSONArray("location");
        JSONObject o = (JSONObject)location.get(0);
        String lat = o.getString("lat");
        String lon = o.getString("lon");
        System.out.println(lat+"="+lon);
        Connection.Response weather = Jsoup.connect("https://devapi.qweather.com/v7/weather/now?key="+key+"&location="+lon+","+lat+"&lang=zh")
                .postDataCharset("UTF-8")
                .ignoreContentType(true)
                .execute();
        String stringweater = weather.body();
        ObjectMapper mapper = new ObjectMapper();
        JSONObject weatherJson = JSONObject.parseObject(stringweater);
        JSONObject now = weatherJson.getJSONObject("now");

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("温度: ").append(now.getString("temp")).append("\n")
                .append("体感温度: ").append( now.getString("feelsLike")).append("\n")
                .append("观测时间: ").append(now.getString("obsTime")).append("\n")
                .append("天气状况: ").append(now.getString("text")).append("\n")
                .append("风向角度: ").append(now.getString("wind360")).append("\n")
                .append("风向: ").append( now.getString("windDir")).append("\n")
                .append("风力等级: ").append(now.getString("windScale")).append("\n")
                .append("风速: ").append(now.getString("windSpeed")).append("\n")
                .append("相对湿度: ").append(now.getString("humidity")).append("\n")
                .append("当前小时累计降水量: ").append(now.getString("precip")).append("毫米\n")
                .append("大气压: ").append( now.getString("pressure")).append("\n")
                .append("能见度: ").append(now.getString("vis")).append("\n");
        System.out.println(stringBuffer);
        return stringBuffer.toString();
    }
}

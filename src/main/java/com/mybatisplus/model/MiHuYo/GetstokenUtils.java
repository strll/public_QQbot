package com.mybatisplus.model.MiHuYo;



import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;


import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GetstokenUtils {

    static String cookie = "";

    private GetstokenUtils() {
    }


    public static void main(String[] args) {
        log.info(String.valueOf(doGen(cookie)));
    }

    public static String doGen(String cookie) {
        String stoken;
        Map<String, String> headers = getCookieHeader(cookie);
        String url = String.format(MiHoYoConfig.HUB_COOKIE2_URL, headers.get("login_ticket"), headers.get("login_uid"));
        MiHoYoAbstractSign helperHeader = new MiHoYoAbstractSign() {
            @Override
            public Object sign() {
                return null;
            }
        };
        JSONObject result = HttpUtils.doGet(url, helperHeader.getHeaders());
        log.info(String.valueOf(result));
        if (!"OK".equals(result.get("message"))) {
            stoken = "login_ticket已失效,请重新登录获取";
        } else {
            stoken = (String) result.getJSONObject("data").getJSONArray("list").getJSONObject(0).get("token");
        }
        return stoken;
    }

    public static Map<String, String> getCookieHeader(String cookie) {
        String[] split = cookie.split(";");
        Map<String, String> map = new HashMap<>();
        for (String s : split) {
            String h = s.trim();
            String[] item = h.split("=");
            map.put(item[0], item[1]);
        }
        return map;
    }

}


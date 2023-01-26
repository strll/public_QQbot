package com.mybatisplus.utils.Qiniuyun.util;

import com.google.gson.Gson;
import com.mybatisplus.utils.Qiniuyun.RequestPojo.Data;
import com.mybatisplus.utils.Qiniuyun.RequestPojo.Params;
import com.mybatisplus.utils.Qiniuyun.RequestPojo.Request_Qiniuyun_JsonRootBean;
import com.qiniu.util.Auth;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.List;

@Log4j2
@Deprecated
public class getSign {
    public static String getToken(String text,String accessKey,String secretKey) {
        //基础参数拼接
        String url = "http://ai.qiniuapi.com/v3/image/censor";
        String host = "ai.qiniuapi.com";
 //       String body = "{ \"data\": { \"uri\": \""+imageUrl+"\" }, \"params\": { \"scenes\": [ \"pulp\", \"terror\", \"politician\" ] } }";
        String contentType = "application/json";
        String method = "POST";

        Request_Qiniuyun_JsonRootBean request = new Request_Qiniuyun_JsonRootBean();
        Data data = new Data();
        data.setText(text);
        request.setData(data);
        Params params = new Params();
        List<String> strings = new LinkedList();
        strings.add("antispam");
        params.setScenes(strings);
        request.setParams(params);

        Gson gson = new Gson();
        String s = gson.toJson(request);

        Auth auth = Auth.create(accessKey, secretKey);
        String qiniuToken = "Qiniu " + auth.signRequestV2(url, method, s.getBytes(), contentType);
      //  log.info("url={},body={},qiniuToken={}",url,s,qiniuToken);
        return  qiniuToken;
    }
}

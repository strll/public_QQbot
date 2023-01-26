package com.mybatisplus.utils.Qiniuyun.util;

import com.google.gson.Gson;
import com.mybatisplus.utils.Qiniuyun.ResponsePojo.Resopnse_Qiniuyun_JsonRootBean;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
@Service
public class QiNuYun_util {




    @Value("${qi.AccessKey}")
    private  String ACCESS_KEY;
    @Value("${qi.SecretKey}")
    private  String SECRET_KEY;
    

    
    private  Client client = new Client();


    public Resopnse_Qiniuyun_JsonRootBean TextCensor(String text) throws QiniuException {
        // 构造post请求body
        Gson gson = new Gson();

        Map<String, Object> uri = new HashMap<>();
        uri.put("text", text);

        Map<String, Object> scenes = new HashMap<>();
        //pulp 黄  terror 恐  politician 敏感人物
        String[] types = {"antispam"};
        scenes.put("scenes", types);

        Map params = new HashMap();
        params.put("data", uri);
        params.put("params", scenes);

        String paraR = gson.toJson(params);
        byte[] bodyByte = new byte[0];
        bodyByte = paraR.getBytes(StandardCharsets.UTF_8);

        // 接口请求地址//http://ai.qiniuapi.com/v3/text/censor
        String url = "http://ai.qiniuapi.com/v3/text/censor";

        String post = post(url, bodyByte);
        Gson gson1 = new Gson();
        Resopnse_Qiniuyun_JsonRootBean person = gson.fromJson(post, Resopnse_Qiniuyun_JsonRootBean.class);

        return person;
    }


    //参考api文档 https://developer.qiniu.com/dora/manual/4252/image-review
    //图片审核
    public Resopnse_Qiniuyun_JsonRootBean ImageCensor(String url) throws QiniuException {
        // 构造post请求body
        Gson gson = new Gson();

        Map<String, Object> uri = new HashMap<>();
        uri.put("uri", url);

        Map<String, Object> scenes = new HashMap<>();
        //pulp 黄  terror 恐  politician 敏感人物
        String[] types = {"pulp", "terror", "politician", "ads"};
        scenes.put("scenes", types);

        Map params = new HashMap();
        params.put("data", uri);
        params.put("params", scenes);

        String paraR = gson.toJson(params);
        byte[] bodyByte = new byte[0];
        bodyByte = paraR.getBytes(StandardCharsets.UTF_8);

        // 接口请求地址
        String url1 = "http://ai.qiniuapi.com/v3/image/censor";

        String post = post(url1, bodyByte);

        Gson gson1 = new Gson();
        Resopnse_Qiniuyun_JsonRootBean person = gson.fromJson(post, Resopnse_Qiniuyun_JsonRootBean.class);

        return person;
    }

    //参考api文档 https://developer.qiniu.com/dora/manual/4258/video-pulp
    //视频审核
    public Resopnse_Qiniuyun_JsonRootBean VideoCensor(String url) throws QiniuException {
        // 构造post请求body
        Gson gson = new Gson();

        Map bodyData = new HashMap();

        Map<String, Object> uri = new HashMap<>();
        uri.put("uri", url);

        Map<String, Object> params = new HashMap<>();

        Map<String, Object> scenes = new HashMap<>();
        //pulp 黄  terror 恐  politician 敏感人物
        String[] types = {"pulp", "terror", "politician"};

        Map<String, Object> cut_param = new HashMap<>();
        cut_param.put("interval_msecs", 500);

        params.put("scenes", types);
        params.put("cut_param", cut_param);

        bodyData.put("data", uri);
        bodyData.put("params", params);
        String paraR = gson.toJson(bodyData);
        byte[] bodyByte = paraR.getBytes();

        // 接口请求地址
        String url1 = "http://ai.qiniuapi.com/v3/video/censor";
        String post = post(url1, bodyByte);
        Gson gson1 = new Gson();
        Resopnse_Qiniuyun_JsonRootBean person = gson.fromJson(post, Resopnse_Qiniuyun_JsonRootBean.class);

        return  person;
    }

    /**
     * 查询视频审核内容结果
     * 参考
     * https://developer.qiniu.com/censor/api/5620/video-censor#4
     * @param ID : 视频审核返回的 job ID
     *
     */
    public String getVideoCensorResultByJobID(String ID){
          Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String url = "http://ai.qiniuapi.com/v3/jobs/video/".concat(ID);
        String accessToken = (String) auth.authorizationV2(url).get("Authorization");
        StringMap headers = new StringMap();
        headers.put("Authorization", accessToken);

        try {
            com.qiniu.http.Response resp = client.get(url,headers);
            return resp.bodyString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String post(String url, byte[] body) throws QiniuException {
          Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        com.qiniu.http.Response resp = client.post(url, body, auth.authorizationV2(url, "POST", body, "application/json"), Client.JsonMime);
        return resp.bodyString();
    }



}

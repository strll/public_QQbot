package com.mybatisplus.controller;

import love.forte.simbot.annotation.Async;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.stereotype.Controller;

@Controller
public class GroupPictureController {


    @Async
    @OnGroup
    @Filter(value="nana图片",trim=true,matchType = MatchType.CONTAINS)
    public void send_picture(GroupMsg groupMsg, Sender sender) {
    synchronized(this){
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet("https://www.dmoe.cc/random.php");
        try {

            //将HttpContext对象作为参数传给execute()方法,则HttpClient会把请求响应交互过程中的状态信息存储在HttpContext中
            HttpResponse response = httpClient.execute(httpGet, httpContext);
            //获取重定向之后的主机地址信息,即"http://127.0.0.1:8088"
            HttpHost targetHost = (HttpHost)httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            //获取实际的请求对象的URI,即重定向之后的"/blog/admin/login.jsp"
            HttpUriRequest realRequest = (HttpUriRequest)httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
            HttpEntity entity = response.getEntity();
            String url = "[CAT:image,file=" + targetHost+realRequest.getURI() + "]";
            sender.sendGroupMsg(groupMsg, url);

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            httpClient.getConnectionManager().shutdown();
        }
    }

    }
}

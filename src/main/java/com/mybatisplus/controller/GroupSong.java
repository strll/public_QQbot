package com.mybatisplus.controller;

import com.alibaba.fastjson.JSONArray;

import com.mybatisplus.utils.GetMuic;
import love.forte.simbot.PriorityConstant;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.listener.ContinuousSessionScopeContext;
import love.forte.simbot.listener.ListenerContext;
import love.forte.simbot.listener.SessionCallback;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

@Controller
public class GroupSong {

    private static final String AREA1_GROUP = "Area1q2sda";
    private static final String AREA2_GROUP = "Area2qsx";

    /**
     * 点歌功能
     * */

    @OnGroup
    @Filter(value = "nana听歌", trim=true,matchType = MatchType.CONTAINS)
    @Async
    public void listenSong1(GroupMsg msg, MsgSender sender) throws IOException {
        String text = msg.getText().substring(6);
        String muic = getMuic.getMuic(text);
        sender.SENDER.sendGroupMsg(msg,muic);
    }


    @Autowired
    private GetMuic getMuic;
    @OnGroup
    @Filter(value = "nana点歌", trim=true,matchType = MatchType.CONTAINS)
    @Async
    public void listenSong(GroupMsg msg, ListenerContext context, MsgSender sender) throws IOException {

        ContinuousSessionScopeContext sessionContext = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert sessionContext != null;

        String accountCode = msg.getAccountInfo().getAccountCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode;

        SessionCallback<String> callback = SessionCallback.<String>builder().onResume(text -> {
            sender.SENDER.sendGroupMsg(msg, "请输入你要听的歌曲的名字");
            sessionContext.waiting(AREA2_GROUP, key, text2 -> {
                String SongName = (String) text2;
                String muic = null;
                try {
                    muic = getMuic.getMuic(SongName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sender.SENDER.sendGroupMsg(msg,muic);
            });

        }).onError(e -> {
            // 这里是第一个会话，此处通过 onError 来处理出现了异常的情况，例如超时
            if (e instanceof TimeoutException) {
                // 超时事件是 waiting的第三个参数，可以省略，默认下，超时时间为 1分钟
                // 这个默认的超时时间可以通过配置 simbot.core.continuousSession.defaultTimeout 进行指定。如果小于等于0，则没有超时，但是不推荐不设置超时。
                System.out.println("onError: 超时啦: " + e);
            } else {
                System.out.println("onError: 出错啦: " + e);
            }
        }).onCancel(e -> {
            // 这里是第一个会话，此处通过 onCancel 来处理会话被手动关闭、超时关闭的情况的处理，有些时候会与 orError 同时被触发（例如超时的时候）
            System.out.println("onCancel 关闭啦: " + e);
        }).build(); // build 构建

        // 这里开始等待第一个会话。
        sessionContext.waiting(AREA1_GROUP, key, callback);
    }



    /**
     * 针对上述第一个会话的监听。
     * 通过 @OnlySession来限制，当且仅当由 AREA1_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnGroup
    @OnlySession(group = AREA1_GROUP)
    public void listenArea1(GroupMsg msg, ListenerContext context){
        // 得到session上下文。
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;
        String accountCode = msg.getAccountInfo().getAccountCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode;
        String text = msg.getText();
        // 尝试将这个phone推送给对应的会话。
        session.push(AREA1_GROUP, key, text);
        /*
            注意！如果你的会话结果逻辑比较复杂，那么你应该先判断这个key对应的会话是否存在，然后再进行推送。
            session.push 在没有发现对应的会话的情况下，会返回false。
         */

    }


    @OnGroup
    @OnlySession(group = AREA2_GROUP)
    public void listenArea2(GroupMsg  msg, ListenerContext context){
        // 得到session上下文。
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;
        String accountCode = msg.getAccountInfo().getAccountCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode;
        String text = msg.getText();
        // 尝试将这个phone推送给对应的会话。
        session.push(AREA2_GROUP, key, text);
    }


}

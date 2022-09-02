package com.mybatisplus.controller;

import com.mybatisplus.utils.GetBaiDu;
import com.mybatisplus.utils.MakeNekoPicture;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class GroupBaiDu {
    @Autowired
    private GetBaiDu getBaiDu;
    @Autowired
    private MessageContentBuilderFactory factory;

    @OnGroup
    @Filter(value = "nana查询", trim=true,matchType = MatchType.CONTAINS)
    public void listenSong1(GroupMsg msg, MsgSender sender) throws IOException {
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        String text = msg.getText().substring(6);
        String yiqing="";
        HashMap baiDu=null;
        try {
            baiDu = getBaiDu.getBaiDu(text);
        }catch (Exception e){
            sender.SENDER.sendGroupMsg(msg,"查询失败");
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append((String)baiDu.get("title")).append("\n").append((String) baiDu.get("content")).append("\n"
        );
        String img = (String) baiDu.get("img");
        if(!img.equals("无"))
        {
             stringBuffer.append(MakeNekoPicture.MakePicture((String) baiDu.get("img")));
       }

        builder.forwardMessage(forwardBuilder -> {
            forwardBuilder.add(msg.getBotInfo(), String.valueOf(stringBuffer));
        });
        final MiraiMessageContent messageContent = builder.build();
        sender.SENDER.sendGroupMsg(msg,messageContent);
    }

    @OnGroup
    @Filter(value = "nana百度", trim=true,matchType = MatchType.CONTAINS)
    public void listenSong12(GroupMsg msg, MsgSender sender) throws IOException {
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        String text = msg.getText().substring(6);
        String yiqing="";
       String baiDu=null;
        try {
            baiDu = getBaiDu.getChaXun(text);
        }catch (Exception e){
            sender.SENDER.sendGroupMsg(msg,"查询百度失败");
        }
        sender.SENDER.sendGroupMsg(msg, baiDu);
    }


}

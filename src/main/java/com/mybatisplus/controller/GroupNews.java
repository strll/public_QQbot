package com.mybatisplus.controller;

import com.mybatisplus.entity.Group_And_Sender;
import com.mybatisplus.utils.GetNews;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.listener.ContinuousSessionScopeContext;
import love.forte.simbot.listener.ListenerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class GroupNews {
    @Autowired
    private MessageContentBuilderFactory factory;
    @Autowired
    private GetNews getNews;
    @Async
    @OnGroup
    @Filter(value="nana每日新闻",trim=true,matchType = MatchType.CONTAINS)
    public void sendNews(GroupMsg msg, Sender sender) throws IOException {
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        String s= getNews.EveryDayNews();
            GroupMsg finalGroup = msg;
        String[] split = s.split("；");
        builder.forwardMessage(forwardBuilder -> {
            for (String s1 : split) {
                forwardBuilder.add(finalGroup.getBotInfo(), s1 );
            }
        });
        final MiraiMessageContent messageContent = builder.build();
        // 发送消息
        sender.sendGroupMsg(msg, messageContent);

 //      sender.sendGroupMsg(msg, s);
    }

}

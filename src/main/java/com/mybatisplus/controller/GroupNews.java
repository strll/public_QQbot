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
    @OnGroup
    @Filter(value="nana每日新闻",trim=true,matchType = MatchType.CONTAINS)
    @Async
    public void sendNews(GroupMsg msg, MsgSender sender) throws IOException {
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        String s= getNews.EveryDayNews();

            String finalS = s;
            GroupMsg finalGroup = msg;
            builder.forwardMessage(forwardBuilder -> {
                forwardBuilder.add(finalGroup.getBotInfo(), finalS);
            });
            final MiraiMessageContent messageContent = builder.build();

            // 发送消息
            sender.SENDER.sendGroupMsg(msg, messageContent);


    }
}

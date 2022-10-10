package com.mybatisplus.controller;

import com.mybatisplus.entity.Today_Eat;
import com.mybatisplus.utils.Get_Cartoon_News;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class Group_Cartoon_News {
    @Autowired
    private Get_Cartoon_News cartoon_news;
    @Autowired
    private MessageContentBuilderFactory factory;
    @OnGroup
    @Filter(value = "nana动漫资讯", trim = true, matchType = MatchType.CONTAINS)
    public void sendnews(GroupMsg msg, Sender sender) throws IOException {
        String news = cartoon_news.getNews();
        String[] ends = news.split("end");
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        builder.forwardMessage(forwardBuilder -> {
            for (String end :ends) {
                forwardBuilder.add(msg.getBotInfo(),  end);
            }
        });
        final MiraiMessageContent messageContent = builder.build();
        sender.sendGroupMsg(msg,messageContent);
    }
}

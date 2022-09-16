package com.mybatisplus.controller;

import com.mybatisplus.utils.GetWbTop10;
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

@Controller
public class GroupWbTop10 {
    @Autowired
    private GetWbTop10 getWbTop10;
    @Autowired
    private MessageContentBuilderFactory factory;
    @OnGroup
    @Filter(value="nana微博热搜",trim=true,matchType = MatchType.CONTAINS)
    @Async
    public void sendNews(GroupMsg msg, MsgSender sender) throws IOException {
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        String wbTop10 = getWbTop10.getWbTop10();
        String[] ends = wbTop10.split("end");
        GroupMsg finalGroup =  msg;
        builder.forwardMessage(forwardBuilder -> {
            for (String end : ends) {
                forwardBuilder.add(finalGroup.getBotInfo(), end);
            }

        });
        final MiraiMessageContent messageContent = builder.build();

        // 发送消息
        sender.SENDER.sendGroupMsg(msg, messageContent);


    }

}

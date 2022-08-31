package com.mybatisplus.controller;

import com.mybatisplus.utils.GetNews;
import com.mybatisplus.utils.HistoryTody;
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
public class GroupHistoryTody {
    @Autowired
    private MessageContentBuilderFactory factory;

    @Autowired
    private HistoryTody historyTody;

    @OnGroup
    @Filter(value="nana历史上的今天",trim=true,matchType = MatchType.CONTAINS)
    @Async
    public void sendNews(GroupMsg msg, MsgSender sender) throws IOException {
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        String historytody = historyTody.historytody();
        String finalS = historytody;
        String replace = finalS.replaceAll("\\\\", "").replaceAll(",", "").replace("[", "").replace("]", "").replaceAll("\"","");

        String[] split = replace.split("n");
        StringBuilder sbuilder = new StringBuilder();

        GroupMsg finalGroup =  msg;
        builder.forwardMessage(forwardBuilder -> {
            for (String s : split) {
                sbuilder.append(s).append("\n");
            }

        });


        final MiraiMessageContent messageContent = builder.build();

        // 发送消息
        sender.SENDER.sendGroupMsg(msg, messageContent);


    }
}

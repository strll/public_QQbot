package com.mybatisplus.controller;

import com.mybatisplus.utils.GetMoYu;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class GroupMoYu {
@Autowired
private GetMoYu moyu;
    @OnGroup
    @Filter(value="nana摸鱼日历",trim=true,matchType = MatchType.CONTAINS)
    @Async
    public void sendmoyu(GroupMsg msg, Sender sender) throws IOException {
        sender.sendGroupMsg(msg,moyu.getMoyu());
    }
}

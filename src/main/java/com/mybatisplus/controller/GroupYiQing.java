package com.mybatisplus.controller;

import com.mybatisplus.utils.GetYiQing;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class GroupYiQing {
    @Autowired
    private GetYiQing getYiQing;

    @OnGroup
    @Filter(value = "nana疫情", trim=true,matchType = MatchType.CONTAINS)
    @Async
    public void listenSong1(GroupMsg msg, MsgSender sender) throws IOException {
        String text = msg.getText().substring(6);
        String yiqing="";
        try {
            yiqing = getYiQing.getyiqing(text);
        }catch (Exception e){
            sender.SENDER.sendGroupMsg(msg,"疫情查询失败");
        }
        sender.SENDER.sendGroupMsg(msg,yiqing);
    }


}

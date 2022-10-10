package com.mybatisplus.controller;

import com.mybatisplus.utils.Get_Chick_Dictionary;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class Group_chick_dictionary {
    @Autowired
    private Get_Chick_Dictionary chick_dictionary;
    @OnGroup
    @Filter(value = "nana小鸡词典", trim=true,matchType = MatchType.CONTAINS)
    public void listenSong12(GroupMsg msg, MsgSender sender) throws IOException {

        if (msg.getText().length() <= 8) {
            sender.SENDER.sendGroupMsg(msg, "请输入要查询的内容 示例: nana小鸡词典 边角料");
        } else {
            String text = msg.getText().substring(8);
            String yiqing = "";
            String baiDu = null;
            try {
                baiDu = chick_dictionary.Get_Chick(text);
            } catch (Exception e) {
                sender.SENDER.sendGroupMsg(msg, "查询词典过于频繁 请稍后查询");
            }
            sender.SENDER.sendGroupMsg(msg, baiDu);
        }

    }
}

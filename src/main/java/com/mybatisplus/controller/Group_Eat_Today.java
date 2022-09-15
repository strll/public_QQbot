package com.mybatisplus.controller;

import catcode.Neko;
import com.mybatisplus.entity.Today_Eat;
import com.mybatisplus.service.TodayEatService;
import com.mybatisplus.utils.MakeNekoPicture;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@Controller
public class Group_Eat_Today {
    @Autowired
    private TodayEatService todayEatService;
    @Autowired
    private MessageContentBuilderFactory factory;

    @OnGroup
    @Filter(value = "nana今天吃什么", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void sendeat(GroupMsg msg, Sender sender) throws IOException {
        Today_Eat today_eat = todayEatService.Send_Today_Eat_Message();
        String qq = today_eat.getQq();
        String message = today_eat.getMessage();
        String s="要不试试"+qq+"推荐的:\n"+message;
        sender.sendGroupMsg(msg,s);
    }
    @OnGroup
    @Filter(value = "学习今天吃什么", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void studyeat(GroupMsg msg, Sender sender) throws IOException {
        Today_Eat eat = new Today_Eat();
        String text = msg.getText().substring(7);
        MessageContent msgContent = msg.getMsgContent();
        String s="";
        List<Neko> image = msgContent.getCats("image");
        if (image.size() != 0) {
            for (Neko neko : image) {
                String url = neko.get("url");
                s = s+MakeNekoPicture.MakePicture(url);
            }
        }
        String qq = msg.getAccountInfo().getAccountCode();
        eat.setMessage(text+"\n"+s);
        eat.setQq(qq);
        int i = todayEatService.Studay_Today_Eat_Message(eat);
        if(i!=0){
            sender.sendGroupMsg(msg,"存储 今天吃什么 成功");
        }else{
            sender.sendGroupMsg(msg,"存储 今天吃什么 失败");
        }

    }
    @OnGroup
    @Filter(value = "删除今天吃什么", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void deleteeat(GroupMsg msg, Sender sender) throws IOException {
        String text = msg.getText().substring(8);
        int i=0;
        try{
             i = todayEatService.Delete_Today_Eat_Message(Integer.parseInt(text));
        }catch (Exception e){
            sender.sendGroupMsg(msg,"出现错误 请输入 删除今天吃什么 要删除的内容的id");
        }
        if(i!=0){
            sender.sendGroupMsg(msg,"删除成功");
        }else {
            sender.sendGroupMsg(msg,"删除失败");
        }

    }
    @OnGroup
    @Filter(value = "查看所有今天吃什么", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void seleectAlleat(GroupMsg msg, Sender sender) throws IOException {
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        List<Today_Eat> today_eats = todayEatService.Send_All_message();
        builder.forwardMessage(forwardBuilder -> {
            for (Today_Eat today_eat : today_eats) {
                String s="该条信息的ID是:\n"+today_eat.getId()+"\n信息内容是:\n"+today_eat.getMessage()+"存储人是:\n"+today_eat.getQq();
                forwardBuilder.add(msg.getBotInfo(), s);
            }
        });
        final MiraiMessageContent messageContent = builder.build();
        sender.sendGroupMsg(msg,messageContent);
    }
}
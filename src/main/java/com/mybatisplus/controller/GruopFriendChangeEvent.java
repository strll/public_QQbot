package com.mybatisplus.controller;

import love.forte.simbot.annotation.OnGroupMemberIncrease;
import love.forte.simbot.annotation.OnGroupMemberReduce;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.api.message.events.GroupMemberIncrease;
import love.forte.simbot.api.message.events.GroupMemberReduce;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import org.springframework.stereotype.Component;

@Component("GroupFriendChangeEvent")
public class GruopFriendChangeEvent {
    @OnGroupMemberIncrease
    public void groupFriendAddEvent(GroupMemberIncrease groupMemberIncrease, MsgSender sender){

        String accountNickname = groupMemberIncrease.getBeOperatorInfo().getAccountNickname();
        String accountCode = groupMemberIncrease.getBeOperatorInfo().getAccountCode();
        sender.SENDER.sendGroupMsg(groupMemberIncrease, "欢迎进群:"+"[CAT:at,code="+accountCode+"]"+"("+accountCode+")"+"\n 我是本群机器人nana 输入nana帮助可以看到我的功能");
           }

   // @OnGroupMemberReduce
    public void groupFriendReduceEvent(GroupMemberReduce roupMemberReduce , MsgSender sender){

        String accountNickname = roupMemberReduce.getBeOperatorInfo().getAccountNickname();
        String accountCode = roupMemberReduce.getBeOperatorInfo().getAccountCode();
        sender.SENDER.sendGroupMsg(roupMemberReduce,"emmm.. \n"+accountNickname+"(QQ号是"+ accountCode+")"+"离开了我们 \n ");
    }

}

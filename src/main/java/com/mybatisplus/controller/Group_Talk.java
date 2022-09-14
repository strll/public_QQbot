package com.mybatisplus.controller;

import catcode.CatCodeUtil;
import com.mybatisplus.entity.Group_And_Sender;
import com.mybatisplus.entity.Group_And_Sender_All;
import com.mybatisplus.entity.Message;
import com.mybatisplus.service.IAdminService;
import com.mybatisplus.utils.Get_Talk;
import love.forte.simbot.annotation.Async;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
@Controller
public class Group_Talk {
    private HashSet<Group_And_Sender_All> hashset=new HashSet();
    @Autowired
    private Get_Talk getTalk;
    @Autowired
    private IAdminService adminService;

    private volatile boolean send_flag=true; //回复模块启动标志

    @OnGroup
    @Filter(value="nana开启聊天模块",trim=true,matchType = MatchType.CONTAINS)
    public void open(GroupMsg groupMsg, Sender sender) throws IOException {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                send_flag = true;
                sender.sendGroupMsg(groupMsg,"聊天模块已经开启");
            }

        }
    }
    @OnGroup
    @Filter(value="nana关闭聊天模块",trim=true,matchType = MatchType.CONTAINS)
    public void cl(GroupMsg  groupMsg, Sender sender) throws IOException {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                send_flag = false;
                sender.sendGroupMsg(groupMsg,"聊天模块已经关闭");
            }
        }
    }

    @org.springframework.scheduling.annotation.Async
    @OnGroup
    @Filter(value="nana聊天",trim=true,matchType = MatchType.CONTAINS)
    public void sendNews(GroupMsg msg, Sender sender) throws IOException {
        if (send_flag) {
            AccountInfo accountInfo = msg.getAccountInfo();
            String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号

            Group_And_Sender_All group_and_sender = new Group_And_Sender_All(msg, sender);
            boolean add = hashset.add(group_and_sender);
            if (add) {
                sender.sendGroupMsg(msg, "您已进入nana的聊天模式,如果想要退出请输入 nana退出聊天");
            } else {
                sender.sendGroupMsg(msg, "进入聊天模式失败");
            }
        }

    }
    //==================================
    @org.springframework.scheduling.annotation.Async
    @OnGroup
    @Filter(value="nana退出聊天",trim=true,matchType = MatchType.CONTAINS)
    public void removesend(GroupMsg msg, Sender sender) throws IOException {
        if (send_flag) {
            AccountInfo accountInfo = msg.getAccountInfo();
            String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
            Group_And_Sender_All group_and_sender = new Group_And_Sender_All(msg, sender);
            boolean add = hashset.remove(group_and_sender);
            if (add) {
                sender.sendGroupMsg(msg, "您已退出nana的聊天模式");
            } else {
                sender.sendGroupMsg(msg, "退出聊天模式失败");
            }
        }
    }

    @Async
    @OnGroup
    public void onGroupMsg(GroupMsg groupMsg,Sender sender) throws IOException {
        if (send_flag) {
            Group_And_Sender_All group_and_sender = new Group_And_Sender_All(groupMsg, sender);
            group_and_sender.setSender(sender);
            group_and_sender.setGroup(groupMsg);
            String accountNickname = groupMsg.getAccountInfo().getAccountNickname();
            if (hashset.contains(group_and_sender)) {

                String text = groupMsg.getText();//获取发生信息
                if (!text.equals("") && text != null) {
                    String talk = getTalk.get_talk(text);
                    sender.sendGroupMsg(groupMsg, talk);
                }
            }
        }
    }
}

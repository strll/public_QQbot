package com.mybatisplus.controller;

import catcode.Neko;
import com.mybatisplus.entity.Message;
import com.mybatisplus.service.IMessageService;
import love.forte.simbot.annotation.Async;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
public class GroupGetKeyByMessage {
    @Autowired
    private IMessageService service;
    private volatile ArrayList<String> userArray = new ArrayList<String>();
    private volatile boolean flag = false;


    @Async
    @OnGroup
    @Filter(value = "nana查询关键词", trim = true, matchType = MatchType.CONTAINS)
    public void GetKeyByMessage_before(GroupMsg groupMsg, Sender sender) {
        synchronized (this) {
            AccountInfo accountInfo = groupMsg.getAccountInfo();
            String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
            userArray.add(accountCode);
            sender.sendGroupMsg(groupMsg, "请输入nana的自动回复内容");
            flag = true;
        }
    }


    @Async
    @OnGroup
    public void GetKeyByMessage_after(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        boolean b = userArray.contains(accountCode);
        if (b && flag) {
            synchronized (this) {
                flag = false;
                String text = groupMsg.getText();
                Message message = new Message();
                MessageContent msgContent = groupMsg.getMsgContent();
                List<Neko> image = msgContent.getCats("image");

                String url = "";
                if (image.size() != 0) {
                    Neko neko = image.get(0);
                    url = neko.get("url");
                    message.setUrl(url);
                    List<Message> messages = service.Get_Key_by_Url(message);
                    HashSet<String> set = new HashSet<>();
                    for (Message message1 : messages) {
                        set.add(message1.getUrl());
                    }
                    ArrayList<String> strings = new ArrayList<>(set);
                    StringBuffer stringBuffer = new StringBuffer();
                    for (String object : strings) {
                        stringBuffer.append(object).append("\n");
                    }
                    sender.sendGroupMsg(groupMsg, "查询结果如下\n"+stringBuffer);
                } else {
                    if (url.equals("")) {
                        try {
                            String value = text;

                            message.setValuemessage(value);
                            List<Message> messages = service.Get_Key_by_Message(message);
                            HashSet<String> set = new HashSet<>();
                            for (Message message1 : messages) {
                                set.add(message1.getKeymessage());
                            }
                            ArrayList<String> strings = new ArrayList<>(set);
                            StringBuffer stringBuffer = new StringBuffer();
                            for (String object : strings) {
                                stringBuffer.append(object).append("\n");
                            }
                            sender.sendGroupMsg(groupMsg, "查询结果如下\n"+stringBuffer+"由于QQ每次发送的图片的url地址不一样所以会出现查询不到的情况这是正常现象");


                        } catch (ArrayIndexOutOfBoundsException e) {
                            if (message.getValuemessage().equals("") && message.getUrl().equals("")) {
                                sender.sendGroupMsg(groupMsg, "未检测到查询内容");
                            }

                        }

                    }


                }


            }

        }
    }
}




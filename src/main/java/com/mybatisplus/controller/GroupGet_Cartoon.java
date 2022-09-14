package com.mybatisplus.controller;

import catcode.Neko;
import com.mybatisplus.entity.Message;
import com.mybatisplus.utils.Get_Cartoon_By_Picture;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnlySession;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.listener.ContinuousSessionScopeContext;
import love.forte.simbot.listener.ListenerContext;
import love.forte.simbot.listener.SessionCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
@Controller
public class GroupGet_Cartoon {

    @Autowired
    private Get_Cartoon_By_Picture get_cartoon_by_picture;
    private static final String AREA1_GROUP = "getpicture1";
    private static final String AREA2_GROUP = "getpicture2";
    @OnGroup
    @Filter(value = "nana找番", trim = true, matchType = MatchType.CONTAINS)
    public void find(GroupMsg msg, ListenerContext context, Sender sender) throws Exception {
        ContinuousSessionScopeContext sessionContext = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert sessionContext != null;
        String accountCode = msg.getAccountInfo().getAccountCode();
        String groupCode = msg.getGroupInfo().getGroupCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode + "-" + groupCode + "study";
        // 发送提示信息

        MessageContent msgContent = msg.getMsgContent();
        List<Neko> image = msgContent.getCats("image");
        if (image.size() != 0) {
            Neko neko = image.get(0);
            String url = neko.get("url");
            String cartoon="";

            cartoon = get_cartoon_by_picture.getCartoon(url);
            sender.sendGroupMsg(msg,"所找到的番剧不一定准确,望谅解");
            sender.sendGroupMsg(msg,cartoon);

    }


    }
}

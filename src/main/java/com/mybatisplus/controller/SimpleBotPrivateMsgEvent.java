package com.mybatisplus.controller;

import catcode.CatCodeUtil;
import catcode.CodeTemplate;
import catcode.Neko;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.Getter;
import love.forte.simbot.api.sender.MsgSender;

import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageUtil;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.constant.PriorityConstant;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.listener.ContinuousSessionScopeContext;
import love.forte.simbot.listener.ListenerContext;
import love.forte.simbot.listener.SessionCallback;
import love.forte.simbot.message.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author SanJiu
 */
//@Controller
public class SimpleBotPrivateMsgEvent {
    private static final String AREA1_GROUP = "Area1";
    private static final String AREA2_GROUP = "Area2";
    private static final String AREA3_GROUP = "Area3";
    private static final String AREA4_GROUP = "Area4";



    @OnPrivate
    public void doPrivateMsg(PrivateMsg msg, MsgSender sender, Getter getter) {
        if (msg.getAccountInfo().getAccountCode().equals("1878784348")) {
            System.out.println("===============");
            System.out.println(msg.getMsg());
            System.out.println("===============");

            sender.SENDER.sendPrivateMsg(msg,msg.getMsgContent());
        }

    }

    @Autowired
    private MessageContentBuilderFactory factory;
    @OnPrivate
    @Filter(value = "测试合并信息", matchType = MatchType.STARTS_WITH)
    public void lis(PrivateMsg msg, Sender sender) {
        if (!(factory instanceof MiraiMessageContentBuilderFactory)) {
            throw new RuntimeException("不支持mirai组件");
        }
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();

        // 通过 MiraiMessageContentBuilder.forwardMessage 构建一个合并消息。
        // 一般来讲，合并消息不支持与其他类型消息同时存在，因此不应再继续拼接其他消息。
        builder.forwardMessage(forwardBuilder -> {
            forwardBuilder.add(msg.getBotInfo(),"你好");
            forwardBuilder.add(msg.getBotInfo(),"你好");
            forwardBuilder.add(msg.getBotInfo(),"你好");
        });

        final MiraiMessageContent messageContent = builder.build();

        // 发送消息
        sender.sendPrivateMsg(msg, messageContent);
    }





    @OnPrivate
    public void testPoke(PrivateMsg msg, MsgSender sender, Getter getter) {
        if (msg.getAccountInfo().getAccountCode().equals("1878784348")) {
            MessageContent msgContent = msg.getMsgContent();
            List<Neko> cats = msgContent.getCats();
            if(cats!=null){
                Neko neko = cats.get(0);
                String botCode = msg.getBotInfo().getBotCode();
                if(neko!=null&&neko.getType().equals("nudge")&&neko.get("target").equals(botCode)){
                    System.out.println("接收到戳一戳拉");
                }
            }
        }

    }

    @OnPrivate
    @Filter(value = "持续对话作用域测试", matchType = MatchType.STARTS_WITH)
    @ListenBreak
    public void testConversationDomain(PrivateMsg msg, ListenerContext context, Sender sender) {
        ContinuousSessionScopeContext sessionContext = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert sessionContext != null;

        String accountCode = msg.getAccountInfo().getAccountCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode;

          /*
            注意！你应当考虑一个问题：同一个人同时触发多次同样的会话，比如它同时发了两次 'start'.
            这个问题的解决应当由你自行解决，比如创建会话之前，先去检查是否已经存在。
         */

        // 发送提示信息
        sender.sendPrivateMsg(msg, "测试开始");

        // 创建回调函数 SessionCallback 实例。
        // 通过 SessionCallback.builder 进行创建
        SessionCallback<String> callback = SessionCallback.<String>builder().onResume(text -> {
            // 得到手机号，进行下一步操作
            sender.sendPrivateMsg(msg, "你输入的信息为:" + text);
            sender.sendPrivateMsg(msg, "请继续输入:");

            // 这是在回调中继续创建一个会话。
            // 这里通过 sessionContext.waiting(group, key, OnResume) 快速创建一个回调，只处理正确结果的情况，而不处理其他（出现错误、关闭事件等）
            // wait, 这里使用的是 name_group，也就是等待提供姓名的group，但是key还是那个人对应唯一的key
            sessionContext.waiting(AREA2_GROUP, key, text2 -> {
                sender.sendPrivateMsg(msg, "你输入的信息为:" + text2);
                sender.sendPrivateMsg(msg, "请继续输入:");

                sessionContext.waiting(AREA3_GROUP, key, text3 -> {
                    sender.sendPrivateMsg(msg, "你输入的信息为:" + text3);
                    sender.sendPrivateMsg(msg, "请继续输入:");

                    sessionContext.waiting(AREA3_GROUP, key, text4 -> {
                        sender.sendPrivateMsg(msg, "你输入的信息为:" + text4);
                        sender.sendPrivateMsg(msg, "测试结束");
                    });

                });

            });


        }).onError(e -> {
            // 这里是第一个会话，此处通过 onError 来处理出现了异常的情况，例如超时
            if (e instanceof TimeoutException) {
                // 超时事件是 waiting的第三个参数，可以省略，默认下，超时时间为 1分钟
                // 这个默认的超时时间可以通过配置 simbot.core.continuousSession.defaultTimeout 进行指定。如果小于等于0，则没有超时，但是不推荐不设置超时。
                System.out.println("onError: 超时啦: " + e);
            } else {
                System.out.println("onError: 出错啦: " + e);
            }
        }).onCancel(e -> {
            // 这里是第一个会话，此处通过 onCancel 来处理会话被手动关闭、超时关闭的情况的处理，有些时候会与 orError 同时被触发（例如超时的时候）
            System.out.println("onCancel 关闭啦: " + e);
        }).build(); // build 构建

        // 这里开始等待第一个会话。
        sessionContext.waiting(AREA1_GROUP, key, callback);

    }

    /**
     * 针对上述第一个会话的监听。
     * 通过 @OnlySession来限制，当且仅当由 AREA1_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnPrivate
    @OnlySession(group = AREA1_GROUP)
    public void listenArea1(PrivateMsg msg, ListenerContext context){
        // 得到session上下文。
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;

        String accountCode = msg.getAccountInfo().getAccountCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode;

        String text = msg.getText();

        // 尝试将这个phone推送给对应的会话。
        session.push(AREA1_GROUP, key, text);

        /*
            注意！如果你的会话结果逻辑比较复杂，那么你应该先判断这个key对应的会话是否存在，然后再进行推送。
            session.push 在没有发现对应的会话的情况下，会返回false。
         */

    }
    /**
     * 针对上述第二个会话的监听。
     * 通过 @OnlySession来限制，当且仅当由 AREA2_GROUP 这个组的会话的时候，此监听函数才会生效。
     */

    @OnPrivate
    @OnlySession(group = AREA2_GROUP)
    public void listenArea2(PrivateMsg msg, ListenerContext context){
        // 得到session上下文。
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;

        String accountCode = msg.getAccountInfo().getAccountCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode;

        String text = msg.getText();

        // 尝试将这个phone推送给对应的会话。
        session.push(AREA2_GROUP, key, text);
    }

    /**
     * 针对上述第三个会话的监听。
     * 通过 @OnlySession来限制，当且仅当由 AREA3_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnPrivate
    @OnlySession(group = AREA3_GROUP)
    public void listenArea3(PrivateMsg msg, ListenerContext context){
        // 得到session上下文。
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;

        String accountCode = msg.getAccountInfo().getAccountCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode;

        String text = msg.getText();

        // 尝试将这个phone推送给对应的会话。
        session.push(AREA3_GROUP, key, text);
    }

    /**
     * 针对上述第四个会话的监听。
     * 通过 @OnlySession来限制，当且仅当由 AREA4_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnPrivate
    @OnlySession(group = AREA4_GROUP)
    public void listenArea4(PrivateMsg msg, ListenerContext context){
        // 得到session上下文。
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;

        String accountCode = msg.getAccountInfo().getAccountCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode;

        String text = msg.getText();

        // 尝试将这个phone推送给对应的会话。
        session.push(AREA4_GROUP, key, text);
    }

}

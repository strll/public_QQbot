package com.mybatisplus.controller;

import catcode.Neko;
import com.mybatisplus.entity.Message;
import com.mybatisplus.service.IAdminService;
import com.mybatisplus.service.IMessageService;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.listener.ContinuousSessionScopeContext;
import love.forte.simbot.listener.ListenerContext;
import love.forte.simbot.listener.SessionCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component("GroupDeleteSessionAreaEvent")
public class GroupDeleteSessionAreaEvent {
    private static final String AREA1_GROUP = "Area1";
    private static final String AREA2_GROUP = "Area2";
    private static final String AREA3_GROUP = "Area3";
    private static final String AREA4_GROUP = "Area4";


    @Autowired
    private IMessageService service;

    Message message = null;
    HashMap<String, Message> hashMap=new HashMap<>();

    private volatile boolean Delete_flag=true; //删除模块启动标志
    @Autowired
    private IAdminService adminService;


    @Async
    @OnGroup
    @Filter(value="nana关闭删除模块",trim=true,matchType = MatchType.CONTAINS)
    public void stopStudy(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                Delete_flag = false;
                sender.sendGroupMsg(groupMsg,"删除模块已经关闭");
            }

        }
    }
    @Async
    @OnGroup
    @Filter(value="nana启动删除模块",trim=true,matchType = MatchType.CONTAINS)
    public void startStudy(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                Delete_flag = true;
                sender.sendGroupMsg(groupMsg,"删除模块已经开启");
            }

        }
    }


    @OnGroup
    @Filter(value = "nana删除", matchType = MatchType.STARTS_WITH)
    @ListenBreak
    public void testConversationDomain(GroupMsg msg, ListenerContext context, Sender sender) {


        if(  Delete_flag ) {


            ContinuousSessionScopeContext sessionContext = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
            assert sessionContext != null;

            String accountCode = msg.getAccountInfo().getAccountCode();
            String groupCode = msg.getGroupInfo().getGroupCode();
            // 通过账号拼接一个此人在此群中的唯一key
            String key = accountCode + "-" + groupCode;
            hashMap.put(key,new Message());

            // 发送提示信息
            sender.sendGroupMsg(msg, "请问您要删除的关键词是什么?");

            SessionCallback<String> callback = SessionCallback.<String>builder().onResume(text -> {

                sender.sendGroupMsg(msg, text);


            }).onError(e -> {
                // 这里是第一个会话，此处通过 onError 来处理出现了异常的情况，例如超时
                if (e instanceof TimeoutException) {
                    // 超时事件是 waiting的第三个参数，可以省略，默认下，超时时间为 1分钟
                    // 这个默认的超时时间可以通过配置 simbot.core.continuousSession.defaultTimeout 进行指定。如果小于等于0，则没有超时，但是不推荐不设置超时。
                    System.out.println("onError: 超时啦: " + e);
                    sender.sendGroupMsg(msg, "超时啦");
                } else {
                    System.out.println("onError: 出错啦: " + e);
                    sender.sendGroupMsg(msg, "出错啦");
                }
            }).onCancel(e -> {
                // 这里是第一个会话，此处通过 onCancel 来处理会话被手动关闭、超时关闭的情况的处理，有些时候会与 orError 同时被触发（例如超时的时候）
                System.out.println("onCancel 关闭啦: " + e);
            }).build(); // build 构建

            // 这里开始等待第一个会话。
            sessionContext.waiting(AREA1_GROUP, key, callback);
        }else{
            sender.sendGroupMsg(msg, "删除模块没有开启");
        }

    }

    /**
     * 针对上述第一个会话的监听。
     * 通过 @OnlySession来限制，当且仅当由 AREA1_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnGroup
    @OnlySession(group = AREA1_GROUP)
    public void listenArea1(GroupMsg msg, ListenerContext context){
        // 得到session上下文。
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;

        String accountCode = msg.getAccountInfo().getAccountCode();
        String groupCode = msg.getGroupInfo().getGroupCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode + "-" + groupCode;


        String text = msg.getText();

        Message message1 = hashMap.get(key);
        if(message1!=null){
       // message1 = new  Message();
        message1.setKeymessage(text);

        List<String> strings = service.Get_QQ_by_key(text); //根据要删除的内容返回这个key所有的qq号

        int size = strings.size();
        if(size!=0) {
            for (String string : strings) {
                //如果这个qq号和触发这个函数的QQ号相同并且权限满足
              //  if (string.contains(accountCode) || Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) < 2) {
                if(true){
                      int i = 0;
                      int b = 0;
                    i = service.DeleteMessage(text);

               //     b = service.DeleteMessage_By_QQ(text, accountCode);
                    if (b+i == 0) {
                        session.push(AREA1_GROUP, key, "删除失败");


                    } else {
                        session.push(AREA1_GROUP, key, "删除成功");
                    }
                    break;
                } else {
                    session.push(AREA1_GROUP, key, "您的权限不足 只能管理员或者本人才能删除");
                }
            }
            hashMap.remove(key);
        }else{
            session.push(AREA1_GROUP, key, "在数据库中未检索到删除的key");
            hashMap.remove(key);
        }
    }
    }
    /**
     * 针对上述第二个会话的监听。
     * 通过 @OnlySession来限制，当且仅当由 AREA2_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnGroup
    @OnlySession(group = AREA2_GROUP)
    public void listenArea2(GroupMsg msg, ListenerContext context){
        // 得到session上下文。
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;

        String accountCode = msg.getAccountInfo().getAccountCode();
        String groupCode = msg.getGroupInfo().getGroupCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode + "-" + groupCode;

        String text = msg.getText();

        MessageContent msgContent = msg.getMsgContent();
        List<Neko> image = msgContent.getCats("image");
        String url="";
        if(image.size()!=0){
            Neko neko = image.get(0);
            url = neko.get("url");
            message.setUrl(url);
        }

        if(url.equals("")){
            try{
                String value = text;

                message.setValuemessage(value);

            }catch(ArrayIndexOutOfBoundsException e){
                if(message.getValuemessage().equals("")&&message.getUrl().equals("")){
                    session.push(AREA2_GROUP, key, "0");

                    message=null;
                }
            }
        }
        message.setValuemessage(text);
        message.setQq(accountCode);
        int study = service.InsertMessage(message);
        session.push(AREA2_GROUP, key, study);
        message=null;

    }

    /**
     * 针对上述第三个会话的监听。
     * 通过 @OnlySession来限制，当且仅当由 AREA3_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnGroup
    @OnlySession(group = AREA3_GROUP)
    public void listenArea3(GroupMsg msg, ListenerContext context){
        // 得到session上下文。
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;

        String accountCode = msg.getAccountInfo().getAccountCode();
        String groupCode = msg.getGroupInfo().getGroupCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode + "-" + groupCode;

        String text = msg.getText();

        session.push(AREA3_GROUP, key, text);


    }

    /**
     * 针对上述第四个会话的监听。
     * 通过 @OnlySession来限制，当且仅当由 AREA4_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnGroup
    @OnlySession(group = AREA4_GROUP)
    public void listenArea4(GroupMsg msg, ListenerContext context){
        // 得到session上下文。
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;

        String accountCode = msg.getAccountInfo().getAccountCode();
        String groupCode = msg.getGroupInfo().getGroupCode();
        // 通过账号拼接一个此人在此群中的唯一key
        String key = accountCode + "-" + groupCode;

        String text = msg.getText();

        session.push(AREA4_GROUP, key, text);

    }


}

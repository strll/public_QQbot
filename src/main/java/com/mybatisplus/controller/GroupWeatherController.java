package com.mybatisplus.controller;

import catcode.Neko;
import com.mybatisplus.entity.Message;
import com.mybatisplus.service.GetWeatherService;
import com.mybatisplus.service.IAdminService;
import com.mybatisplus.service.IMessageService;
import com.mybatisplus.utils.GetNowWeather;
import com.mybatisplus.utils.GetWeather;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.listener.ContinuousSessionScopeContext;
import love.forte.simbot.listener.ListenerContext;
import love.forte.simbot.listener.SessionCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

@Controller
public class GroupWeatherController {
    private static final String AREA1_GROUP = "WeatherArea1";
    Message message = null;
    @Autowired
    private GetNowWeather getNowWeather;
@Autowired
private GetWeatherService getWeatherService;

    private volatile boolean Weather_flag=true; //天气模块启动标志


    @Autowired
    private IAdminService adminService;


    @Async
    @OnGroup
    @Filter(value="nana关闭天气模块",trim=true,matchType = MatchType.CONTAINS)
    public void stopStudy(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                Weather_flag = false;
                sender.sendGroupMsg(groupMsg,"天气模块已经关闭");
            }

        }
    }
    @Async
    @OnGroup
    @Filter(value="nana启动天气模块",trim=true,matchType = MatchType.CONTAINS)
    public void startStudy(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                Weather_flag = true;
                sender.sendGroupMsg(groupMsg,"天气模块已经开启");
            }

        }
    }

    @Autowired
    private MessageContentBuilderFactory factory;

    @Autowired
    private GetWeather getWeather;
    @OnGroup
    @Filter(value = "nana天气", matchType = MatchType.STARTS_WITH)
    @ListenBreak
    public void testConversationDomain(GroupMsg msg, ListenerContext context, Sender sender) throws Exception{


        if(  Weather_flag ) {
            ContinuousSessionScopeContext sessionContext = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
            assert sessionContext != null;

            String accountCode = msg.getAccountInfo().getAccountCode();
            String groupCode = msg.getGroupInfo().getGroupCode();
            // 通过账号拼接一个此人在此群中的唯一key
            String key = accountCode + "-" + groupCode;

            // 发送提示信息
            sender.sendGroupMsg(msg, "请输入您要查询的城市:");

            SessionCallback<String> callback = SessionCallback.<String>builder().onResume(text -> {

                try {
                    sender.sendGroupMsg(msg,"城市当前天气是:"+ getNowWeather.GetWeather(text));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sender.sendGroupMsg(msg, text+"近日天气如下");

                MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
                ArrayList<HashMap<String, String>> weather = getWeather.getWeather(text);
                GroupMsg finalGroup = msg;

                builder.forwardMessage(forwardBuilder -> {
                    for (HashMap<String, String> stringStringHashMap : weather) {
                        StringBuffer re=new StringBuffer();
                        Set<String> strings = stringStringHashMap.keySet();
                           re.append("日期:").append(stringStringHashMap.get("日期"))
                                   .append("\n晚间天气:").append(stringStringHashMap.get("晚间天气"))
                                   .append("\n白天天气:").append(stringStringHashMap.get("白天天气"))
                                   .append("\n当天最高温度:").append(stringStringHashMap.get("当天最高温度"))
                                   .append("\n当天最低温度:").append(stringStringHashMap.get("当天最低温度"))
                                   .append("风力等级:").append( stringStringHashMap.get("风力等级"))
                                   .append("\n风速，单位为km/h公里每小时或mph英里每小时:").append(stringStringHashMap.get("风速，单位为km/h公里每小时或mph英里每小时"))
                                   .append("\n相对湿度，0~100，单位为百分比:").append(stringStringHashMap.get("相对湿度，0~100，单位为百分比"))
                                   .append("\n降水概率，范围0~100:").append(stringStringHashMap.get("降水概率，范围0~100"))
                                   .append("\n降水量，单位mm").append(stringStringHashMap.get("风向文字"))
                                   .append("\n风向角度，范围0~360，0为正北，90为正东，180为正南，270为正西:").append(stringStringHashMap.get("风向角度，范围0~360，0为正北，90为正东，180为正南，270为正西"));
                        forwardBuilder.add(finalGroup.getBotInfo(), String.valueOf(re));
                    }

                });
                final MiraiMessageContent messageContent = builder.build();

                // 发送消息
                sender.sendGroupMsg(msg, messageContent);


            }).onError(e -> {
                // 这里是第一个会话，此处通过 onError 来处理出现了异常的情况，例如超时
                if (e instanceof TimeoutException) {
                    // 超时事件是 waiting的第三个参数，可以省略，默认下，超时时间为 1分钟
                    // 这个默认的超时时间可以通过配置 simbot.core.continuousSession.defaultTimeout 进行指定。如果小于等于0，则没有超时，但是不推荐不设置超时。
              //     System.out.println("onError: 超时啦: " + e);
                    sender.sendGroupMsg(msg, "超时啦");
                } else {
                 //   System.out.println("onError: 出错啦: " + e);
                    sender.sendGroupMsg(msg, "出错啦");
                }
            }).onCancel(e -> {
                // 这里是第一个会话，此处通过 onCancel 来处理会话被手动关闭、超时关闭的情况的处理，有些时候会与 orError 同时被触发（例如超时的时候）
                System.out.println("onCancel 关闭啦: " + e);
            }).build(); // build 构建

            // 这里开始等待第一个会话。
            sessionContext.waiting(AREA1_GROUP, key, callback);
        }else{
            sender.sendGroupMsg(msg, "天气模块没有开启");
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
        String s = msg.getMsg();
        session.push(AREA1_GROUP, key, s);


    }


}

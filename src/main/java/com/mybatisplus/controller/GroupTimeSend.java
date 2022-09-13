package com.mybatisplus.controller;

import com.mybatisplus.entity.Group_And_Sender;
import com.mybatisplus.service.IAdminService;

import com.mybatisplus.utils.GetMoYu;
import com.mybatisplus.utils.GetNews;
import com.mybatisplus.utils.HistoryTody;
import love.forte.simbot.annotation.Async;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashSet;

@Controller
public class GroupTimeSend {
    private HashSet<Group_And_Sender> hashset=new HashSet();
    @Autowired
    private IAdminService adminService;

    @Autowired
    private MessageContentBuilderFactory factory;

    private volatile boolean ds_flag=true; //定时模块启动标志
    @Autowired
    private GetMoYu moyu;




    @org.springframework.scheduling.annotation.Async
    @OnGroup
    @Filter(value="nana添加群定时",trim=true,matchType = MatchType.CONTAINS)
    public void addtime(GroupMsg msg, Sender sender) throws IOException {
        AccountInfo accountInfo = msg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {
            Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
            boolean add = hashset.add(group_and_sender);
            if (add){
                sender.sendGroupMsg(msg,"添加成功");
            }else{
                sender.sendGroupMsg(msg,"添加失败");
            }
        }
    }
    @org.springframework.scheduling.annotation.Async
    @OnGroup
    @Filter(value="nana取消群定时",trim=true,matchType = MatchType.CONTAINS)
    public void removetime(GroupMsg msg, Sender sender) throws IOException {
        AccountInfo accountInfo = msg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {
            Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
            boolean add = hashset.remove(group_and_sender);
            if (add){
                sender.sendGroupMsg(msg,"添加成功");
            }else{
                sender.sendGroupMsg(msg,"添加失败");
            }
        }

    }

    @Async
    @OnGroup
    @Filter(value="nana关闭定时模块",trim=true,matchType = MatchType.CONTAINS)
    public void ds(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {
            synchronized (this) {
                ds_flag = false;
                sender.sendGroupMsg(groupMsg,"定时模块已经关闭");
            }

        }
    }
    @Async
    @OnGroup
    @Filter(value="nana启动定时模块",trim=true,matchType = MatchType.CONTAINS)
    public void dso(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {
            synchronized (this) {
                ds_flag = true;
                sender.sendGroupMsg(groupMsg,"定时模块已经开启");
            }

        }
    }

    @Scheduled(cron="0 30 7 * * *")
    public void send_morning(){
        if(ds_flag) {
           GroupMsg group = null;
            Sender sender = null;
            for (Group_And_Sender group_and_sender : hashset) {
                group = group_and_sender.getGroup();
                sender = group_and_sender.getSender();
                sender.sendGroupMsg(group, "早上起来 拥抱太阳! 今天大家也要好好吃饭 ");
                sender.sendGroupMsg(group, "生活是美好的 希望大家都能照顾好自己");
            }
        }
    }

    @Scheduled(cron="0 30 9 * * *")
    public void tg(){
        if(ds_flag) {
            GroupMsg group = null;
            Sender sender = null;

            for (Group_And_Sender group_and_sender : hashset) {
                group = group_and_sender.getGroup();
                sender = group_and_sender.getSender();
                sender.sendGroupMsg(group, "大家好 希望大家多喝热水 不要长时间久坐");
            }
        }
    }

    @Scheduled(cron="0 0 9 ? * MON-FRI")
    public void sb(){
        if(ds_flag) {
            GroupMsg group = null;
            Sender sender = null;

            for (Group_And_Sender group_and_sender : hashset) {
                group = group_and_sender.getGroup();
                sender = group_and_sender.getSender();
                sender.sendGroupMsg(group, "工作时间到了 希望大家在工作中都有一个好心情 改摸鱼就摸鱼吧 一定要记得放松一下自己哦");
            }
        }
    }
    @Scheduled(cron="0 0 10 ? * MON-FRI")
    public void sb1(){
        if(ds_flag) {
            GroupMsg group = null;
            Sender sender = null;

            for (Group_And_Sender group_and_sender :  hashset) {
                group = group_and_sender.getGroup();
                sender = group_and_sender.getSender();
                sender.sendGroupMsg(group, "十点啦 去接个水上个厕所 走动一下吧 适当的摸鱼可以保持一天的好心情哦");
            }
        }
    }

    @Scheduled(cron="0 0 18 ? * FRI")
    public void sb2(){
        if(ds_flag) {
            GroupMsg group = null;
            Sender sender = null;

            for (Group_And_Sender group_and_sender :  hashset) {
                group = group_and_sender.getGroup();
                sender = group_and_sender.getSender();
                sender.sendGroupMsg(group, "周五下班了 这一周工作辛苦了 周末就好好放松一下吧");
            }
        }
    }


    @Scheduled(cron="0 0 18 ? * MON-FRI")
    public void xb(){
        if(ds_flag) {
            GroupMsg group = null;
            Sender sender = null;

            for (Group_And_Sender group_and_sender :  hashset) {
                group = group_and_sender.getGroup();
                sender = group_and_sender.getSender();
                sender.sendGroupMsg(group, "下班啦下班了 今天也好好的犒劳一下自己吧");
            }
        }
    }


    @Scheduled(cron="0 0 15 * * *")
    public void tg2(){
        if(ds_flag) {
            GroupMsg group = null;
            Sender sender = null;

            for (Group_And_Sender group_and_sender :  hashset) {
                group = group_and_sender.getGroup();
                sender = group_and_sender.getSender();
                sender.sendGroupMsg(group, "希望大家不要久坐 多从位置上起来走走");
            }
        }
    }

    //{ 秒数} {分钟} {小时} {日期} {月份} {星期}
    @Scheduled(cron="0 30 19 * * * ")
    public void send_evening(){
        if(ds_flag) {
            GroupMsg group = null;
            Sender sender = null;

            for (Group_And_Sender group_and_sender :  hashset) {
                group = group_and_sender.getGroup();
                sender = group_and_sender.getSender();
                sender.sendGroupMsg(group, "晚上好 今天晚上也要好好吃饭");
                sender.sendGroupMsg(group, "生活是美好的 希望大家都能照顾好自己");
            }
        }
    }

    @Scheduled(cron="0 0 12 * * * ")
    public void send_afternoon(){
        if(ds_flag){
            GroupMsg group=null;
            Sender sender =null;

            for (Group_And_Sender group_and_sender : hashset) {
                group = group_and_sender.getGroup();
                sender = group_and_sender.getSender();
                sender.sendGroupMsg(group,"中午好 中午记得要午休哦");
                sender.sendGroupMsg(group,"生活是美好的 希望大家都能照顾好自己");
            }
        }

    }
//==============

    @Autowired
    private HistoryTody historyTody;
    @Scheduled(cron="0 0 7 * * * ")
    public void historyTody(){
        if(ds_flag) {
            for (Group_And_Sender group_and_sender :  hashset) {
                GroupMsg group = group_and_sender.getGroup();
                Sender sender = group_and_sender.getSender();


                MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
                String historytody = historyTody.historytody();
                String finalS = historytody;
                String replace = finalS.replaceAll("\\\\", "").replaceAll(",", "").replace("[", "").replace("]", "").replaceAll("\"", "");
                String[] split = replace.split("n");
                StringBuilder sbuilder = new StringBuilder();

                GroupMsg finalGroup = group;
                builder.forwardMessage(forwardBuilder -> {
                    for (String s : split) {
                        sbuilder.append(s).append("\n");
                    }
                    forwardBuilder.add(finalGroup.getBotInfo(), String.valueOf(sbuilder));

                });
                final MiraiMessageContent messageContent = builder.build();

                // 发送消息
                sender.sendGroupMsg(group, messageContent);
            }
        }

    }

    @Scheduled(cron="0 30 7 * * *")
    public void moyu(){
        if(ds_flag) {
            GroupMsg group = null;
            Sender sender = null;

            for (Group_And_Sender group_and_sender :  hashset) {
                group = group_and_sender.getGroup();
                sender = group_and_sender.getSender();
                sender.sendGroupMsg(group, moyu.getMoyu());
            }
        }
    }


    @Autowired
    private GetNews getNews;
    @Scheduled(cron="0 0 8 * * * ")
    public void sendNews() throws IOException {
        if(ds_flag) {
            MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
            String s = getNews.EveryDayNews();

            for (Group_And_Sender group_and_sender :  hashset) {
                GroupMsg group = group_and_sender.getGroup();
                Sender sender = group_and_sender.getSender();
                sender.sendGroupMsg(group, "早上好 这是今天的每日新闻 本新闻来源于知乎");

                String historytody = historyTody.historytody();
                String finalS = historytody;
                String replace = finalS.replaceAll("\\\\", "").replaceAll(",", "\n").replace("[", "").replace("]", "").replaceAll("\"","");

                GroupMsg finalGroup =  group_and_sender.getGroup();
                builder.forwardMessage(forwardBuilder -> {
                    String[] split = replace.split("\n");
                    for (String s1 : split) {
                        forwardBuilder.add(finalGroup.getBotInfo(), s1);
                    }
                });
                MiraiMessageContent build = builder.build();
                sender.sendGroupMsg(group, build );

            }
        }
    }
}

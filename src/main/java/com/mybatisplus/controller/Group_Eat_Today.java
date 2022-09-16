package com.mybatisplus.controller;

import catcode.Neko;
import com.mybatisplus.entity.Group_And_Sender;
import com.mybatisplus.entity.Today_Eat;
import com.mybatisplus.service.IAdminService;
import com.mybatisplus.service.TodayEatService;
import com.mybatisplus.utils.MakeNeko;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import static org.apache.commons.lang.math.RandomUtils.nextDouble;

@Controller
@Transactional
public class Group_Eat_Today {
    @Autowired
    private TodayEatService todayEatService;
    @Autowired
    private MessageContentBuilderFactory factory;
    private HashSet<Group_And_Sender> hashset=new HashSet();
    private List<Today_Eat> today_eat=null;
    @Autowired
    private IAdminService adminService;
    @org.springframework.scheduling.annotation.Async
    @OnGroup
    @Filter(value="nana添加群今天吃什么",trim=true,matchType = MatchType.CONTAINS)
    public void sendNews(GroupMsg msg, Sender sender) throws IOException {
        AccountInfo accountInfo = msg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {

            Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
            boolean add = hashset.add(group_and_sender);
            if (add) {
                sender.sendGroupMsg(msg, "添加成功");
            } else {
                sender.sendGroupMsg(msg, "已存在该群");
            }
        }
    }

    @org.springframework.scheduling.annotation.Async
    @OnGroup
    @Filter(value="nana取消群今天吃什么",trim=true,matchType = MatchType.CONTAINS)
    public void removesend(GroupMsg msg, Sender sender) throws IOException {
        AccountInfo accountInfo = msg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {


            Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
            boolean add = hashset.remove(group_and_sender);
            if (add) {
                sender.sendGroupMsg(msg, "取消成功");
            } else {
                sender.sendGroupMsg(msg, "取消失败");
            }
        }
    }

    @OnGroup
    @Filter(value = "nana今天吃什么", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void sendeat(GroupMsg msg, Sender sender) throws IOException {
        Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
        group_and_sender.setSender(sender);
        group_and_sender.setGroup(msg);

        if (hashset.contains(group_and_sender)) {
            if (today_eat == null) {
                this.today_eat = todayEatService.Send_Today_Eat_Message();
            }
            int size = today_eat.size();
            double v = nextDouble();
            double v1 = size * v;
            Today_Eat today_eat1 = today_eat.get((int) v1);
            String qq = today_eat1.getQq();
            String message = today_eat1.getMessage();
            String s = "要不试试" + qq + "推荐的:\n" + message;
            sender.sendGroupMsg(msg, s);
        } else{
            sender.sendGroupMsg(msg,"管理员未开启该功能");
        }
    }
    @OnGroup
    @Filter(value = "学习今天吃什么", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void studyeat(GroupMsg msg, Sender sender) throws IOException {
        Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
        group_and_sender.setSender(sender);
        group_and_sender.setGroup(msg);
        String textlength = msg.getText();
        if( textlength.length()==7){
            sender.sendGroupMsg(msg, "请输入学习内容 如果不知道如何使用该功能请输入nana帮助 如果还是不明白请联系作者");
        }else {


            if (hashset.contains(group_and_sender)) {
                Today_Eat eat = new Today_Eat();
                String text = msg.getText().substring(7);
                MessageContent msgContent = msg.getMsgContent();
                String s = "";
                List<Neko> image = msgContent.getCats("image");
                if (image.size() != 0) {
                    for (Neko neko : image) {
                        String url = neko.get("url");
                        s = s + MakeNeko.MakePicture(url);
                    }
                }
                String qq = msg.getAccountInfo().getAccountCode();
                String nickname = msg.getAccountInfo().getAccountNickname();
                eat.setMessage(text + "\n" + s);
                eat.setQq(nickname + "(" + qq + ")");
                int i = todayEatService.Studay_Today_Eat_Message(eat);
                if (i != 0) {
                    sender.sendGroupMsg(msg, "存储 今天吃什么 成功");
                    this.today_eat = null;
                    this.today_eat = todayEatService.Send_Today_Eat_Message();
                } else {
                    sender.sendGroupMsg(msg, "存储 今天吃什么 失败");
                }
            } else {
                sender.sendGroupMsg(msg, "管理员未开启该功能");
            }
        }
    }
    @OnGroup
    @Filter(value = "删除今天吃什么", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void deleteeat(GroupMsg msg, Sender sender) throws IOException {
        Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
        group_and_sender.setSender(sender);
        group_and_sender.setGroup(msg);
        if (hashset.contains(group_and_sender)) {
            String text = msg.getText().substring(8);
            int i = 0;
            try {
                i = todayEatService.Delete_Today_Eat_Message(Integer.parseInt(text));
            } catch (Exception e) {
                sender.sendGroupMsg(msg, "出现错误 请输入 删除今天吃什么 要删除的内容的id");
            }
            if (i != 0) {
                this.today_eat = null;
                this.today_eat = todayEatService.Send_Today_Eat_Message();
                sender.sendGroupMsg(msg, "删除成功");
            } else {
                sender.sendGroupMsg(msg, "删除失败");
            }
        } else{
            sender.sendGroupMsg(msg,"管理员未开启该功能");
        }
    }
    @OnGroup
    @Filter(value = "查看所有今天吃什么", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void seleectAlleat(GroupMsg msg, Sender sender) throws IOException {
        Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
        group_and_sender.setSender(sender);
        group_and_sender.setGroup(msg);
        if (hashset.contains(group_and_sender)) {
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        if(this.today_eat==null){
            this.today_eat=todayEatService.Send_All_message();
        }
        List<Today_Eat> today_eats = this.today_eat;
        builder.forwardMessage(forwardBuilder -> {
            for (Today_Eat today_eat : today_eats) {
                String s="该条信息的ID是: "+today_eat.getId()+"\n信息内容是:\n"+today_eat.getMessage()+"\n存储人是:\n"+today_eat.getQq();
                forwardBuilder.add(msg.getBotInfo(), s);
            }
        });
        final MiraiMessageContent messageContent = builder.build();
        sender.sendGroupMsg(msg,messageContent);
    }
        else{
            sender.sendGroupMsg(msg,"管理员未开启该功能");
        }
    }


}
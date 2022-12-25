package com.mybatisplus.controller;

import catcode.Neko;
import com.mybatisplus.config.minio.config.service.FileStorageService;
import com.mybatisplus.config.minio.config.service.impl.MinIOFileStorageService;
import com.mybatisplus.entity.Group_And_Sender;
import com.mybatisplus.entity.Today_Eat;
import com.mybatisplus.service.IAdminService;
import com.mybatisplus.service.TodayEatService;
import com.mybatisplus.utils.MakeNeko;
import com.mybatisplus.utils.Send_To_minio;
import io.ktor.client.features.ClientRequestException;
import io.ktor.http.Url;
import lombok.extern.slf4j.Slf4j;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.math.RandomUtils.nextDouble;
@Slf4j
@Controller
@Transactional
public class Group_Eat_Today {

    @Autowired
    private Send_To_minio send_to_minio;

    @Autowired
    private MinIOFileStorageService fileStorageService;

    @Autowired
    private MessageContentBuilderFactory factory;
    @Autowired
    private TodayEatService todayEatService;

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
    public void sendeat(GroupMsg msg, Sender sender) throws IOException, EOFException {
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
            try{
                try{
                    String s = "要不试试" + qq + "推荐的:\n" + message;
                    sender.sendGroupMsg(msg, s);
                }
                catch(IllegalStateException e){
                    sender.sendGroupMsg(msg, "图片发送失败可能是因为存储的图片太大了导致服务器无法发送 图片id是"+today_eat1.getId());
                }
            }catch(ClientRequestException e) {
                String[] split = message.split("\n");
                sender.sendGroupMsg(msg, "存储图片已经失效 存储的key是: "+split[0]+" 该条失效信息正在被删除");
                int i1 = todayEatService.select_Id_By_Msg(message);
                int i = todayEatService.Delete_Today_Eat_Message(i1);
                if (i !=0){
                    sender.sendGroupMsg(msg, "失效信息已被删除");
                }else{
                    sender.sendGroupMsg(msg, "失效信息删除失败");
                }

            }

        } else{
            sender.sendGroupMsg(msg,"管理员未开启该功能");
        }
        group_and_sender=null;
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
                try{
                Today_Eat eat = new Today_Eat();
                String text = msg.getText().substring(7);
                MessageContent msgContent = msg.getMsgContent();
                String s = "";
                List<Neko> image = msgContent.getCats("image");
                if (image.size() != 0) {
                    for (Neko neko : image) {
                        String url = neko.get("url");
                        String fileId= send_to_minio.Send_ToMinio_Picture_new(url);
                        s = s + MakeNeko.MakePicture(fileId);
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

                } catch (Exception e) {
                    log.info("文件上传失败 错误如下");
                    e.printStackTrace();
                }

            } else {
                sender.sendGroupMsg(msg, "管理员未开启该功能");
            }
        }
    }



    @OnGroup
    @Filter(value = "查询今天吃什么", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void selecteat(GroupMsg msg, Sender sender) throws IOException {
        String text = msg.getText().substring(8);
        List<Today_Eat> list  =todayEatService.selectMsg(text);

        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        builder.forwardMessage(forwardBuilder -> {
            for (Today_Eat end :list) {
                String s="该条信息的ID是: "+end.getId()+"\n信息内容是:\n"+end.getMessage()+"\n存储人是:\n"+end.getQq();
                forwardBuilder.add(msg.getBotInfo(), s);
            }
        });
        final MiraiMessageContent messageContent = builder.build();
        sender.sendGroupMsg(msg,"若要删除某个信息请输入: 删除今天吃什么 id");
        sender.sendGroupMsg(msg,messageContent);
    }
// 猪脚饭
// 猪脚饭
//[CAT:image,file=http://gchat.qpic.cn/gchatpic_new/3041413893/2187532476-2935071242-0001F5663F72FD0CA3871DE006565E65/0?term=3]


    @OnGroup
    @Filter(value = "删除今天吃什么", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void deleteeat(GroupMsg msg, Sender sender) throws IOException {
        Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
        group_and_sender.setSender(sender);
        group_and_sender.setGroup(msg);
        if (hashset.contains(group_and_sender)) {
            String text = msg.getText().substring(7);
            int i = 0;
            try {
               Today_Eat today_Eat = todayEatService.select_Todayeat_By_id(Integer.parseInt(text.replaceAll(" ","")));
                    String message = today_Eat.getMessage();

                    if(message.contains("file=")){
                        String[] split = message.split("file=");
                        String substring = split[1].replace("]", "");
                        send_to_minio.Send_To_minio_Delete(substring);
                    }
                int number=Integer.parseInt(text.replaceAll(" ",""));
                i = todayEatService.Delete_Today_Eat_Message(number);

            } catch (Exception e) {

                sender.sendGroupMsg(msg, "出现错误 请输入 删除今天吃什么 要删除的内容的id");
            }
            if (i != 0) {
                this.today_eat = null;
                //刷新缓存
                this.today_eat = todayEatService.Send_Today_Eat_Message();
                sender.sendGroupMsg(msg, "删除成功");
            } else {
                sender.sendGroupMsg(msg, "删除失败");
            }
        } else{
            sender.sendGroupMsg(msg,"管理员未开启该功能");
        }
    }



}
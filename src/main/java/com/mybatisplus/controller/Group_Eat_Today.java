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
    @Filter(value="nana????????????????????????",trim=true,matchType = MatchType.CONTAINS)
    public void sendNews(GroupMsg msg, Sender sender) throws IOException {
        AccountInfo accountInfo = msg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //??????????????????QQ???
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {

            Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
            boolean add = hashset.add(group_and_sender);
            if (add) {
                sender.sendGroupMsg(msg, "????????????");
            } else {
                sender.sendGroupMsg(msg, "???????????????");
            }
        }
    }

    @org.springframework.scheduling.annotation.Async
    @OnGroup
    @Filter(value="nana????????????????????????",trim=true,matchType = MatchType.CONTAINS)
    public void removesend(GroupMsg msg, Sender sender) throws IOException {
        AccountInfo accountInfo = msg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //??????????????????QQ???
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {


            Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
            boolean add = hashset.remove(group_and_sender);
            if (add) {
                sender.sendGroupMsg(msg, "????????????");
            } else {
                sender.sendGroupMsg(msg, "????????????");
            }
        }
    }

    @OnGroup
    @Filter(value = "nana???????????????", trim = true, matchType = MatchType.CONTAINS)
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
                    String s = "????????????" + qq + "?????????:\n" + message;
                    sender.sendGroupMsg(msg, s);
                }
                catch(IllegalStateException e){
                    sender.sendGroupMsg(msg, "???????????????????????????????????????????????????????????????????????????????????? ??????id???"+today_eat1.getId());
                }
            }catch(ClientRequestException e) {
                String[] split = message.split("\n");
                sender.sendGroupMsg(msg, "???????????????????????? ?????????key???: "+split[0]+" ?????????????????????????????????");
                int i1 = todayEatService.select_Id_By_Msg(message);
                int i = todayEatService.Delete_Today_Eat_Message(i1);
                if (i !=0){
                    sender.sendGroupMsg(msg, "????????????????????????");
                }else{
                    sender.sendGroupMsg(msg, "????????????????????????");
                }

            }

        } else{
            sender.sendGroupMsg(msg,"???????????????????????????");
        }
        group_and_sender=null;
    }
    @OnGroup
    @Filter(value = "?????????????????????", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void studyeat(GroupMsg msg, Sender sender) throws IOException {


        Group_And_Sender group_and_sender = new Group_And_Sender(msg, sender);
        group_and_sender.setSender(sender);
        group_and_sender.setGroup(msg);
        String textlength = msg.getText();
        if( textlength.length()==7){
            sender.sendGroupMsg(msg, "????????????????????? ?????????????????????????????????????????????nana?????? ????????????????????????????????????");
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
                    sender.sendGroupMsg(msg, "?????? ??????????????? ??????");
                    this.today_eat = null;
                    this.today_eat = todayEatService.Send_Today_Eat_Message();
                } else {
                    sender.sendGroupMsg(msg, "?????? ??????????????? ??????");
                }

                } catch (Exception e) {
                    log.info("?????????????????? ????????????");
                    e.printStackTrace();
                }

            } else {
                sender.sendGroupMsg(msg, "???????????????????????????");
            }
        }
    }



    @OnGroup
    @Filter(value = "?????????????????????", trim = true, matchType = MatchType.CONTAINS)
    @Async
    public void selecteat(GroupMsg msg, Sender sender) throws IOException {
        String text = msg.getText().substring(8);
        List<Today_Eat> list  =todayEatService.selectMsg(text);

        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        builder.forwardMessage(forwardBuilder -> {
            for (Today_Eat end :list) {
                String s="???????????????ID???: "+end.getId()+"\n???????????????:\n"+end.getMessage()+"\n????????????:\n"+end.getQq();
                forwardBuilder.add(msg.getBotInfo(), s);
            }
        });
        final MiraiMessageContent messageContent = builder.build();
        sender.sendGroupMsg(msg,"?????????????????????????????????: ????????????????????? id");
        sender.sendGroupMsg(msg,messageContent);
    }
// ?????????
// ?????????
//[CAT:image,file=http://gchat.qpic.cn/gchatpic_new/3041413893/2187532476-2935071242-0001F5663F72FD0CA3871DE006565E65/0?term=3]


    @OnGroup
    @Filter(value = "?????????????????????", trim = true, matchType = MatchType.CONTAINS)
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

                sender.sendGroupMsg(msg, "???????????? ????????? ????????????????????? ?????????????????????id");
            }
            if (i != 0) {
                this.today_eat = null;
                //????????????
                this.today_eat = todayEatService.Send_Today_Eat_Message();
                sender.sendGroupMsg(msg, "????????????");
            } else {
                sender.sendGroupMsg(msg, "????????????");
            }
        } else{
            sender.sendGroupMsg(msg,"???????????????????????????");
        }
    }



}
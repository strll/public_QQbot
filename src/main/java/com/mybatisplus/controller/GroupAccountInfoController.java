package com.mybatisplus.controller;


import catcode.CatCodeUtil;
import catcode.Neko;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mybatisplus.entity.Group_And_Sender;
import com.mybatisplus.entity.Message;
import com.mybatisplus.listener.MyNewGroupMemberListen;

import com.mybatisplus.service.IAdminService;
import com.mybatisplus.service.IMessageService;

import com.mybatisplus.service.impl.MessageServiceImpl;
import com.mybatisplus.utils.Get_LOVE;
import com.mybatisplus.utils.Random_say;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wxt
 * @since 2022-07-23
 *   @OnGroup
 *     public void onGroupMsg(GroupMsg groupMsg) {
 *         // 打印此次消息中的 纯文本消息内容。
 *         // 纯文本消息中，不会包含任何特殊消息（例如图片、表情等）。
 *         System.out.println(groupMsg.getText());
 *
 *         // 打印此次消息中的 消息内容。
 *         // 消息内容会包含所有的消息内容，也包括特殊消息。特殊消息使用CAT码进行表示。
 *         // 需要注意的是，绝大多数情况下，getMsg() 的效率低于甚至远低于 getText()
 *         System.out.println(groupMsg.getMsg());
 *
 *         // 获取此次消息中的 消息主体。
 *         // messageContent代表消息主体，其中通过可以获得 msg, 以及特殊消息列表。
 *         // 特殊消息列表为 List<Neko>, 其中，Neko是CAT码的封装类型。
 *
 *         MessageContent msgContent = groupMsg.getMsgContent();
 *
 *         // 打印消息主体
 *         System.out.println(msgContent);
 *         // 打印消息主体中的所有图片的链接（如果有的话）
 *         List<Neko> imageCats = msgContent.getCats("image");
 *         System.out.println("img counts: " + imageCats.size());
 *         for (Neko image : imageCats) {
 *             System.out.println("Img url: " + image.get("url"));
 *         }
 *
 *
 *         // 获取发消息的人。
 *         GroupAccountInfo accountInfo = groupMsg.getAccountInfo();
 *         // 打印发消息者的账号与昵称。
 *         System.out.println(accountInfo.getAccountCode());
 *         System.out.println(accountInfo.getAccountNickname());
 *
 *
 *         // 获取群信息
 *         GroupInfo groupInfo = groupMsg.getGroupInfo();
 *         // 打印群号与名称
 *         System.out.println(groupInfo.getGroupCode());
 *         System.out.println(groupInfo.getGroupName());
 *     }
 */
@Controller
public class GroupAccountInfoController {
    @Autowired
private IMessageService service;

    private HashSet<Group_And_Sender> hashset=new HashSet();

    private volatile boolean send_flag=true; //回复模块启动标志
    @Autowired
    private MessageContentBuilderFactory factory;

    @Autowired
    private IAdminService adminService;
    @Autowired
    private Random_say random_say;

    @Async
    @OnGroup
    @Filter(value="nana帮助",trim=true,matchType = MatchType.CONTAINS)
    public void help(GroupMsg groupMsg, Sender sender) {

        if (!(factory instanceof MiraiMessageContentBuilderFactory)) {
            throw new RuntimeException("不支持mirai组件");
        }
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();

        // 通过 MiraiMessageContentBuilder.forwardMessage 构建一个合并消息。
        // 一般来讲，合并消息不支持与其他类型消息同时存在，因此不应再继续拼接其他消息。
        builder.forwardMessage(forwardBuilder -> {
            forwardBuilder.add(groupMsg.getBotInfo(),
                    "目前nana有一下几个功能:\n 1.关键词触发" +
                            "\n 2.学习功能 发送 nana学习 可以触发 " +
                            "\n 学习第一次发送的是key要求key必须是字符(建议出发关键字不要过短因为查询用的是模糊查询) " +
                            "\n 第二次发送的是value value可以是图片 " +
                    "\n 3.删除功能 发送nana删除 可以触发 " +
                    "\n 4.nana图片 发送随机二次元图片(2022.8.1新增) " +
                    "\n 5.nana查询关键词 根据nana自动触发的返回值查询触发词(2022.8.2新增)"+
                    "\n 6.定时发送固定信息(2022.8.3新增)"+
                    "\n 7.新增权限管理(2022.8.3新增)"+
                    "\n 如果机器人出现bug请管理员及时禁言 "+
                    "\n 8.nana天气 新增天气查询(2022.8.15)"+
                    "\n 9.nana模块管理" +
                    "\n nana听歌  (示例:nana听歌 Hurt)"+
                    "\n 11.nana每日新闻"+
                    "\n 12.nana微博热搜"+
                    "\n 13.nana历史上的今天"+
                    "\n 14.戳一戳nana发送信息"
                    +"\n 15.nana翻译(示例: nana翻译 hello)"
                    +"\n 16.nana百度(使用方法示例: nana百度 春节) "
                    +"\n 17.nana查询 使用方法同上(改接口失效请使用百度功能进行搜索)"
                    +"\n 18.nana摸鱼日历"+
                     "\n 19.nana找番(用法: nana找番+动漫截图)"
                    +"\n 20.nana聊天(nana监听你发送的信息并且做出回复 退出请输入nana退出聊天)"
                    +"\n 21.nana今天吃什么 随机发送\n"
                    +"\n  学习今天吃什么 (示例: 学习今天吃什么 泡面 泡面图片)"
                    +"\n  删除今天吃什么 id  id请输入 查看所有今天吃什么 获取所有id"
                    +"\n  查询今天吃什么空格+关键词 可通过模糊查询获取内容的所有信息 可通过id进行删除"
                    +"\n 22.nana动漫资讯"
                    +"\n 23.nana小鸡词典 (示例: nana小鸡词典 盐系)"
                    +"\n 24.nana刷新"
                    );
            forwardBuilder.add(groupMsg.getBotInfo(),"nana模块管理(管理员使用)");
            forwardBuilder.add(groupMsg.getBotInfo(),"更多功能正在开发中(指刚刚新建好文件夹)");
        });

        final MiraiMessageContent messageContent = builder.build();
        // 发送消息
        sender.sendGroupMsg(groupMsg, messageContent);
    }

@Autowired
private MessageServiceImpl messageServiceImpl;
    @Async
    @OnGroup
    @Filter(value="nana刷新",trim=true,matchType = MatchType.CONTAINS)
    public void help1(GroupMsg groupMsg, Sender sender) {
        int i = messageServiceImpl.Delete_Null_Message();
        sender.sendGroupMsg(groupMsg, "已刷新 删除无效数据:"+i+"条");
    }

    @OnGroup
    @Filter(value="nana模块管理",trim=true,matchType = MatchType.CONTAINS)
    public void helpmk(GroupMsg groupMsg, Sender sender) {
        if (!(factory instanceof MiraiMessageContentBuilderFactory)) {
            throw new RuntimeException("不支持mirai组件");
        }
        MiraiMessageContentBuilder builder = ((MiraiMessageContentBuilderFactory) factory).getMessageContentBuilder();
        builder.forwardMessage(forwardBuilder -> {
            forwardBuilder.add(groupMsg.getBotInfo(),"nana启动学习模块\n" +
                    "nana关闭学习模块\n " +
                    "nana关闭回复模块\n " +
                    "nana启动回复模块\n" +
                    "nana关闭定时模块\n" +
                    "nana启动定时模块\n" +
                    "nana关闭天气模块\n" +
                    "nana启动天气模块 (超级管理员使用)\n"
                    +"nana添加群定时\n"
                    +"nana取消群定时\n"
                    +"nana添加群回复\n"
                    +"nana取消群回复\n"
                    +"nana开启聊天模块 \n"
                    +"nana关闭聊天模块\n"
                    +"nana添加群今天吃什么\n"
                    +"nana取消群今天吃什么"
            );
        });

        final MiraiMessageContent messageContent = builder.build();

        // 发送消息
        sender.sendGroupMsg(groupMsg, messageContent);

    }




//==================================
@org.springframework.scheduling.annotation.Async
@OnGroup
@Filter(value="nana添加群回复",trim=true,matchType = MatchType.CONTAINS)
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
    //==================================
    @org.springframework.scheduling.annotation.Async
    @OnGroup
    @Filter(value="nana取消群回复",trim=true,matchType = MatchType.CONTAINS)
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
//====================================================
@Autowired
private Get_LOVE love;
    @Async
    @OnGroup
    public void onGroupMsg(GroupMsg groupMsg,Sender sender) throws IOException {
        Group_And_Sender group_and_sender= new Group_And_Sender(groupMsg, sender);
        group_and_sender.setSender(sender);
        group_and_sender.setGroup(groupMsg);

        if (send_flag&&hashset.contains(group_and_sender)) {
            String valuemessage = "";
            String text = groupMsg.getMsg();//获取发生信息
            if( (int)Math.round(Math.random()*10000)<7 ){
                //随机复读
                sender.sendGroupMsg(groupMsg, groupMsg.getMsgContent());
            }
            String q="[CAT:at,code="+groupMsg.getBotInfo().getBotCode()+"]";
            if(text.equals(q)){
                sender.sendGroupMsg(groupMsg,love.getQinghua());
            }

            if (!text.equals("") && text != null) {
                Message message = new Message();
                message.setKeymessage(text);
                List<Message> messages = service.Get_Message_by_key(message);
                int size = messages.size();
                if (messages.size() != 0) {
                    if (messages.size() == 1) {
                        valuemessage = messages.get(0).getValuemessage();
                        if (valuemessage.equals("")) {
                            String url = "[CAT:image,file=" + messages.get(0).getUrl() + "]";
                            sender.sendGroupMsg(groupMsg, url);
                        } else {
                            sender.sendGroupMsg(groupMsg, messages.get(0).getValuemessage());
                        }

                    } else {
                        double d = Math.random();
                        int i = (int) (d * size);
                        valuemessage = messages.get(i).getValuemessage();
                        if (valuemessage.equals("")) {
                            CatCodeUtil util = CatCodeUtil.INSTANCE;
                            String url = "[CAT:image,file=" + messages.get(i).getUrl() + "]";

                            sender.sendGroupMsg(groupMsg, url);
                        } else {

                            sender.sendGroupMsg(groupMsg, valuemessage);

                        }
                    }
                }
            }



        try{
            //查询b站信息
            List<Neko> cats = groupMsg.getMsgContent().getCats();
            Neko neko = cats.get(0);
            String content = neko.get("content");
            JSONObject jsonObject = JSON.parseObject(content);
            if(!Objects.isNull(jsonObject.get("desc").toString())){
                String desc = jsonObject.get("desc").toString();
                if (desc!=null&&desc.equals("哔哩哔哩")){

                    String meta = jsonObject.get("meta").toString();
                    JSONObject json = JSON.parseObject(meta);
                    JSONObject  detail_1= (JSONObject) json.get("detail_1");
                    String url =  detail_1.get("qqdocurl").toString();

                    Document parse = null;
                    try {
                        parse = Jsoup.parse(new URL(url), 3000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Elements elementsByClass = parse.getElementsByClass("desc-info desc-v2 open");
                    Element comment = parse.getElementById("v_desc");
                    sender.sendGroupMsg(groupMsg,"视频的简介是:\n"+comment.text());
                }
            }
        }catch (Exception e){

        }
        MessageContent messageContent=groupMsg.getMsgContent();
            List<Neko> cats = messageContent.getCats();
            if(cats!=null){
                Neko neko = cats.get(0);
                String botCode = groupMsg.getBotInfo().getBotCode();
                if(neko!=null&&neko.getType().equals("nudge")&&neko.get("target").equals(botCode)){
                    sender.sendGroupMsg(groupMsg,random_say.say());
                }
            }


        }
    }



    @OnGroup
    @Filter(value="nana骰子",trim=true,matchType = MatchType.CONTAINS)
    public void tz(GroupMsg groupMsg, Sender sender) {

        String text = groupMsg.getText();
        String accountCode = groupMsg.getAccountInfo().getAccountCode();
        if (text.length()<=6){

            sender.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()+"您的骰子是");
            sender.sendGroupMsg(groupMsg,"[CAT:at,code="+accountCode+"] "+"[CAT:dice,random=true]");
        }else{
            String replace = text.substring(6).replace(" ", "");
            int i = Integer.parseInt(replace);
            if (i>0&&i<7){
                sender.sendGroupMsg(groupMsg,  groupMsg.getAccountInfo().getAccountNickname()+"您的骰子是");
                sender.sendGroupMsg(groupMsg,  "[CAT:nudge,target="+groupMsg.getAccountInfo().getAccountCode()+"]");
                        sender.sendGroupMsg(groupMsg, "[CAT:dice,value="+replace+"]");
            }else{
                sender.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountNickname()+"骰子大小应该是1到6");
            }

        }

            }

    //==========================================================================










    @Async
    @OnGroup
    @Filter(value="nana关闭回复模块",trim=true,matchType = MatchType.CONTAINS)
    public void stopStudy(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                send_flag = false;
                sender.sendGroupMsg(groupMsg,"回复模块已经关闭");
            }

        }
    }
    @Async
    @OnGroup
    @Filter(value="nana启动回复模块",trim=true,matchType = MatchType.CONTAINS)
    public void startStudy(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                send_flag = true;
                sender.sendGroupMsg(groupMsg,"回复模块已经开启");
            }

        }
    }
}
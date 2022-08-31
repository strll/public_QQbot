package com.mybatisplus.abandoned;

import catcode.Neko;
import com.mybatisplus.entity.Message;
import com.mybatisplus.service.IAdminService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Controller
public class GroupStudyController {

    private volatile ArrayList<String> studyArray=new ArrayList<String>();

    private volatile Map<String,String> studyMap_key_QQ=new HashMap<String,String>();

    private volatile boolean flag=false;

    private volatile boolean flag_f=false;

    private volatile ArrayList<String> userArray=new ArrayList<String>();

    @Autowired
    private IMessageService service;

    @Autowired
    private IAdminService adminService;

    private volatile boolean Study_flag=true; //学习模块启动标志





    @Async
    @OnGroup
    @Filter(value="nana关闭学习模块",trim=true,matchType = MatchType.CONTAINS)
    public void stopStudy(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                Study_flag = false;
                sender.sendGroupMsg(groupMsg,"学习模块已经关闭");
            }

        }
        }
    @Async
    @OnGroup
    @Filter(value="nana启动学习模块",trim=true,matchType = MatchType.CONTAINS)
    public void startStudy(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                Study_flag = true;
                sender.sendGroupMsg(groupMsg,"学习模块已经开启");
            }

        }
    }



    @Async
    @OnGroup
    @Filter(value="nana学习",trim=true,matchType = MatchType.CONTAINS)
    public void study_send_before(GroupMsg groupMsg, Sender sender) {
        if(Study_flag){
            AccountInfo accountInfo = groupMsg.getAccountInfo();
            String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
            studyArray.add(accountCode);
            sender.sendGroupMsg(groupMsg,"请问您要学习什么?");
            flag_f=true;
        }else{
            sender.sendGroupMsg(groupMsg,"学习模块没有启动 请联系超级管理员开启学习模块");

        }

    }


    @OnGroup
    public void study_send_after(GroupMsg groupMsg, Sender sender) {
        if(flag_f){
            synchronized (this){


                    AccountInfo accountInfo = groupMsg.getAccountInfo();
                    String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
                    boolean contains = studyArray.contains(accountCode);
                    if (contains){
                        flag_f=false;
                        String text = groupMsg.getText();
                        if(text.contains(" ")||text.contains("nana学习")||text.equals("nana学习")||text.equals("nana删除")){
                            sender.sendGroupMsg(groupMsg,"为了避免关键词误触发 设置的关键词中不能存在空格或者关键词 现在重新触发学习功能");
                        }else if(text.equals("取消学习")){
                            sender.sendGroupMsg(groupMsg,"学习已取消");
                        }
                        else{
                            studyMap_key_QQ.put(accountCode,text);
                            sender.sendGroupMsg(groupMsg,"请问您要触发改关键词应该返回什么?");
                            this.flag=true;
                        }

                    }

                    userArray.remove(accountCode);
                    studyArray.remove(accountCode);
                }



            }



    }

    @Async
    @OnGroup
    public void study_set_KV(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        boolean b = studyMap_key_QQ.containsKey(accountCode);
        if(b&&flag){

            synchronized (this){
                flag=false;
                String text = groupMsg.getText();
                Message message = new Message();
                MessageContent msgContent = groupMsg.getMsgContent();
                List<Neko> image = msgContent.getCats("image");

                String url="";
                if(image.size()!=0){
                    Neko neko = image.get(0);
                    url = neko.get("url");
                    message.setUrl(url);
                }
                String key = studyMap_key_QQ.remove(accountCode);
                if(url.equals("")){
                    try{
                        String value = text;

                        message.setValuemessage(value);

                    }catch(ArrayIndexOutOfBoundsException e){
                        if(message.getValuemessage().equals("")&&message.getUrl().equals("")){
                            sender.sendGroupMsg(groupMsg, "未检测到学习内容");
                        }
                    }
                }
                message.setKeymessage(key);
                message.setQq(accountCode);
                int study = service.InsertMessage(message);

                if (study == 0) {
                    sender.sendGroupMsg(groupMsg, "学习失败");
                } else {
                    sender.sendGroupMsg(groupMsg, "学习成功");
                }

            }


        }

    }


}

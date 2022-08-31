package com.mybatisplus.controller;

import catcode.CatCodeUtil;
import catcode.Neko;
import com.mybatisplus.entity.Message;
import com.mybatisplus.service.IMessageService;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PrivateAccountInfoController {

    @Autowired
    private IMessageService service;




  //  private volatile Map userMap= new HashMap<String,String>();
    private volatile ArrayList<String> userArray=new ArrayList<String>();


    @OnPrivate
    @Filter(value="nana删除",trim=true,matchType = MatchType.CONTAINS)
    public void delete_send_before(PrivateMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        userArray.add(accountCode);
        sender.sendPrivateMsg(groupMsg,"请问您要删除什么?");
    }

    @OnPrivate
    public void delete_send_after(PrivateMsg groupMsg, Sender sender) {

        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
        boolean contains = userArray.contains(accountCode);
        if(contains){
            String text = groupMsg.getText();//获取发送信息

            int i = service.DeleteMessage(text);
            if (i==0){
                sender.sendPrivateMsg(groupMsg,"删除失败");
            }else{
                sender.sendPrivateMsg(groupMsg,"删除成功");
            }
            userArray.remove(accountCode);
        }


    }





    @OnPrivate
    public void onGroupMsg(PrivateMsg groupMsg, Sender sender) {
        String valuemessage="";
        String text = groupMsg.getText();//获取发生信息
        Message message = new Message();
        message.setKeymessage(text);
        List<Message> messages = service.Get_Message_by_key(message);
        int size= messages.size();
        if (messages.size()!=0){
            if (messages.size()==1){
                 valuemessage = messages.get(0).getValuemessage();
                if(valuemessage==null){
                    String url="[CAT:image,file="+messages.get(0).getUrl()+"]";
                    sender.sendPrivateMsg(groupMsg,url);
                }else {
                    sender.sendPrivateMsg(groupMsg,messages.get(0).getValuemessage());
                }

            }else{
                double d = Math.random();
                int i = (int)(d*size);
                valuemessage = messages.get(i).getValuemessage();
                if(valuemessage==null){
                    CatCodeUtil util = CatCodeUtil.INSTANCE;
                    String url="[CAT:image,file="+messages.get(i).getUrl()+"]";

                    sender.sendPrivateMsg(groupMsg,url);
                }else {

                    sender.sendPrivateMsg(groupMsg,valuemessage);

                }
            }
        }
    //    GroupAccountInfo accountInfo = groupMsg.getAccountInfo();
  //      String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号

    }


    @OnPrivate
    @Filter(value="nana学习",trim=true,matchType = MatchType.CONTAINS)
    public void onGroupMsg_Study(PrivateMsg groupMsg,Sender sender) {
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


        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号


        String[] split = text.split("\\s+");
        String key = split[1];
        if(url.equals("")){
            try{
                String value = split[2];

                message.setValuemessage(value);

            }catch(ArrayIndexOutOfBoundsException e){
                if(message.getValuemessage().equals("")&&message.getUrl().equals("")){
                    sender.sendPrivateMsg(groupMsg, "未检测到学习内容");
                }

            }
        }



            message.setKeymessage(key);
            message.setQq(accountCode);


            int study = service.InsertMessage(message);

            if (study == 0) {
                sender.sendPrivateMsg(groupMsg, "学习失败");
            } else {
                sender.sendPrivateMsg(groupMsg, "学习成功");
            }

    }


}

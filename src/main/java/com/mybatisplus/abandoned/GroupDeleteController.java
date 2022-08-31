package com.mybatisplus.abandoned;

import com.mybatisplus.entity.Message;
import com.mybatisplus.service.IAdminService;
import com.mybatisplus.service.IMessageService;
import love.forte.simbot.annotation.Async;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

//@Controller
public class GroupDeleteController {


    private volatile ArrayList<String> userArray=new ArrayList<String>();
    private volatile boolean delete_flag=false;
    @Autowired
    private IMessageService service;

    @Autowired
    private IAdminService adminService;
//123

    @OnGroup
    @Filter(value="nana删除",trim=true,matchType = MatchType.CONTAINS)
    public void delete_send_before(GroupMsg groupMsg, Sender sender) {
        synchronized(this){
            AccountInfo accountInfo = groupMsg.getAccountInfo();
            String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
            userArray.add(accountCode);
            sender.sendGroupMsg(groupMsg,"请问您要删除什么?");
            delete_flag=true;
        }

    }

    @Async
    @OnGroup
    public void delete_send_after(GroupMsg groupMsg, Sender sender) {
        if(delete_flag){
            synchronized(this){
           //     if(delete_flag){

                AccountInfo accountInfo = groupMsg.getAccountInfo();
                String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
                boolean contains = userArray.contains(accountCode);
                userArray.remove(accountCode);



                if(contains){
                    delete_flag=false;
                    String text = groupMsg.getText();//获取发送信息

                    List<String> strings = service.Get_QQ_by_key(text); //根据要删除的内容返回这个key所有的qq号

                    int size = strings.size();
        if(size!=0) {
            for (String string : strings) {
                //如果这个qq号和触发这个函数的QQ号相同并且权限满足
                if (string.equals(accountCode) || Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) < 2) {
                    //得到这个qq号所写的这个key的所有message
                    //      List<Message> messages = service.Get_Message_by_QQ_And_key(accountCode, text);
                    int i = 0;
                    if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) < 2) {
                        i = service.DeleteMessage(text);
                    } else {
                        i = service.DeleteMessage_By_QQ(text, accountCode);
                    }


                    if (i == 0) {
                        sender.sendGroupMsg(groupMsg, "删除失败");

                    } else {
                        sender.sendGroupMsg(groupMsg, "删除成功");

                    }
                    break;
                } else {
                    sender.sendGroupMsg(groupMsg, "您的权限不足 只能管理员或者本人才能删除");

                }
            }
        }else{
            sender.sendGroupMsg(groupMsg, "在数据库中未检索到删除的key");
        }
//

                }

            }

        }



    }

}

package com.mybatisplus.controller;

import com.mybatisplus.entity.Admin;
import com.mybatisplus.service.IAdminService;
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
import java.util.HashMap;
import java.util.Map;

@Controller
public class GruopSetAdmin {

    private volatile ArrayList<String> adminArray = new ArrayList<String>();
    private volatile boolean flag = false;
    @Autowired
    private IAdminService adminService;
    ;

    @Async
    @OnGroup
    @Filter(value = "nana权限设置", trim = true, matchType = MatchType.CONTAINS)
    public void set_admin_before(GroupMsg groupMsg, Sender sender) {
        AccountInfo accountInfo = groupMsg.getAccountInfo();
        String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号

        Admin admin = adminService.get_Admin_permission(accountCode);
        String account = admin.getAccount();
    synchronized (this){

    if(Integer.parseInt(account)<1){
        adminArray.add(accountCode);
        sender.sendGroupMsg(groupMsg, "请问您要将那个QQ号设置成管理员?");
        flag = true;
    }else{
        sender.sendGroupMsg(groupMsg, "您的那个权限不足 不能设置其他人的权限");
    }
    }





        }



    @Async
    @OnGroup
    public void set_admin_after(GroupMsg groupMsg, Sender sender){
        if(flag){

            synchronized (this){
                AccountInfo accountInfo = groupMsg.getAccountInfo();
                String accountCode = accountInfo.getAccountCode();  //获取发送人的QQ号
                boolean contains = adminArray.contains(accountCode);

                if (contains){
                    Admin admin = new Admin();
                    admin.setAccount(accountCode);
                    admin.setPermission("1");
                    String s = adminService.set_Admin_permission(admin);
                    sender.sendGroupMsg(groupMsg, s);
                }
            }


        }
    }


}

package com.mybatisplus.service.impl;

import com.mybatisplus.entity.Group_And_Sender;
import com.mybatisplus.service.Group_And_Sender_Storehouse;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Scope("prototype")
public class Group_And_Sender_Storehouse_Impl implements Group_And_Sender_Storehouse {
    private    ThreadLocal<HashSet<Group_And_Sender>> set = new ThreadLocal<>();
   private volatile  Group_And_Sender  group_and_sender = new Group_And_Sender();
  private volatile HashSet<Group_And_Sender> hashset=new HashSet();
    @Override
    public  boolean add(GroupMsg group, Sender sender) {


        group_and_sender.setSender(sender);
        group_and_sender.setGroup(group);

        boolean add = hashset.add(group_and_sender);
        set.set(hashset);
        return  add;
    }

    @Override
    public boolean remove(GroupMsg group, Sender sender) {
        Group_And_Sender  group_and_sender = new Group_And_Sender();
        group_and_sender.setSender(sender);
        group_and_sender.setGroup(group);
  //      set.remove();
        boolean remove = hashset.remove(group);
    //    set.set(hashset);
        return remove;
    }

    @Override
    public HashSet<Group_And_Sender> get() {
        HashSet<Group_And_Sender> group_and_senders = set.get();
        return group_and_senders;
    }


}

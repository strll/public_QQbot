package com.mybatisplus.service;

import com.mybatisplus.entity.Group_And_Sender;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

import java.util.HashSet;

public interface Group_And_Sender_Storehouse {


   public boolean add(GroupMsg group, Sender sender);

    public boolean remove(GroupMsg group, Sender sender);
    public HashSet<Group_And_Sender> get();
}

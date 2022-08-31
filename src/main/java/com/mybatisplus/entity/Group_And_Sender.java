package com.mybatisplus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group_And_Sender {
    private GroupMsg group;
    private Sender sender;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group_And_Sender that = (Group_And_Sender) o;
        // 获取群信息
        GroupInfo groupInfo = that.getGroup().getGroupInfo();
        // 打印群号
        String groupCode = groupInfo.getGroupCode();
        if (group != null ? !group.getGroupInfo().getGroupCode().equals(groupCode) : that.group != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = group != null ? group.getGroupInfo().getGroupCode().hashCode() : 0;
        return result;
    }
}

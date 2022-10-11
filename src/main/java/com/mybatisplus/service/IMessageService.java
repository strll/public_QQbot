package com.mybatisplus.service;

import com.mybatisplus.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wxt
 * @since 2022-07-23
 */
public interface IMessageService extends IService<Message> {
        public List<Message> Get_Message_by_key(Message key);
        public int Study(Message message);
        public int InsertMessage(Message message);

        int DeleteMessage(String text);
        int DeleteMessage_By_QQ(String text,String qq);
        public List<Message> Get_Key_by_Message(Message message);
        public List<Message> Get_Key_by_Url(Message message);
        public  List<String> Get_QQ_by_key(String key );
        public List<Message> Get_Message_by_QQ_And_key(String qq,String key);
        public int Delete_Null_Message();
}

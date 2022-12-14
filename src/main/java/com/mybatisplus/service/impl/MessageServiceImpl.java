package com.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus.entity.Message;
import com.mybatisplus.mapper.MessageMapper;
import com.mybatisplus.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatisplus.utils.Send_To_minio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wxt
 * @since 2022-07-23
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
@Autowired
    Send_To_minio send_to_minio;

    @Autowired
    private MessageMapper mapper;
    @Override
    @Cacheable(key="#key",value="QQ")
    public List<Message> Get_Message_by_key(Message key) {
        String k1=key.getKeymessage();
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        messageQueryWrapper.eq("keymessage",k1);
        List<Message> messages = mapper.selectList(messageQueryWrapper);
        return messages;
    }
    @Override
    @CacheEvict(value = "QQ", key="'Study'",allEntries=true)
    public int Study(Message message) {
        int i = mapper.InsertStudy(message);
        return i;
    }

    @Override
    @CacheEvict(value = "QQ", key="'InsertMessage'",allEntries=true)
    public int InsertMessage(Message message) {
        int insert = mapper.insert(message);
        return insert;
    }

    @Override
    @CacheEvict(value = "QQ", key="'DeleteMessage'",allEntries=true)
    public int DeleteMessage(String text) {
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        messageQueryWrapper.eq("keymessage",text);
        Message message = mapper.selectOne(messageQueryWrapper);
        try {
            send_to_minio.Send_To_minio_Delete(message.getUrl());
        }catch (Exception e){
       
        }

        int i =mapper.deletemessage(text);
        return i;
    }

    @Override
    public int DeleteMessage_By_QQ(String text, String qq) {
        return mapper.DeleteMessage_By_QQ(text,qq);
    }

    @Override
    public List<Message> Get_Key_by_Message(Message message) {
        List<Message> keyByMessage = mapper.getKeyByMessage(message);
        return keyByMessage;
    }

    @Override
    public List<Message> Get_Key_by_Url(Message message) {
        List<Message> keyByUrl = mapper.getKeyByUrl(message.getUrl());
        return keyByUrl;
    }

    @Override
    public  List<String> Get_QQ_by_key(String key) {
        List<String> qqByKeymessage = mapper.getQqByKeymessage(key);

        return qqByKeymessage;
    }

    @Override
    public List<Message> Get_Message_by_QQ_And_key(String qq, String key) {
        return mapper.Get_Message_by_QQ_And_key( qq,  key);
    }

    @Override
    public int Delete_Null_Message() {
        return mapper.DeleteNullMessage();
    }
}

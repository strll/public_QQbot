package com.mybatisplus.mapper;

import com.mybatisplus.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wxt
 * @since 2022-07-23
 */
public interface MessageMapper extends BaseMapper<Message> {
 List<Message> selectall();
 Message selectUseId(@Param("message") Message message);
 int InsertStudy(@Param("message") Message message);
 int Deletemessagenull() ;

 int deletemessage(@Param("message") String message);
 List<Message> getKeyByMessage(@Param("message") Message message);
 List<Message> getKeyByUrl(@Param("message") String message);

 List<String> getQqByKeymessage(@Param("Keymessage") String Keymessage);

 List<Message> Get_Message_by_QQ_And_key(@Param("qq")String qq,@Param("key") String key);

 int DeleteMessage_By_QQ(@Param("key")String text, @Param("qq")String qq);
 int DeleteNullMessage();
}

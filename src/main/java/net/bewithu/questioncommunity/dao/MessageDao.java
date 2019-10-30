package net.bewithu.questioncommunity.dao;

import net.bewithu.questioncommunity.model.Comment;
import net.bewithu.questioncommunity.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDao {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELDS = " from_id fromId, to_id toId, content, created_date createdDate, has_read hasRead, conversation_id conversationId ";
    @Insert("insert into "+TABLE_NAME+"("+INSERT_FIELDS+")"+" values (#{fromId}," +
            "#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})")
    int insertMessage(Message message);

    @Select("select"+SELECT_FIELDS+","+" count(id) as id from (select * from  message  where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt group by conversation_id order by created_date desc")
    List<Message> getMessageList(@Param("userId") int userId);

    @Select("select"+SELECT_FIELDS+" from"+TABLE_NAME+"where conversation_id=#{conversationId} order by created_date ASC")
    List<Message> getMessageByConversationId(@Param("conversationId") String  conversationId);

    @Select("select count(id) from "+TABLE_NAME+" where conversation_id=#{conversationId} and has_read=0 and from_id=#{toId}")
    int getUnReadMessageCount(@Param("conversationId") String conversationId,
                              @Param("toId") int toId);

    @Update("update "+TABLE_NAME+" set has_read=1"+" where conversation_id=#{conversationId} and to_id=#{toId}")
    boolean updateHasRead(@Param("conversationId")String conversationId,
                          @Param("toId") int toId);
}

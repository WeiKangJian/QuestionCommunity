package net.bewithu.questioncommunity.dao;

import net.bewithu.questioncommunity.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentDao {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, entity_id, entity_type, created_date, status ";
    String SELECT_FIELDS = " id, "+" content, user_id userId, entity_id entityId, entity_type entityType, created_date createdDate, status ";

    @Insert("insert into "+TABLE_NAME+"("+INSERT_FIELDS+")"+" values (#{content}," +
            "#{userId},#{entityId},#{entityType},#{createdDate},#{status})")
    int insertComment(Comment comment);

    @Select("select"+SELECT_FIELDS+" from"+TABLE_NAME+"where entity_id=#{entityId}")
    List<Comment> selectCommentByUserId(int entityId);

    @Select("select count(id)"+" from"+TABLE_NAME+"where entity_id=#{entityId} and entity_type=#{entityType}")
    int getCount(@Param("entityId") int entityId, @Param("entityType") int entityType);
}

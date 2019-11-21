package net.bewithu.questioncommunity.dao;

import net.bewithu.questioncommunity.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + " title, content, created_date createdDate, user_id userId, comment_count commentCount";

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    @SelectKey(statement="select last_insert_id()",before=false,keyProperty="id",resultType=Integer.class,keyColumn="id")
    int addQuestion(Question question);

    @Select("select"+SELECT_FIELDS+" from"+TABLE_NAME+"where id=#{id}")
    Question selectQuectionById(int id);

    @Update("update"+TABLE_NAME+"set comment_count=#{newCount} where id=#{id}")
    boolean upadteCommentCount(@Param("id") int id,@Param("newCount")int newCount);

    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);
}

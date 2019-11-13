package net.bewithu.questioncommunity.dao;

import net.bewithu.questioncommunity.model.Feed;
import net.bewithu.questioncommunity.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, type, created_date, data ";
    String SELECT_FIELDS = " id, "+"user_id userId, type, created_date createdDate, data ";

    /**
     * 插入feed数据
     * @param feed
     * @return
     */
    @Insert("insert into "+TABLE_NAME+"("+INSERT_FIELDS+")"+"values (#{userId},"+
            "#{type},#{createdDate},#{data})")
    int insertFeed(Feed feed);

    /**
     * 拉取数据，产生feed流
     * @param userIds
     * @param maxId
     * @param limit
     * @return
     */
    List<Feed> selectFeeds(@Param("userIds") List<Integer> userIds,
                           @Param("maxId") int maxId,
                           @Param("limit") int limit);

}

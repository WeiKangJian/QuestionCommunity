package net.bewithu.questioncommunity.dao;

import net.bewithu.questioncommunity.model.LoginTicket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = " id, " + " user_id userId, ticket, expired, status ";

    @Insert({"insert into"+TABLE_NAME+"("+INSERT_FIELDS+")" +
            " values (#{userId},#{ticket},#{expired},#{status})"})
    int insertLoginTicket(LoginTicket loginTicket);

    @Select("select "+SELECT_FIELDS+" from "+TABLE_NAME+"where "+"ticket=#{ticket}")
    LoginTicket selectByTicket(String ticket);

    @Update("update"+TABLE_NAME+"set status="+1+" where ticket=#{ticket}")
    boolean upadteStatus(String ticket);

    @Select("select "+SELECT_FIELDS+" from "+TABLE_NAME+"where "+"user_id=#{userid}")
    LoginTicket selectByUserId(int userid);
}

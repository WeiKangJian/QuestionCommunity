package net.bewithu.questioncommunity.Service;

import antlr.StringUtils;
import net.bewithu.questioncommunity.dao.LoginTicketDAO;
import net.bewithu.questioncommunity.dao.UserDAO;
import net.bewithu.questioncommunity.model.LoginTicket;
import net.bewithu.questioncommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    Random random = new Random();

    public User getUserById(int id) {
        return userDAO.selectById(id);
    }

    /**
     * 注册功能
     * @param userName
     * @param passWord
     * @return
     */
    public HashMap<String,String> register(String userName,String passWord){
        HashMap<String,String> map =new HashMap();
        if(userName.length()<5||passWord.length()<5){
            map.put("msg","用户名和密码的长度必须大于五");
            return map;
        }
        if(userDAO.selectByName(userName)!=null){
            map.put("msg","用户名已存在");
            return map;
        }
        User user =new User();
        user.setName(userName);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(Util.MD5(passWord+user.getSalt()));
        userDAO.addUser(user);

        map.put("ticket",addticket(userDAO.selectByName(userName).getId()));
        return map;
    }

    /**
     * 登录功能
     * @param userName
     * @param passWord
     * @return
     */
    public HashMap<String,String> login(String userName,String passWord){
        HashMap<String,String> map =new HashMap();
        if(userName==null||passWord==null){
            map.put("msg","用户名和密码不能为空");
            return map;
        }
        if(userDAO.selectByName(userName)==null){
            map.put("msg","用户名不存在");
            return map;
        }
        User user =userDAO.selectByName(userName);
        if(user.getPassword().equals(Util.MD5(passWord+user.getSalt()))){
            map.put("ticket",addticket(user.getId()));
        }

        return map;
    }

    /**
     * 添加cookies
     */
    public String addticket( int userID){
        LoginTicket loginTicket =new LoginTicket();
        String ticket = UUID.randomUUID().toString().substring(0,16);
        loginTicket.setUserId(userID);
        loginTicket.setTicket(ticket);
        Date date =new Date();
        date.setTime(date.getTime()+60*60*1000);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicketDAO.insertLoginTicket(loginTicket);
        return  ticket;
    }
    /**
     * 登出使得cokkies无效
     */
    public boolean vaildCookies(String cookie){
          return loginTicketDAO.upadteStatus(cookie);
    }

}

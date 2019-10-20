package net.bewithu.questioncommunity.interceptor;

import net.bewithu.questioncommunity.dao.LoginTicketDAO;
import net.bewithu.questioncommunity.dao.UserDAO;
import net.bewithu.questioncommunity.model.HostHolder;
import net.bewithu.questioncommunity.model.LoginTicket;
import net.bewithu.questioncommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserDAO userDAO;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(httpServletRequest.getCookies()==null) {
            return true;
        }
        for(Cookie cookie:httpServletRequest.getCookies()){
            if(cookie.getName().equals("ticket")){

                LoginTicket loginTicket =loginTicketDAO.selectByTicket(cookie.getValue());

                if(loginTicket!=null&&loginTicket.getExpired().after(new Date())&&loginTicket.getStatus()==0){
                    User user =userDAO.selectById(loginTicket.getUserId());
                    hostHolder.setUser(user);
                }

                }
            }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null&&hostHolder.getUser()!=null) {
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}

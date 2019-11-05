package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.*;
import net.bewithu.questioncommunity.async.EventModel;
import net.bewithu.questioncommunity.async.EventProducer;
import net.bewithu.questioncommunity.async.EventType;
import net.bewithu.questioncommunity.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
public class UserController {
    @Autowired
    add a;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    MessageService messageService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    Util util;

    /**
     * 登陆界面
     * @param model
     * @return
     */
    @RequestMapping(path = {"/loginview"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String loginview(Model model,
                            @RequestParam(value = "next",required = false) String next,
                            HttpServletResponse response) {
        //禁止浏览器保存登陆界面的缓存，防止用户后退重复提交表单
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","No-cache");
        response.setDateHeader("Expires", -1);
        response.setHeader("Cache-Control", "No-store");
        if(hostHolder.getUser()!=null){
            return "redirect:/";
        }
        model.addAttribute("next",next);
        return "login";
    }

    /**
     * 注册处理
     */
    @RequestMapping(path = {"/reg"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String reg(Model model,
                      @RequestParam(value = "username") String username,
                      @RequestParam(value = "password") String password,
                      HttpServletResponse httpServletResponse) {
        HashMap<String, String> map = userService.register(username, password);
        if (map.containsKey("ticket")) {
            Cookie cookie =new Cookie("ticket",map.get("ticket"));
            cookie.setMaxAge(3600*5);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
            EventModel registerModel =new EventModel();
            registerModel.setOwnerId(userService.getUserByName(username).getId())
                    .setType(EventType.REGISTER);
            eventProducer.produceEvent(registerModel);
            return "redirect:/";
        } else {
            model.addAttribute("msg",map.get("msg"));
            return "login";
        }
    }

    /**
     * 登陆处理
     */
    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String login(Model model,
                        @RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "next", required = false) String next,
                        HttpServletResponse httpServletResponse) {
        HashMap<String, String> map = userService.login(username,password);

        //  登陆信息成功，保存t票

        if (map.containsKey("ticket")) {
            Cookie cookie =new Cookie("ticket",map.get("ticket"));
            //设置cookie在电脑端存活时间
            cookie.setMaxAge(3600*5);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
            if(!StringUtils.isEmpty(next)){
                if(Util.judgeLegal(next)) {
                    return "redirect:" + next;
                }
            }
            return "redirect:/";
        } else {
            //失败信息保存
            model.addAttribute("msg",map.get("msg"));
            return "login";
        }
    }


    /**
     * 登出处理
     */
    @RequestMapping(path = "/logout")
    public String logout(@CookieValue("ticket") String ticket){
        userService.vaildCookies(ticket);
        hostHolder.clear();
        return "redirect:/";
    }
}

package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.QuestionService;
import net.bewithu.questioncommunity.Service.UserService;
import net.bewithu.questioncommunity.Service.add;
import net.bewithu.questioncommunity.dao.QuestionDAO;
import net.bewithu.questioncommunity.model.HostHolder;
import net.bewithu.questioncommunity.model.Question;
import net.bewithu.questioncommunity.model.ViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    add a;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        Date d = new Date();
        SimpleDateFormat sf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = sf.format(d);
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("time",date);
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    /**
     * 访问主页
     * @param model
     * @param pop
     * @return
     */
    @RequestMapping(path = {"/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
//        a.contextLoads();
        model.addAttribute("vos", getQuestions(0, pop, 10));
        return "index";
    }
    /**
     * 访问用户个人主页
     * @param model
     * @param
     * @return
     */
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        return "index";
    }

    /**
     * 登陆界面
     * @param model
     * @return
     */
    @RequestMapping(path = {"/loginview"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String loginview(Model model) {
        if(hostHolder.getUser()!=null){
            return "redirect:/";
        }
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
                        HttpServletResponse httpServletResponse) {
        HashMap<String, String> map = userService.login(username,password);
        if (map.containsKey("ticket")) {
            Cookie cookie =new Cookie("ticket",map.get("ticket"));
            cookie.setMaxAge(3600*5);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
            return "redirect:/";
        } else {
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

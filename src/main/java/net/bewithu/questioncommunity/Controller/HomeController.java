package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.*;
import net.bewithu.questioncommunity.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
    @Autowired
    CommentService commentService;
    @Autowired
    Util util;


    /**
     * 获取主页符合内容，生成VIEW
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        SimpleDateFormat sf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("time",sf.format(question.getCreatedDate()));
            vo.set("question", question);
            vo.set("user", userService.getUserById(question.getUserId()));
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
        model.addAttribute("nextPop",pop+10);
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
        /**
         * 登陆信息成功，保存t票
         */
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

    /**
     * 发布问题
     */
     @RequestMapping(path ="/question/add")
     @ResponseBody
     public String addQuestion(Model model,
                               @RequestParam(value="title") String title,
                               @RequestParam(value = "content") String content){
         User user =hostHolder.getUser();
         if(user==null){
          return Util.returnJson(999," ");
         }
         questionService.addQuestion(title,content,user.getId());
        return Util.returnJson(0,"ok");
     }

    /**
     * 转到问题详情并显示评论界面处理
     */
    @RequestMapping(path = "/question/{questionId}",method = {RequestMethod.GET,RequestMethod.POST})
    public String comment(Model model,
                          @PathVariable(value = "questionId") int entityId){
        List<Comment> commentList =commentService.selectCommentByQuestionId(entityId);
        List<ViewObject> views =new ArrayList<>();
        for(Comment comment:commentList){
            ViewObject vo =new ViewObject();
            vo.set("time",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(comment.getCreatedDate()));
            vo.set("comment",comment);
            vo.set("user",userService.getUserById(comment.getUserId()));
            views.add(vo);
        }
        model.addAttribute("views",views);
        model.addAttribute("question",questionService.getQuestion(entityId));
        return  "detail";
    }
    /**
     *添加评论信息
     */
    @RequestMapping(path = "/addComment",method = {RequestMethod.POST})
    public String addComment(Model model,
                             @RequestParam("content") String content,
                             @RequestParam("questionId") int entityid){
        if(hostHolder.getUser()==null){
            return "login";
        }
        //预先防止用户输入空字符串和其他，限制最小长度，具体后期留给前端处理
        if(content.length()<2){
            return "redirect:/question/"+entityid;
        }
        commentService.addComment(content,entityid,hostHolder.getUser().getId());

        //添加成功后，修改评论个数，这个后期还要通过异步实现，或者事务，这里先同步实现
        questionService.upadteQuestionCommentCount(entityid,commentService.getCommentCount(entityid,1));
        return "redirect:/question/"+entityid;
    }
}

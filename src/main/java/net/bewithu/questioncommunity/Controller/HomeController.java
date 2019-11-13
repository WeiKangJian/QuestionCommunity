package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.*;
import net.bewithu.questioncommunity.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    MessageService messageService;
    @Autowired
    FollowService followService;
    @Autowired
    Util util;

    private  static final Logger logger = LoggerFactory.getLogger(HomeController.class);
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
            vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,question.getId()));
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
        User user =userService.getUserById(userId);
        ViewObject vo2 = new ViewObject();
        vo2.set("user", user);
        vo2.set("commentCount", commentService.getCommentsCountByUserId(userId));
        vo2.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo2.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo2.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo2.set("followed", false);
        }
        model.addAttribute("profileUser", vo2);
        return "profile";
    }
}


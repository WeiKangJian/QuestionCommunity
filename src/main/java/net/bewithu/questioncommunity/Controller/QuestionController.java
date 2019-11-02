package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.*;
import net.bewithu.questioncommunity.model.Comment;
import net.bewithu.questioncommunity.model.HostHolder;
import net.bewithu.questioncommunity.model.User;
import net.bewithu.questioncommunity.model.ViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.util.resources.cldr.ar.CalendarData_ar_OM;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class QuestionController {
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
    LikeService likeService;
    @Autowired
    Util util;

    private final  int ENTITY_QUESTION = 1;
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
        if(hostHolder.getUser()==null){
            return "redirect:/loginview";
        }
        List<Comment> commentList =commentService.selectCommentByQuestionId(entityId);
        List<ViewObject> views =new ArrayList<>();
        for(Comment comment:commentList){
            ViewObject vo =new ViewObject();
            vo.set("time",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(comment.getCreatedDate()));
            vo.set("comment",comment);
            vo.set("user",userService.getUserById(comment.getUserId()));
            vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),ENTITY_QUESTION,comment.getId()));
            vo.set("likeCount",likeService.getLikeCount(ENTITY_QUESTION, comment.getId()));
            views.add(vo);
        }
        model.addAttribute("views",views);
        model.addAttribute("question",questionService.getQuestion(entityId));
        return  "detail";
    }
}

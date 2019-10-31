package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.*;
import net.bewithu.questioncommunity.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {
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
    Util util;

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

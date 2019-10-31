package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.*;
import net.bewithu.questioncommunity.model.HostHolder;
import net.bewithu.questioncommunity.model.Message;
import net.bewithu.questioncommunity.model.User;
import net.bewithu.questioncommunity.model.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {
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

    private  static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    /**
     *跳转到私信列表界面
     */
    @RequestMapping(path = "/msg/list")
    public String messageList(Model model,
                              HttpServletResponse response){
        //禁止浏览器缓存，实时显示未读消息个数
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","No-cache");
        response.setDateHeader("Expires", -1);
        response.setHeader("Cache-Control", "No-store");
        if(hostHolder.getUser()==null){
            return "login";
        }
        List<Message> messages= messageService.getMessage(hostHolder.getUser().getId());
        List<ViewObject> vos =new ArrayList<>();
        for(Message message:messages){
            ViewObject vo =new ViewObject();
            vo.set("conversation",message);
            User user;
            if(message.getToId()==hostHolder.getUser().getId()){
                user =userService.getUserById(message.getFromId());
            }
            else {
                user = userService.getUserById(message.getToId());
            }
            vo.set("unreadMessage",messageService.getUnReadMessageCount(message.getConversationId(),user.getId()));
            vo.set("time",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(message.getCreatedDate()));
            vo.set("user",user);
            vos.add(vo);
        }
        model.addAttribute("conversations",vos);
        return "letter";
    }

    /**
     * 具体每一条私信界面
     */
    @RequestMapping(path = "/msg/detail",method = {RequestMethod.GET})
    public String messageDetail(Model model,
                                @RequestParam(value = "conversationId",required = true) String conversationId){
        try {
            List<Message> messageList = messageService.getMessageByConversationId(conversationId);
            List<ViewObject> vos = new ArrayList<>();
            for (Message message : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("headUrl", userService.getUserById(message.getFromId()).getHeadUrl());
                vo.set("time", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(message.getCreatedDate()));
                vos.add(vo);
                model.addAttribute("messages",vos);
            }
        } catch (Exception e){
            logger.error(e.getMessage());
        }
        // 设置所有未读未已读
        messageService.updateHasRead(conversationId,hostHolder.getUser().getId());
        return "letterDetail";
    }

    /**
     * 发送私信的功能，AJAX传JSON
     */
    @RequestMapping("/msg/addMessage")
    @ResponseBody
    public  String sendMessage(@RequestParam("content") String content,
                               @RequestParam("toName") String toName){
        if(hostHolder.getUser()==null){
            return Util.returnJson(999,"未登录");
        }
        try {
            messageService.insertMessage(hostHolder.getUser().getId(), userService.getUserByName(toName).getId(), content);
        }catch (Exception e){
            logger.error(e.getMessage());
            return  Util.returnJson(1,e.getMessage());
        }
        return  Util.returnJson(0,"成功");
    }
}

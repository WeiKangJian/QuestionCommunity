package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.CommentService;
import net.bewithu.questioncommunity.Service.LikeService;
import net.bewithu.questioncommunity.Service.UserService;
import net.bewithu.questioncommunity.Service.Util;
import net.bewithu.questioncommunity.async.EventModel;
import net.bewithu.questioncommunity.async.EventProducer;
import net.bewithu.questioncommunity.async.EventType;
import net.bewithu.questioncommunity.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    CommentService commentService;
    @Autowired
    EventProducer eventProducer;

    private final  int ENTITY_QUESTION = 1;

    @RequestMapping(path ="/like",method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser()==null){
            return Util.returnJson(999,"未登录");
        }

        long likeCount = likeService.like(hostHolder.getUser().getId(),ENTITY_QUESTION,commentId);
        EventModel likeEventModel =new EventModel();
        likeEventModel.setType(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId())
                .setOwnerId(commentService.getUserIdByCommentId(commentId))
                .setMapValue("questionId",String.valueOf(commentService.getOneCommentById(commentId).getEntityId()));
        eventProducer.produceEvent(likeEventModel);
        return  Util.returnJson(0,String.valueOf(likeCount));
    }

    @RequestMapping(path ="/dislike",method = {RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser()==null){
            return Util.returnJson(999,"未登录");
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(),ENTITY_QUESTION,commentId);
        return  Util.returnJson(0,String.valueOf(likeCount));
    }
}

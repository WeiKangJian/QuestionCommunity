package net.bewithu.questioncommunity.async.handler;

import net.bewithu.questioncommunity.Service.MessageService;
import net.bewithu.questioncommunity.Service.UserService;
import net.bewithu.questioncommunity.async.EventHandle;
import net.bewithu.questioncommunity.async.EventModel;
import net.bewithu.questioncommunity.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CommentHandler implements EventHandle {
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    /**
     * 根据用户回答问题发送站内信提醒
     * @param eventModel
     */
    @Override
    public void doHandle(EventModel eventModel) {
        String content ="同学"+"<a href=http://127.0.0.1:8080/user/"
                +eventModel.getActorId()+">"
                +userService.getUserById(eventModel.getActorId()).getName()+"</a>"
                +"回答了你的问题。  "+"<a href=http://127.0.0.1:8080/question/"
                + eventModel.getMapValue("questionId")
                +">"+"去看看"+"</a>" ;
        //110是管理员账号
        messageService.insertMessage(110,eventModel.getOwnerId(),content);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.COMMENT);
    }
}

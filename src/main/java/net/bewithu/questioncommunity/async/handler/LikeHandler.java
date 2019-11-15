package net.bewithu.questioncommunity.async.handler;

import net.bewithu.questioncommunity.Service.MessageService;
import net.bewithu.questioncommunity.Service.UserService;
import net.bewithu.questioncommunity.async.EventHandle;
import net.bewithu.questioncommunity.async.EventModel;
import net.bewithu.questioncommunity.async.EventType;
import net.bewithu.questioncommunity.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandle {
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

     //点赞事件处理机制，发送站内信
    @Override
    public void doHandle(EventModel eventModel) {
        String content ="同学"+"<a href=/user/"
                +eventModel.getActorId()+">"
                +userService.getUserById(eventModel.getActorId()).getName()+"</a>"
                +"赞了你的回答。  "+"<a href=/question/"
                + eventModel.getMapValue("questionId")
                +">"+"去看看"+"</a>" ;
        ///110是管理员账号
        messageService.insertMessage(110,eventModel.getOwnerId(),content);
    }

    //自注册模块，通过Awire交给容器去注册
    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}

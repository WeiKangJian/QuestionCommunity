package net.bewithu.questioncommunity.async.handler;

import net.bewithu.questioncommunity.Service.MessageService;
import net.bewithu.questioncommunity.Service.UserService;
import net.bewithu.questioncommunity.async.EventHandle;
import net.bewithu.questioncommunity.async.EventModel;
import net.bewithu.questioncommunity.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FollowerHandler implements EventHandle {
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    Logger log = LoggerFactory.getLogger(FollowerHandler.class);
    @Override
    public void doHandle(EventModel eventModel) {
        String content ="同学"+"<a href=/user/"
                +eventModel.getActorId()+">"
                +userService.getUserById(eventModel.getActorId()).getName()+"</a>"
                +"关注了你。  ";
        ///110是管理员账号
        messageService.insertMessage(110,eventModel.getOwnerId(),content);

    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.FOLLOWER);
    }
}

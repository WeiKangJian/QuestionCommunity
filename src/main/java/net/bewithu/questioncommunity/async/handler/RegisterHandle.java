package net.bewithu.questioncommunity.async.handler;

import net.bewithu.questioncommunity.Service.MessageService;
import net.bewithu.questioncommunity.async.EventHandle;
import net.bewithu.questioncommunity.async.EventModel;
import net.bewithu.questioncommunity.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RegisterHandle implements EventHandle {
    @Autowired
    MessageService messageService;
    @Override
    public void doHandle(EventModel eventModel) {
        String content ="欢迎来到PKU社区，开始与世界分享你的问题和知识吧";
        ///110是管理员账号
        messageService.insertMessage(110,eventModel.getOwnerId(),content);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.REGISTER);
    }
}

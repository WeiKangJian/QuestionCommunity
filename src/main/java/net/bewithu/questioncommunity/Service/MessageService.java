package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.dao.MessageDao;
import net.bewithu.questioncommunity.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;
    /**
     * 获得当前用户的消息列表
     */
    public List<Message> getMessage(int userId){
       return  messageDao.getMessageList(userId);
    }

    /**
     * 获得一个消息列表的具体历史消息
     * @param
     * @param
     * @return
     */
    public List<Message> getMessageByConversationId(String conversationId){
        return  messageDao.getMessageByConversationId(conversationId);
    }

    /**
     * 发布消息
     * @return
     */
    public int insertMessage(int fromId,int toId,String content){
        Message message =new Message();
        message.setToId(toId);
        message.setFromId(fromId);
        message.setContent(content);
        if(fromId<toId) {
            message.setConversationId(String.format("%d_%d", fromId, toId));
        }
        else {
            message.setConversationId(String.format("%d_%d", toId, fromId));
        }
        message.setCreatedDate(new Date());
        return messageDao.insertMessage(message);
    }
    /**
     * 查询未读消息个数
     */
    public int getUnReadMessageCount(String conversationId,int toId){
        return  messageDao.getUnReadMessageCount(conversationId,toId);
    }
    /**
     * 将未读消息设置为已读
     */
    public  boolean updateHasRead(String conversationId,int toId){
        return  messageDao.updateHasRead(conversationId,toId);
    }
}

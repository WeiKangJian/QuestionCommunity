package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.dao.CommentDao;
import net.bewithu.questioncommunity.dao.UserDAO;
import net.bewithu.questioncommunity.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentDao commentDao;
    @Autowired
    UserDAO userDAO;

    /**
     * 添加每个评论中的用户对象
     * @param entityId
     * @return
     */
    public List<Comment> selectByUserId(int entityId){
        List<Comment> list = commentDao.selectCommentByUserId(entityId);
        for(Comment comment:list){
            comment.setUser(userDAO.selectById(comment.getUserId()));
        }
        return  list;
    }
}

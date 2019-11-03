package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.dao.CommentDao;
import net.bewithu.questioncommunity.dao.UserDAO;
import net.bewithu.questioncommunity.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentDao commentDao;
    @Autowired
    UserDAO userDAO;
    @Autowired
    SensitiveService sensitiveService;

    private final int ENTITY_QUESTION=1;
    private  static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    /**
     * 返回问题评论列表
     * @param entityId
     * @return
     */
    public List<Comment> selectCommentByQuestionId(int entityId){
        List<Comment> list = commentDao.selectCommentByUserId(entityId);
        return  list;
    }

    /**
     * 添加评论信息，评论用户，被评论问题的信息
     */
    public  boolean addComment(String content,int entityId,int userId){
        try {
            Comment comment = new Comment();
            //防脚本和敏感词
            content = HtmlUtils.htmlEscape(content);
            content =sensitiveService.matchTree(content);
            //初始化
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(entityId);
            comment.setEntityType(ENTITY_QUESTION);
            comment.setStatus(0);
            comment.setUserId(userId);
            commentDao.insertComment(comment);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return true;
    }
    /**
     * 查询问题评论数量
     */
    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCount(entityId,entityType);
    }
    /**
     * 根据评论ID，查发布评论的用户
     */
    public  int getUserIdByCommentId(int commentId){
        return  commentDao.getUserIdByCommentId(commentId);
    }

    /**
     * 根据ID查询单个评论
     */
    public  Comment getOneCommentById(int id){
       return commentDao.selectOneCommentById(id);
    }
}

package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.dao.QuestionDAO;
import net.bewithu.questioncommunity.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;

    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
    /**
     * 获取当前问题信息
     */
    public Question getQuestion(int questionId){
        return questionDAO.selectQuectionById(questionId);
    }

    /**
     * 添加问题
     */
    public boolean addQuestion(String title,String content,int userId){
        //脚本过滤(转义)
        title = HtmlUtils.htmlEscape(title);
        content =HtmlUtils.htmlEscape(content);
        //敏感词过滤
        title=sensitiveService.matchTree(title);
        content=sensitiveService.matchTree(content);
        Question question =new Question();
        question.setTitle(title);
        question.setContent(content);
        question.setCommentCount(0);
        question.setCreatedDate(new Date());
        question.setUserId(userId);
        return questionDAO.addQuestion(question)>0?true:false;
    }


}

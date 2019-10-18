package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.dao.QuestionDAO;
import net.bewithu.questioncommunity.dao.UserDAO;
import net.bewithu.questioncommunity.model.Question;
import net.bewithu.questioncommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

/**
 * 添加测试数据
 */
@Service
public class add {
    @Autowired
    UserDAO userDAO;
    @Autowired
    QuestionDAO questionDAO;
    public void contextLoads() {
        Random random = new Random();
        for (int i = 1; i <= 100; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            user.setPassword("newpassword");
            userDAO.updatePassword(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("Balaababalalalal Content %d", i));
            questionDAO.addQuestion(question);
        }
    }
}

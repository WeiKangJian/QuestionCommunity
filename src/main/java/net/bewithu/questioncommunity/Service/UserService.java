package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.dao.UserDAO;
import net.bewithu.questioncommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

}

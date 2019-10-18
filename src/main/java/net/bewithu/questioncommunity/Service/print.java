package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.dao.UserDAO;
import net.bewithu.questioncommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class print {
    @Autowired
    UserDAO userdao;

    public String  prin(){
        return "你好啊";
    }
}

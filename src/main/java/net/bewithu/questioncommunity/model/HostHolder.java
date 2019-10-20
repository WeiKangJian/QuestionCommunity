package net.bewithu.questioncommunity.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private static  ThreadLocal<User> users= new ThreadLocal<User>();

    public  User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }
    public  void clear(){
       users.remove();
    }
}

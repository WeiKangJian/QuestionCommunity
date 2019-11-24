package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.EntityType;
import net.bewithu.questioncommunity.Service.FeedService;
import net.bewithu.questioncommunity.Service.FollowService;
import net.bewithu.questioncommunity.model.Feed;
import net.bewithu.questioncommunity.model.HostHolder;
import net.bewithu.questioncommunity.model.ViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class FeedController {
    @Autowired
    FeedService feedService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;

    @RequestMapping(path = "/feeds")
    public String getFeeds(Model model){
        if(hostHolder.getUser()==null){
            return "login";
        }
        List<Feed> lists;
        if(hostHolder.getUser()==null) {
            lists = feedService.getFeeds(new ArrayList<Integer>(), 10, 10);
        }else {
            List<Integer> followeesIds =followService.getFollowees(hostHolder.getUser().getId(), EntityType.ENTITY_USER,0,50);
            lists = feedService.getFeeds(followeesIds, 10000, 10);
        }
            ViewObject vo = new ViewObject();
            vo.set("userHeadUrl","http://images.nowcoder.com/head/834t.png");
            vo.set("userName","社区管理员");
            vo.set("time",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            model.addAttribute("greenUser",vo);

        model.addAttribute("feeds",lists);
        return "feeds";
    }
}

package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.EntityType;
import net.bewithu.questioncommunity.Service.FeedService;
import net.bewithu.questioncommunity.Service.FollowService;
import net.bewithu.questioncommunity.model.Feed;
import net.bewithu.questioncommunity.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        List<Feed> lists;
        if(hostHolder.getUser()==null) {
            lists = feedService.getFeeds(new ArrayList<Integer>(), 10, 10);
        }else {
            List<Integer> followeesIds =followService.getFollowees(hostHolder.getUser().getId(), EntityType.ENTITY_USER,0,50);
            lists = feedService.getFeeds(followeesIds, 10, 10);
        }
        model.addAttribute("feeds",lists);
        return "feeds";
    }
}

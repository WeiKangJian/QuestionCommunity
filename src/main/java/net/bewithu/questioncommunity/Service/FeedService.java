package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.dao.FeedDAO;
import net.bewithu.questioncommunity.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;

    public List<Feed> getFeeds(List<Integer> userIds,int maxId,int limit){
        return feedDAO.selectFeeds(userIds,maxId,limit);
    }

    public int insertFeed(Feed feed){
        return  feedDAO.insertFeed(feed);
    }
}

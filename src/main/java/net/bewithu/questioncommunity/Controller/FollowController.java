package net.bewithu.questioncommunity.Controller;

import com.alibaba.fastjson.JSONObject;
import net.bewithu.questioncommunity.Service.*;
import net.bewithu.questioncommunity.model.HostHolder;
import net.bewithu.questioncommunity.model.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;

    /**
     * 关注用户服务
     *
     * @param ownerUoserId 被关注的用户ID
     * @return
     */
    @RequestMapping(path = "/followUser", method = RequestMethod.POST)
    @ResponseBody
    public String follower(@RequestParam("userId") int ownerUoserId) {
        if (hostHolder.getUser() == null) {
            return Util.returnJson(999, "未登录");
        }
        try {
            followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, ownerUoserId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Util.returnJson(1, "失败");
        }
        return Util.returnJson(0, "成功");
    }

    /**
     * 取消关注用户服务
     *
     * @param ownerUoserId 被取消关注的用户ID
     * @return
     */
    @RequestMapping(path = "/unfollowUser", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String unFollower(@RequestParam("userId") int ownerUoserId) {
        if (hostHolder.getUser() == null) {
            return Util.returnJson(999, "未登录");
        }
        try {
            followService.unFollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, ownerUoserId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Util.returnJson(1, "失败");
        }
        return Util.returnJson(0, "成功");
    }

    /**
     * 展示用户粉丝列表
     */
    @RequestMapping(path = "/user/{userId}/followers",method = RequestMethod.GET)
    public String displayFollowers(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("curUser", userService.getUserById(userId));
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        List<ViewObject> list = new ArrayList<>();
        List<Integer> userIdList = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        for (Integer id : userIdList) {
            ViewObject vo = new ViewObject();
            vo.set("user", userService.getUserById(id));
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userService.getUserById(id).getId()));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
            vo.set("followeeCount", followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
            vo.set("commentCount", commentService.getCommentsCountByUserId(userId));
            list.add(vo);
        }
        model.addAttribute("followers", list);
        return "followers";
    }

    /**
     e* 展示用户关注的人的列表
     */
    @RequestMapping(path = "/user/{userId}/followees",method = RequestMethod.GET)
    public String displayFollowees(Model model, @PathVariable("userId") int userId) throws IOException {
        model.addAttribute("curUser", userService.getUserById(userId));
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
        List<ViewObject> list = new ArrayList<>();
        List<Integer> userIdList = followService.getFollowees(userId,EntityType.ENTITY_USER, 0, 10);
        for (Integer id : userIdList) {
            ViewObject vo = new ViewObject();
            vo.set("user", userService.getUserById(id));
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userService.getUserById(id).getId()));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
            vo.set("followeeCount", followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
            vo.set("commentCount", commentService.getCommentsCountByUserId(userId));
            list.add(vo);
        }
        model.addAttribute("followees", list);
        return "followees";
    }

    /**
     * 关注问题服务
     *
     * @param entityId 被关注的问题ID
     * @return
     */
    @RequestMapping(path = "/followQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int entityId) {
        if (hostHolder.getUser() == null) {
            return Util.returnJson(999, "未登录");
        }
        try {
            followService.follow(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,entityId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Util.returnJson(1, "失败");
        }
        JSONObject questionfollow =new JSONObject();
        questionfollow.put("code",0);
        questionfollow.put("msg","成功");
        questionfollow.put("id",hostHolder.getUser().getId());
        questionfollow.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,entityId));
        questionfollow.put("headUrl",hostHolder.getUser().getHeadUrl());
        return questionfollow.toJSONString();
    }

    /**
     * 取消关注问题服务
     *
     * @param entityId 被关注的问题ID
     * @return
     */
    @RequestMapping(path = "/unfollowQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int entityId) {
        if (hostHolder.getUser() == null) {
            return Util.returnJson(999, "未登录");
        }
        try {
            followService.unFollow(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,entityId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Util.returnJson(1, "失败");
        }
        JSONObject questionfollow =new JSONObject();
        questionfollow.put("code",0);
        questionfollow.put("msg","成功");
        questionfollow.put("id",hostHolder.getUser().getId());
        questionfollow.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,entityId));
        return questionfollow.toJSONString();
    }
}

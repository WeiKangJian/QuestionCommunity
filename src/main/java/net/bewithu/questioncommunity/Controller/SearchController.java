package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.*;
import net.bewithu.questioncommunity.model.HostHolder;
import net.bewithu.questioncommunity.model.Question;
import net.bewithu.questioncommunity.model.User;
import net.bewithu.questioncommunity.model.ViewObject;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController{
        @Autowired
        add a;
        @Autowired
        QuestionService questionService;
        @Autowired
        UserService userService;
        @Autowired
        HostHolder hostHolder;
        @Autowired
        CommentService commentService;
        @Autowired
        MessageService messageService;
        @Autowired
        FollowService followService;
        @Autowired
        SearchService searchService;
        @Autowired
        Util util;
        private  static final Logger logger = LoggerFactory.getLogger(HomeController.class);
        /**
         * 搜索结果展示
         * @param model
         * @return
         */
        @RequestMapping(path = {"search"}, method = {RequestMethod.GET})
        public String index(Model model,
                            @RequestParam(value = "offset", defaultValue = "0") int offset,
                            @RequestParam(value = "count",defaultValue = "10") int count,
                            @RequestParam(value = "q") String keyWord) throws IOException, SolrServerException {
            List<Question> questionList=searchService.search(keyWord,"<font color='red'><b>","</b></font>",offset,count);
            List<ViewObject> vos = new ArrayList<>();
            for (Question originQuestion : questionList) {
                SimpleDateFormat sf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
                ViewObject vo = new ViewObject();
                Question question = questionService.getQuestion(originQuestion.getId());
                if(originQuestion.getContent()!=null){
                    question.setContent(originQuestion.getContent());
                }
                if(originQuestion.getTitle()!=null){
                    question.setTitle(originQuestion.getTitle());
                }
                vo.set("time",sf.format(question.getCreatedDate()));
                vo.set("question", question);
                vo.set("user", userService.getUserById(question.getUserId()));
                vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,question.getId()));
                vos.add(vo);
            }
            model.addAttribute("vos",vos);
            model.addAttribute("nextOffset",offset+10);
            model.addAttribute("q",keyWord);
            return "result";
        }

}

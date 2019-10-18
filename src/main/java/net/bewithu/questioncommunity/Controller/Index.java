package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.add;
import net.bewithu.questioncommunity.Service.print;
import net.bewithu.questioncommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//@Controller
public class Index {

    @Autowired
    print p;

    @Autowired
    add a;


    @RequestMapping(path = {"/index"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String test(){
        //加载测试数据据
//      a.contextLoads();
        return "ok";
    }

    @RequestMapping(path = {"/admin"},method = {RequestMethod.GET,RequestMethod.POST})
    public String admin(@RequestParam("key") String key){
        if(key.equals("123")){
            throw new IllegalArgumentException("不对不对！");
        }
        throw new IllegalArgumentException("不对不对！");
    }

    @ExceptionHandler()
    public String exception(Model model,Exception e){
        model.addAttribute("exception",e.getMessage());
        Date date =new Date();
        SimpleDateFormat sim =new SimpleDateFormat("HH:mm:ss");
        model.addAttribute("date",sim.format(date));
        System.out.println(e.getMessage());
        return "exception";
    }

    @RequestMapping(path = {"/vm"},method = {RequestMethod.GET,RequestMethod.POST})
    public String template(Model model,
                           HttpServletRequest reuqest,
                           HttpServletResponse response){
       HttpSession session=  reuqest.getSession();
       model.addAttribute("sessionid",session.getId());
        String[] s =new String[]{"蓝色","绿色"};

        List<String> list = Arrays.asList(new String[]{"蓝色","绿色"});

        model.addAttribute("value1","123");
        model.addAttribute("colors",list);
        return "home";

    }
}

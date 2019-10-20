package net.bewithu.questioncommunity.Controller;

import net.bewithu.questioncommunity.Service.add;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Controller
public class Index {


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

}

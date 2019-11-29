package net.bewithu.questioncommunity.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class Log {
    private static  final Logger logger =  LoggerFactory.getLogger(Log.class);
    @Before("execution(* net.bewithu.questioncommunity.Controller.Index.test(..))")
    public void before(JoinPoint joinPoint){
        for(Object o:joinPoint.getArgs()){
            logger.info(o.toString());
        }
        logger.info("befor");

    }
    @After("execution(* net.bewithu.questioncommunity.Controller.Index.test(..))")
    public void after(){
        logger.info("after");
    }
}

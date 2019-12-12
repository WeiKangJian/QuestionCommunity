package net.bewithu.questioncommunity.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.bewithu.questioncommunity.Util.RedisAdapter;
import net.bewithu.questioncommunity.Util.RedisKeyProducer;
import org.aspectj.runtime.internal.cflowstack.ThreadStackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class EventConsumer implements DisposableBean, InitializingBean, ApplicationContextAware {
    @Autowired
    RedisAdapter redisAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private HashMap<EventType, List<EventHandle>> config =new HashMap<EventType, List<EventHandle>>();
    private ApplicationContext applicationContext;
    /**
     * 线程池来复用
     * ExecutorService threadPoll = Executors.newFixedThreadPool(50);
     * 行吧，自己建就自己建
    **/
    ThreadPoolExecutor threadPool  =new ThreadPoolExecutor(30,50,5, TimeUnit.MINUTES,new ArrayBlockingQueue<Runnable>(20));

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandle> beans = applicationContext.getBeansOfType(EventHandle.class);
        //将事件类型和其对应的hadle的map构造出来
        if(beans!=null){
            for(Map.Entry<String,EventHandle> entry:beans.entrySet()){
                //获取每个实现了handle接口的具体hadle，获得其支持的事件类型集合
                List<EventType> list =entry.getValue().getSupportEventType();
                //构建
                for(EventType type:list){
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandle>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

         /**
          * 构建事件和处理器的map完毕，起线程
          * 哇，这个编辑器好牛逼，还推荐lambda,配合P3C美滋滋
          * 起30个消费者线程，并行处理队列
          * 起不动的呀，默认缓存连接池最多9个，改配置文件报错，后面再说吧
          **/
         logger.info("开始创建线程池");
        for(int i=0;i<=3;i++) {
            logger.info("创建的第"+i+"个线程");
            threadPool.execute(() -> {
                String key = RedisKeyProducer.getEventQueueKey();
                while (true) {
                    List<String> list = redisAdapter.brpop(key);
                    for (String message : list) {
                        if (message.equals(key)) {
                            continue;
                        }
                        EventModel model = JSON.parseObject(message, EventModel.class);
                        for (EventHandle handle : config.get(model.getType())) {
                            handle.doHandle(model);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext =applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        threadPool.shutdown();
    }
}

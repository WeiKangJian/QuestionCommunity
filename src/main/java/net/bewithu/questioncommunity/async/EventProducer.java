package net.bewithu.questioncommunity.async;

import com.alibaba.fastjson.JSONObject;
import net.bewithu.questioncommunity.Util.RedisAdapter;
import net.bewithu.questioncommunity.Util.RedisKeyProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    @Autowired
    RedisAdapter redisAdapter;
    private  static  final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    public boolean produceEvent(EventModel model){
        try {
            String key = RedisKeyProducer.getEventQueueKey();
            String eventModel = JSONObject.toJSONString(model);
            redisAdapter.lpush(key, eventModel);
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
            return  false;
        }
    }

}

package net.bewithu.questioncommunity.Util;

import net.bewithu.questioncommunity.Controller.HomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

@Service
public class RedisAdapter implements InitializingBean {
    private  JedisPool jedisPool;

    private  static final Logger logger = LoggerFactory.getLogger(RedisAdapter.class);
    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool =new JedisPool("redis://localhost:6379/9");
    }

    public static  void main(String [] args){

        JedisPool pool = new JedisPool("redis://localhost:6379/9");
        for (int i = 0; i < 100; ++i) {
            Jedis j = pool.getResource();
            System.out.println(j.get("pv"));
            j.close();
        }

    }
    public long sadd(String key,String value){
        Jedis jedis = null;
        try {
            jedis=jedisPool.getResource();
        return jedis.sadd(key,value);
        }catch (Exception e){
        logger.error("add"+e.getMessage());
        }finally {
            jedis.close();
        }
        return 0;
    }

    public long  srem(String key,String value){
        Jedis jedis = null;
        try {
            jedis=jedisPool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("rem"+e.getMessage());
        }finally {
            jedis.close();
        }
        return 0;
    }

    public long  scard(String key){
        Jedis jedis = null;
        try {
            jedis=jedisPool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("card"+e.getMessage());
        }finally {
            jedis.close();
        }
        return 0;
    }

    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try {
            jedis=jedisPool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return false;
    }
}

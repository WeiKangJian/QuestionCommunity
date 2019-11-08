package net.bewithu.questioncommunity.Util;

import net.bewithu.questioncommunity.Controller.HomeController;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Set;

/**
 * 这个类是对jedis里面的方法做一个封装，主要是使用缓存连接池和异常处理
 */
@Service
public class RedisAdapter implements InitializingBean {
    private  JedisPool jedisPool;

    private  static final Logger logger = LoggerFactory.getLogger(RedisAdapter.class);
    @Override
    public void afterPropertiesSet() throws Exception {
        //这里加了配置文件就出错，我服了
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxTotal(50);
//        config.setMaxIdle(50);
//        config.setMinIdle(0);
////        jedisPoolConfig.setMaxIdle(50);
////        jedisPoolConfig.setTestOnBorrow(true);
        jedisPool =new JedisPool("redis://localhost:6379/9");
    }

//    public static  void main(String [] args){
//        JedisPool pool = new JedisPool("redis://localhost:6379/9");
//        for (int i = 0; i < 100; ++i) {
//            Jedis j = pool.getResource();
//            System.out.println(j.get("pv"));
//            j.close();
//        }
//
//    }
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

    public long lpush(String key,String value){
        Jedis jedis = null;
        try {
            jedis=jedisPool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return 0;
    }

    public List<String> brpop(String key){
        Jedis jedis = null;
        try {
            jedis=jedisPool.getResource();
            //参数0表示一直阻塞下去
            return jedis.brpop(0,key);
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    public long zadd(String key,double score,String value){
        Jedis jedis = null;
        try {
            jedis=jedisPool.getResource();
            //参数0表示一直阻塞下去
            return jedis.zadd(key,score,value);
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return 0;
    }

    public long zrem(String key,String value){
        Jedis jedis = null;
        try {
            jedis=jedisPool.getResource();
            return jedis.zrem(key,value);
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return 0;
    }

    public long zcard(String key){
        Jedis jedis = null;
        try {
            jedis=jedisPool.getResource();
            return jedis.zcard(key);
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return 0;
    }

    //这个一定别忘了关闭
    public  Jedis getJedis(){
        return jedisPool.getResource();
    }

    //开启事务的类
    public Transaction multi(Jedis jedis){
        try{
            return  jedis.multi();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    //执行事务
    public List<Object> exec(Transaction transaction,Jedis jedis){
        try{
            return  transaction.exec();
        }catch (Exception e){
            transaction.discard();
            logger.error(e.getMessage());
        }finally {
            if(transaction!=null){
                try{
                    transaction.close();
                }catch (Exception e){
                    logger.error(e.getMessage());
                }
            }
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    public Set<String> zrevrange(String key, long start, long end){
        Jedis jedis = null;
        try {
            jedis=jedisPool.getResource();
            return jedis.zrevrange(key,start,end);
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }

    public Double zscore(String key, String member){
        Jedis jedis = null;
        try {
            jedis=jedisPool.getResource();
            return jedis.zscore(key,member);
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            jedis.close();
        }
        return null;
    }
}

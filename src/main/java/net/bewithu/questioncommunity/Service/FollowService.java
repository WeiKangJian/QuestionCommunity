package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.Util.RedisAdapter;
import net.bewithu.questioncommunity.Util.RedisKeyProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {

    @Autowired
    RedisAdapter redisAdapter;

    //点击关注之后发生的事件
    public boolean follow(int userId,int entityType,int entityId){
        String fllowers =RedisKeyProducer.getFllowers(entityType,entityId);
        String fllowees = RedisKeyProducer.getFllowees(userId,entityType);
        Date date =new Date();
        Jedis jedis = redisAdapter.getJedis();
        Transaction transaction = redisAdapter.multi(jedis);
        transaction.zadd(fllowees,date.getTime(),String.valueOf(entityId));
        transaction.zadd(fllowers,date.getTime(),String.valueOf(userId));
        List<Object> list =redisAdapter.exec(transaction,jedis);
        return list.size()==2&&(long)list.get(0)>0&&(long)list.get(1)>0;
    }

    //取消关注
    public boolean unFollow(int userId,int entityType,int entityId){
        String fllowers =RedisKeyProducer.getFllowers(entityType,entityId);
        String fllowees = RedisKeyProducer.getFllowees(userId,entityType);
        Jedis jedis = redisAdapter.getJedis();
        Transaction transaction = redisAdapter.multi(jedis);
        transaction.zrem(fllowees,String.valueOf(entityId));
        transaction.zrem(fllowers,String.valueOf(userId));
        List<Object> list =redisAdapter.exec(transaction,jedis);
        return list.size()==2&&(long)list.get(0)>0&&(long)list.get(1)>0;
    }

    //获得关注者的数量
    public long getFollowerCount(int entityType,int entityId){
        String fllowers =RedisKeyProducer.getFllowers(entityType,entityId);
       return redisAdapter.zcard(fllowers);
    }

    // 获得自己关注的对象数量
    public long getFolloweeCount(int userId,int entityType){
        String fllowees =RedisKeyProducer.getFllowees(userId,entityType);
        return redisAdapter.zcard(fllowees);
    }

    private List<Integer> getIdsFromSet(Set<String> set){
        List<Integer> list =new ArrayList<Integer>();
        for(String str:set){
            list.add(Integer.parseInt(str));
        }
        return list;
    }

    //获得所有被关注者的信息
    public  List<Integer> getFollowers(int entityType,int entityId,int start,int end){
        String fllowers =RedisKeyProducer.getFllowers(entityType,entityId);
        return getIdsFromSet(redisAdapter.zrevrange(fllowers,start,end));
    }

    //获得关注的所有对象的ID
    public  List<Integer> getFollowees(int userId,int entityType,int start,int end){
        String fllowees =RedisKeyProducer.getFllowees(userId,entityType);
        return getIdsFromSet(redisAdapter.zrevrange(fllowees,start,end));
    }
    public boolean isFollower(int userId,int entityType,int entityId){
        String fllowers =RedisKeyProducer.getFllowers(entityType,entityId);
       return redisAdapter.zscore(fllowers,String.valueOf(userId))!=null;
    }
}

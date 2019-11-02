package net.bewithu.questioncommunity.Service;
import net.bewithu.questioncommunity.Util.RedisAdapter;
import net.bewithu.questioncommunity.Util.RedisKeyProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    RedisAdapter redisAdapter;

    public long like(int userId,int entityType,int entityId){
        String likeKey = RedisKeyProducer.getLikeKey(entityType,entityId);
        redisAdapter.sadd(likeKey,String.valueOf(userId));
        String disLikeKey =RedisKeyProducer.getDLikeKey(entityType,entityId);
        redisAdapter.srem(disLikeKey,String.valueOf(userId));
        return  redisAdapter.scard(likeKey);
    }

    public long disLike(int userId,int entityType,int entityId){
        String disLikeKey =RedisKeyProducer.getDLikeKey(entityType,entityId);
        redisAdapter.sadd(disLikeKey,String.valueOf(userId));
        String likeKey = RedisKeyProducer.getLikeKey(entityType,entityId);
        redisAdapter.srem(likeKey,String.valueOf(userId));
        return  redisAdapter.scard(likeKey);
    }

    public long getLikeCount(int entityType,int entityId){
        String likeKey = RedisKeyProducer.getLikeKey(entityType,entityId);
        return  redisAdapter.scard(likeKey);
    }

    public  int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey =RedisKeyProducer.getLikeKey(entityType,entityId);
        if(redisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        else{
            String disLikeKey =RedisKeyProducer.getDLikeKey(entityType,entityId);
            return  redisAdapter.sismember(disLikeKey,String.valueOf(userId))?-1:0;
        }
    }
}

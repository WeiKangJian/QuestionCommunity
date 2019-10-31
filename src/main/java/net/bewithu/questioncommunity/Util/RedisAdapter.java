package net.bewithu.questioncommunity.Util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import java.util.PriorityQueue;

public class RedisAdapter {
    public static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }
    public  static <queue> void main(String[] args) {
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        jedis.flushDB();
        /**
         * 数据类型之String
         */
        jedis.set("hello", "world");
        jedis.set("hello", "hahah");
        print(0, jedis.exists("hello"));
        print(1, jedis.get("hello"));
        jedis.rename("hello", "he");
        print(2, jedis.get("he"));
        jedis.setex("hello", 1800, "good");
        print(3, jedis.get("hello"));
        jedis.set("pv", "100");
        jedis.incr("pv");
        jedis.incrBy("pv", 100);
        jedis.decrBy("pv", 20);
        print(4, jedis.get("pv"));
        print(5, jedis.keys("*"));

        /**
         * 数据类型之list
         */
        String listName = "listName";
        for (int i = 1; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(6, jedis.lrange(listName, 0, 12));
        print(6, jedis.llen(listName));
        print(7, jedis.lpop(listName));
        print(8, jedis.lrange(listName, 0, 4));
        print(9, jedis.llen(listName));
        print(10, jedis.lindex(listName, 3));
        print(11, jedis.lrem(listName, 0, "a6"));
        print(12, jedis.lrange(listName, 0, 12));
        print(13, jedis.keys("*"));
        /**
         * 数据类型之hash
         */
        String userkey = "user";
        jedis.hset(userkey, "name", "xiaowei");
        jedis.hset(userkey, "sex", "boy");
        jedis.hset(userkey, "num", "13579");
        print(14, jedis.hget(userkey, "sex"));
//        jedis.incr("num");
        print(15, jedis.hget(userkey, "num"));
        print(16, jedis.hgetAll(userkey));
        jedis.hdel(userkey, "sex");
        print(17, jedis.hgetAll(userkey));
        print(18, jedis.hkeys(userkey));
        print(19, jedis.hvals(userkey));
        //如果不存在则新建一个对应
        jedis.hsetnx(userkey, "name", "good");
        jedis.hsetnx(userkey, "fly", "yes");
        print(19, jedis.hgetAll(userkey));

        /**
         * 数据类型之set
         */
        String myset = "myset1";
        String myset2 = "myset2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(myset, String.valueOf(i));
            jedis.sadd(myset2, String.valueOf(i * i));
        }
        print(20, jedis.smembers(myset));
        print(20, jedis.smembers(myset2));
        print(21, jedis.sunion(myset, myset2));
        print(22, jedis.sinter(myset, myset2));
        print(23, jedis.sdiff(myset, myset2));
        print(24, jedis.sismember(myset, "3"));
        jedis.srem(myset, "5");
        print(25, jedis.smembers(myset));
        print(25, jedis.scard(myset));
        /**
         * 数据类型之sortset
         */
        String setKey = "zset";
        jedis.zadd(setKey, 50, "123");
        jedis.zadd(setKey, 60, "play");
        jedis.zadd(setKey, 40, "good");
        print(26, jedis.zrange(setKey, 0, 10));
        print(27, jedis.zrange(setKey, 0, 10));
        print(28, jedis.zlexcount(setKey, "[good", "[play"));
        print(27, jedis.zcount(setKey, 30, 100));
        print(29, jedis.zrange(setKey, 0, 100));
        print(30, jedis.zscore(setKey, "play"));
        print(31, jedis.zrevrange(setKey, 0, 100));
        print(55, jedis.zrank(setKey, "play"));
        for (Tuple tuple : jedis.zrangeByScoreWithScores(setKey, "0", "100")) {
            print(32, tuple.getElement() + "：" + tuple.getScore());
        }
       jedis.srem("good","usre");
        jedis.sadd("good","us");
        jedis.srem("good","us");
        print(56,jedis.scard("good"));
    }
}

package net.bewithu.questioncommunity.Util;

public class RedisKeyProducer {
    private static  String SPILT=":";
    private static  String LIKE="LIKE";
    private static  String DISLIKE="DISLIKE";
    private static  String FLLOWER="FLLOWER";
    private static  String FLLOWEE="FLLOWEE";

    /**
     * 产生redis的Key的实例，保证实例名对应
     * @param entityType
     * @param entityId
     * @return
     */
    //点赞集合Key名
    public  static  String getLikeKey(int entityType,int entityId) {
        return LIKE + SPILT + String.valueOf(entityType) + SPILT + String.valueOf(entityId);
    }
    //踩集合Key名
    public  static  String getDLikeKey(int entityType,int entityId){
        return  DISLIKE+SPILT+String.valueOf(entityType)+SPILT+String.valueOf(entityId);
    }
    //某个实体（问题，用户）关注的fllower的key名
    public  static  String  getFllowers(int entityType,int entityId){
        return FLLOWER+SPILT+String.valueOf(entityType)+SPILT+String.valueOf(entityId);
    }
    //某个人关注的对象（问题，用户）
    public  static  String  getFllowees(int userId,int entityType){
        return FLLOWEE+SPILT+String.valueOf(userId)+SPILT+String.valueOf(entityType);
    }
    //整个系统的事件队列名，各个消费者都从这个里面取自己的事件
    public  static String getEventQueueKey(){
        return  "QUESTIONCOMMUNITY"+SPILT+"EVENTQUEUE";
    }
}

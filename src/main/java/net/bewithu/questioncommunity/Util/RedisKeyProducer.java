package net.bewithu.questioncommunity.Util;

public class RedisKeyProducer {
    private static  String SPILT=":";
    private static  String LIKE="LIKE";
    private static  String DISLIKE="DISLIKE";

    /**
     * 产生redis的Key的实例，保证实例名对应
     * @param entityType
     * @param entityId
     * @return
     */
    public  static  String getLikeKey(int entityType,int entityId) {
        return LIKE + SPILT + String.valueOf(entityType) + SPILT + String.valueOf(entityId);
    }
    /**
     * 产生redis的Key的实例，保证实例名对应
     * @param entityType
     * @param entityId
     * @return
     */
    public  static  String getDLikeKey(int entityType,int entityId){
        return  DISLIKE+SPILT+String.valueOf(entityType)+SPILT+String.valueOf(entityId);
    }
}

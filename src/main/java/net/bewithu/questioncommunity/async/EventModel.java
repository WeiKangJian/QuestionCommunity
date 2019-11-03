package net.bewithu.questioncommunity.async;

import java.util.HashMap;

public class EventModel {
    private  EventType type;
    private  int actorId;
    private  int ownerId;
    private  int entityType;
    private  int entityId;
    private HashMap<String,String> map =new HashMap<>();

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId =actorId;
        return  this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public EventModel setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return  this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return  this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public EventModel setMap(HashMap<String, String> map) {
        this.map = map;
        return this;
    }

    public EventModel setMapValue(String key,String value){
        map.put(key,value);
        return  this;
    }

    public String getMapValue(String key){
       return map.get(key);
    }
}

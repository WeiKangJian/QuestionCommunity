package net.bewithu.questioncommunity.async;

import java.util.List;

public interface EventHandle {
     void doHandle(EventModel eventModel);

     List<EventType> getSupportEventType();
}

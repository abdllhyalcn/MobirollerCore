package com.mobiroller.core.models.events;

public class UserBlockEvent {

    public String uid;
    public boolean isBlocked;
    // 1 from userprofile 2 from chat
    public int from;

    public UserBlockEvent(String uid, boolean isBlocked,int from) {
        this.uid = uid;
        this.from = from;
        this.isBlocked = isBlocked;
    }
}

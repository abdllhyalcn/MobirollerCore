package com.mobiroller.core.models.events;

/**
 * Created by ealtaca on 9/7/17.
 */

public class ChatIsReadEvent {

    private String uid;

    private boolean isRead;
    public String screenId;

    public ChatIsReadEvent(String uid, boolean isRead, String screenId) {
        this.uid = uid;
        this.isRead = isRead;
        this.screenId = screenId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}

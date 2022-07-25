package com.mobiroller.core.models;

import java.io.Serializable;

/**
 * Created by ealtaca on 10.03.2017.
 */

public class TwitterModel implements Serializable{

    String text;
    String id;
    TwitterUserModel user;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TwitterUserModel getUser() {
        return user;
    }

    public void setUser(TwitterUserModel user) {
        this.user = user;
    }
}

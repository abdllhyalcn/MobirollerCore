package com.mobiroller.core.models.events;

/**
 * Created by ealtaca on 7/4/17.
 */

public class ProfileUpdateEvent {
    private String profileImageURL;
    private String userName;

    public ProfileUpdateEvent(String userName) {
        this.userName = userName;
    }

    public ProfileUpdateEvent(String profileImageURL, String userName) {
        this.profileImageURL = profileImageURL;
        this.userName = userName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}

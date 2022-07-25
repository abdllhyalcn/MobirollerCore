package com.mobiroller.core.models;

import java.io.Serializable;

/**
 * Created by ealtaca on 10.03.2017.
 */

public class TwitterUserModel implements Serializable{

    private String profile_image_url;

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }
}

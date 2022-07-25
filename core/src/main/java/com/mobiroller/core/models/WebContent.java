package com.mobiroller.core.models;

import java.io.Serializable;

public class WebContent implements Serializable {

    public String title;
    public String url;
    public boolean shareable;


    public WebContent(String title, String url, boolean shareable) {
        this.title = title;
        this.url = url;
        this.shareable = shareable;
    }
}

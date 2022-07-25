package com.mobiroller.core.models;

import java.io.Serializable;

/**
 * Created by ealtaca on 10/18/17.
 */

public class Audio implements Serializable {

    private String data;
    private String title;
    private boolean isSelected;

    public Audio(String data, String title) {
        this.data = data;
        this.title = title;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
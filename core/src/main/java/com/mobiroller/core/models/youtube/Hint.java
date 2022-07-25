package com.mobiroller.core.models.youtube;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hint implements Serializable
{

    @SerializedName("property")
    @Expose
    private String property;
    @SerializedName("value")
    @Expose
    private String value;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

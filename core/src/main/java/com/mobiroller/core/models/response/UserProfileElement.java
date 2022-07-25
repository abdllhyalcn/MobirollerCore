package com.mobiroller.core.models.response;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class UserProfileElement implements Serializable,Comparable<UserProfileElement>{
    public String id;
    public String type;
    public String subType;
    public String title;
    public boolean mandotory;
    public boolean isActive;
    public int orderIndex;
    public String selections;
    public String value;


    public UserProfileElement(String title, String type, String subType, String value) {
        this.title = title;
        this.type = type;
        this.subType = subType;
        this.value = value;
    }


    @Override
    public int compareTo(@NonNull UserProfileElement o) {
        return(orderIndex - o.orderIndex);
    }

}

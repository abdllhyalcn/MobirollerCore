package com.mobiroller.core.models;

import java.io.Serializable;

public class ApplyzeResponse<T> implements Serializable {

    public boolean success;
    public String key;
    public T data;
    public String message;
    public boolean isUserFriendlyMessage;

}

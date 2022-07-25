package com.mobiroller.core.util.exceptions;

import android.content.Context;

import com.mobiroller.core.util.DialogUtil;

public class UserFriendlyException extends MobirollerException {

    private int messageResource;

    public UserFriendlyException(String message,int messageResource) {
        super(message);
        this.messageResource = messageResource;
    }


    public UserFriendlyException(int messageResource) {
        super();
        this.messageResource = messageResource;
    }


    public int getMessageResource() {
        return messageResource;
    }

    public void setMessageResource(int messageResource) {
        this.messageResource = messageResource;
    }

    public void showDialog(Context context){
        DialogUtil.showDialog(context,context.getString(messageResource));
    }
}

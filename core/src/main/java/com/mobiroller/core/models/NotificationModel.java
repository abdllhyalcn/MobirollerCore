package com.mobiroller.core.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ealtaca on 06.01.2017.
 */

public class NotificationModel implements Serializable {

    private String uniqueId;
    public String title;
    private String message;
    private boolean read;
    private long date;
    private String screenId;
    private String screenType;
    public String subScreenType;
    public String notificationType;
    public String webURL;
    public String displayType;
    public String url;
    public String packageName;
    public String inAppUrl;

    public NotificationModel(String message, String notificationType, String screenId, String screenType, String subScreenType) {
        this.message = message;
        this.screenId = screenId;
        this.screenType = screenType;
        this.subScreenType = subScreenType;
        this.notificationType = notificationType;
        this.read = false;
        this.date = new Date().getTime();
    }

    public NotificationModel() {
        this.read = false;
        this.date = new Date().getTime();
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead() {
        this.read = true;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "uniqueId='" + uniqueId + '\'' +
                ", message='" + message + '\'' +
                ", read=" + read +
                ", date=" + date +
                ", screenId='" + screenId + '\'' +
                ", screenType='" + screenType + '\'' +
                '}';
    }

    public FCMNotificationModel.Action getAction() {
        FCMNotificationModel.Action action = new FCMNotificationModel.Action();
        if (displayType != null)
            action.setDisplayType(displayType);
        if (inAppUrl != null)
            action.setInAppUrl(inAppUrl);
        if (packageName != null)
            action.setPackageName(packageName);
        if (screenType != null)
            action.setScreenType(screenType);
        if (subScreenType != null)
            action.setScreenSubType(subScreenType);
        if (notificationType != null)
            action.setType(notificationType);
        if (url != null)
            action.setUrl(url);
        if (webURL != null)
            action.setWebUrl(webURL);
        if (screenId != null)
            action.setId(screenId);
        return action;
    }
}

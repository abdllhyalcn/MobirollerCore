package com.mobiroller.core.models;

import android.content.Context;
import android.os.Build;

import com.mobiroller.core.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FCMNotificationModel implements Serializable {

    private String id = "";
    private String title = "";
    private String body = "";
    private String alert = "";
    private String accountScreenID = "";
    private String screenType = "";
    private String category = "";
    private String bigPictureUrl = "";
    private String smallIcon = "";
    private String priority = "";
    private String largeIcon = "";
    private String color = "";
    private List<Button> buttons = new ArrayList<>();
    private Action action = new Action();
    public boolean saveNotification = true;


    public String getPriority() {
        if (priority.equals(""))
            return "";
        try {
            int pri = Integer.parseInt(priority);
            if (pri > 5 || pri < 0)
                return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return priority;
        } else {
            int pri = Integer.parseInt(priority);
            return String.valueOf(pri - 3);
        }
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getId() {
        if (id.equals(""))
            id = String.valueOf(getRandomInt());
        return id;
    }

    private int getRandomInt() {
        Random r = new Random();
        int lowerBound = 10;
        int upperBound = 1234567;
        return r.nextInt(upperBound - lowerBound) + lowerBound;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle(Context context) {
        if (title.equals(""))
            title = context.getString(R.string.app_name);
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        if (body.equals("") && !alert.equals(""))
            body = alert;
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getAccountScreenID() {
        return accountScreenID;
    }

    public void setAccountScreenID(String accountScreenID) {
        this.accountScreenID = accountScreenID;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBigPictureUrl() {
        return bigPictureUrl;
    }

    public void setBigPictureUrl(String bigPictureUrl) {
        this.bigPictureUrl = bigPictureUrl;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public class Button implements Serializable {
        private String text;
        private Action action;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Action getAction() {
            return action;
        }

        public void setAction(Action action) {
            this.action = action;
        }

    }

    public static class Action implements Serializable {

        public Action() {
        }

        public Action(String id, String type, String screenType) {
            this.id = id;
            this.type = type;
            this.screenType = screenType;
        }

        private String id;
        private String type;
        private String url;
        private String packageName;
        private String displayType;
        private String webUrl;
        private String inAppUrl;
        private String screenSubType;
        private String screenType;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDisplayType() {
            return displayType;
        }

        public void setDisplayType(String displayType) {
            this.displayType = displayType;
        }

        public String getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }

        public String getInAppUrl() {
            return inAppUrl;
        }

        public void setInAppUrl(String inAppUrl) {
            this.inAppUrl = inAppUrl;
        }

        public String getScreenSubType() {
            return screenSubType;
        }

        public void setScreenSubType(String screenSubType) {
            this.screenSubType = screenSubType;
        }

        public String getScreenType() {
            return screenType;
        }

        public void setScreenType(String screenType) {
            this.screenType = screenType;
        }

    }



}


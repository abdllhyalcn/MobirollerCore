package com.mobiroller.core.models;

import java.io.Serializable;

/**
 * Created by ealtaca on 07.03.2017.
 */

public class IntroModel implements Serializable {

    private String introMessage;
    private String introMessageId;
    private String introMessageScreenType;
    public String introMessageSubScreenType;
    private String introMessageScreenID;
    private String updateDate;
    private String activeLanguages;

    public String getIntroMessage() {
        return introMessage;
    }

    public void setIntroMessage(String introMessage) {
        this.introMessage = introMessage;
    }

    public String getIntroMessageId() {
        return introMessageId;
    }

    public void setIntroMessageId(String introMessageId) {
        this.introMessageId = introMessageId;
    }

    public String getIntroMessageScreenType() {
        return introMessageScreenType;
    }

    public void setIntroMessageScreenType(String introMessageScreenType) {
        this.introMessageScreenType = introMessageScreenType;
    }

    public String getIntroMessageScreenID() {
        return introMessageScreenID;
    }

    public void setIntroMessageScreenID(String introMessageScreenID) {
        this.introMessageScreenID = introMessageScreenID;
    }

    public String getUpdateDate() {
        if(updateDate==null)
            return "";
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getActiveLanguages() {
        return activeLanguages;
    }

    public void setActiveLanguages(String activeLanguages) {
        this.activeLanguages = activeLanguages;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntroModel))
            return false;
        IntroModel rhs = (IntroModel) obj;
        return getUpdateDate().equalsIgnoreCase(rhs.getUpdateDate());
    }
}

package com.mobiroller.core.coreui.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ealtaca on 07.03.2017.
 */

public class TableItemsModel implements Serializable {

    //youtube items start
    private String youtubeURL;
    private String subtitle;
    private String thumb;
    private String duration;
    //youtube items end

    //youtube,mp3view and forms shares
    private String title;

    // aveHtmlView, emergency view phone number
    private String phoneNumber;

    //form items start
    private String id;
    private String type;
    private String value;
    private String align;
    private String ratingLevel;
    private String lineCount;
    private String mandatory;
    //form items end

    //mp3 items start
    private String fileURL;
    private String linkURL;
    //mp3 items end

    //MenuView
    private ImageModel imageName;
    private String accountScreenID;
    private String screenType;
    public String screenSubType;
    private boolean isLoginActive;
    private ArrayList<String> roles;
    private String items;

    private String question;
    private String answer;
    private String orderIndex;

    private String updateDate;

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getYoutubeURL() {
        return youtubeURL;
    }

    public void setYoutubeURL(String youtubeURL) {
        this.youtubeURL = youtubeURL;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        if(value==null)
            return "";
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getRatingLevel() {
        return ratingLevel;
    }

    public void setRatingLevel(String ratingLevel) {
        this.ratingLevel = ratingLevel;
    }

    public String getLineCount() {
        return lineCount;
    }

    public void setLineCount(String lineCount) {
        this.lineCount = lineCount;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getFileURL() {
        if(linkURL!=null)
            return linkURL;
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    public ImageModel getImageName() {
        return imageName;
    }

    public void setImageName(ImageModel imageName) {
        this.imageName = imageName;
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

    public boolean isLoginActive() {
        return isLoginActive;
    }

    public void setLoginActive(boolean loginActive) {
        isLoginActive = loginActive;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }
}

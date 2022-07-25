package com.mobiroller.core.models;

import com.mobiroller.core.coreui.models.ImageModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ealtaca on 08.03.2017.
 */

public class NavigationItemModel implements Serializable{

    private String accountScreenID;
    private String screenType;
    public String screenSubtype;
    private String title;
    private String updateDate;
    private ImageModel iconImage;
    private boolean isLoginActive;
    private ArrayList<String> roles;


    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageModel getIconImage() {
        return iconImage;
    }

    public void setIconImage(ImageModel iconImage) {
        this.iconImage = iconImage;
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

package com.mobiroller.core.models;

import com.mobiroller.core.coreui.models.ColorModel;
import com.mobiroller.core.coreui.models.ImageModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ealtaca on 08.03.2017.
 */

public class NavigationModel implements Serializable {

    private String updateDate;
    private int type;
    public int menuType;
    public int subMenuType;
    private ImageModel backgroundImage;
    private String fontName;
    private int numberOfRows;
    private int numberOfColumns;
    private ImageModel itemBackgroundImage;
    private ColorModel navBarTintColor;
    private ColorModel menuTextColor;
    private ColorModel menuBackgroundColor;
    private boolean isLoginActive;
    private boolean isRegistrationActive;
    private boolean isAppStartLogin;
    private ArrayList<NavigationItemModel> navigationItems;

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ImageModel getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(ImageModel backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public ImageModel getItemBackgroundImage() {
        return itemBackgroundImage;
    }

    public void setItemBackgroundImage(ImageModel itemBackgroundImage) {
        this.itemBackgroundImage = itemBackgroundImage;
    }

    public ColorModel getNavBarTintColor() {
        return navBarTintColor;
    }

    public void setNavBarTintColor(ColorModel navBarTintColor) {
        this.navBarTintColor = navBarTintColor;
    }

    public ColorModel getMenuTextColor() {
        return menuTextColor;
    }

    public void setMenuTextColor(ColorModel menuTextColor) {
        this.menuTextColor = menuTextColor;
    }

    public ColorModel getMenuBackgroundColor() {
        return menuBackgroundColor;
    }

    public void setMenuBackgroundColor(ColorModel menuBackgroundColor) {
        this.menuBackgroundColor = menuBackgroundColor;
    }

    public boolean isLoginActive() {
        return isLoginActive;
    }

    public void setLoginActive(boolean loginActive) {
        isLoginActive = loginActive;
    }

    public boolean isRegistrationActive() {
        return isRegistrationActive;
    }

    public void setRegistrationActive(boolean registrationActive) {
        isRegistrationActive = registrationActive;
    }

    public boolean isAppStartLogin() {
        return isAppStartLogin;
    }

    public void setAppStartLogin(boolean appStartLogin) {
        isAppStartLogin = appStartLogin;
    }

    public ArrayList<NavigationItemModel> getNavigationItems() {
        return navigationItems;
    }

    public void setNavigationItems(ArrayList<NavigationItemModel> navigationItems) {
        this.navigationItems = navigationItems;
    }

    public ImageModel getBackgroundImageName() {
        return backgroundImage;
    }


    public ImageModel getTableCellBackground() {
        return itemBackgroundImage;
    }

    public NavigationModel getConfiguredNavigationModel() {
        if (menuType == 0 && subMenuType == 0) {
            if (getType() == 0) {
                menuType = 1;
            } else if (getType() == 1) {
                menuType = 2;
            } else if (getType() == 2 && getNumberOfRows() == 6 && getNumberOfColumns() == 0) {
                menuType = 3;
                subMenuType = 2;
            } else if (getType() == 2) {
                menuType = 3;
                subMenuType = 1;
            } else if (getType() == 3) {
                menuType = 4;
            } else {
                menuType = 3;
                subMenuType = 2;
            }
        }
        return this;
    }

}

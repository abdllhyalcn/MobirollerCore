package com.mobiroller.core.models;

import com.google.gson.annotations.SerializedName;
import com.mobiroller.core.coreui.models.ImageModel;

import java.io.Serializable;

/**
 * Created by ealtaca on 21.03.2017.
 */

public class AccountModel implements Serializable {

    private String accountName;
    private String title;
    private ImageModel logo;
    @SerializedName("package")
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageModel getLogo() {
        return logo;
    }

    public void setLogo(ImageModel logo) {
        this.logo = logo;
    }


    @Override
    public String toString() {
        return "AccountModel{" +
                "accountName='" + accountName + '\'' +
                ", title='" + title + '\'' +
                ", logo=" + logo +
                '}';
    }
}

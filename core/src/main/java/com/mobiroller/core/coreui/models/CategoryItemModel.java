package com.mobiroller.core.coreui.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ealtaca on 09.03.2017.
 */

public class CategoryItemModel implements Serializable{

    private String itemTitle;
    private String itemSubTitle;
    private String itemDescription;
    private String itemLink;
    private String currency;
    private String itemCreateDate;
    private String itemPrice;
    private String button;
    private ArrayList<String> tableImages;

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemSubTitle() {
        return itemSubTitle;
    }

    public void setItemSubTitle(String itemSubTitle) {
        this.itemSubTitle = itemSubTitle;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getItemCreateDate() {
        return itemCreateDate;
    }

    public void setItemCreateDate(String itemCreateDate) {
        this.itemCreateDate = itemCreateDate;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public ArrayList<String> getTableImages() {
        return tableImages;
    }

    public void setTableImages(ArrayList<String> tableImages) {
        this.tableImages = tableImages;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
}

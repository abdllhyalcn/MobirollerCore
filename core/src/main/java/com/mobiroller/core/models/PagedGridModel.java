package com.mobiroller.core.models;

import java.util.ArrayList;

/**
 * Created by ealtaca on 26.12.2016.
 */

public class PagedGridModel {

    private ArrayList<NavigationItemModel> navItems;
    private int numOfRows;
    private int numOfColumns;
    private int[] idList;
    private String[] typeList;
    private Boolean[] loginActiveList;
    private int stParam;
    private int color;

    public PagedGridModel() {
    }

    public PagedGridModel(ArrayList<NavigationItemModel> navItems, int numOfRows, int numOfColumns, int[] idList, String[] typeList, Boolean[] loginActiveList, int stParam, int color) {
        this.navItems = navItems;
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
        this.idList = idList;
        this.typeList = typeList;
        this.loginActiveList = loginActiveList;
        this.stParam = stParam;
        this.color = color;
    }

    public PagedGridModel(ArrayList<NavigationItemModel> navItems, int numOfRows, int numOfColumns, int stParam, int color) {
        this.navItems = navItems;
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
        this.stParam = stParam;
        this.color = color;
    }



    public int getElementPerPage() {
        return numOfColumns*numOfRows;
    }


    public ArrayList<NavigationItemModel> getNavItems() {
        if(navItems == null)
            return navItems = new ArrayList<>();
        return navItems;
    }

    public void setNavItems(ArrayList<NavigationItemModel> navItems) {
        this.navItems = navItems;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public int getNumOfColumns() {
        return numOfColumns;
    }

    public void setNumOfColumns(int numOfColumns) {
        this.numOfColumns = numOfColumns;
    }

    public int[] getIdList() {
        return idList;
    }

    public void setIdList(int[] idList) {
        this.idList = idList;
    }

    public String[] getTypeList() {
        return typeList;
    }

    public void setTypeList(String[] typeList) {
        this.typeList = typeList;
    }

    public Boolean[] getLoginActiveList() {
        return loginActiveList;
    }

    public void setLoginActiveList(Boolean[] loginActiveList) {
        this.loginActiveList = loginActiveList;
    }

    public int getStParam() {
        return stParam;
    }

    public void setStParam(int stParam) {
        this.stParam = stParam;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

package com.mobiroller.core.models;

import java.util.ArrayList;

/**
 * Created by ealtaca on 12.04.2017.
 */

public class RssSliderHeaderModel {

    private ArrayList<Object> dataList;

    public RssSliderHeaderModel(ArrayList<Object> dataList) {
        this.dataList = dataList;
    }

    public ArrayList<Object> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Object> dataList) {
        this.dataList = dataList;
    }
}

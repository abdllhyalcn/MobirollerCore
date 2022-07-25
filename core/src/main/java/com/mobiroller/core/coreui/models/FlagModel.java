package com.mobiroller.core.coreui.models;

import java.io.Serializable;

/**
 * Created by ealtaca on 07.03.2017.
 */

public class FlagModel implements Serializable {

    private String lattitude;
    private String longitude;
    private String title;
    private String detail;

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

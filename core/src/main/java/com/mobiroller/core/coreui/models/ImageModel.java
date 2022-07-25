package com.mobiroller.core.coreui.models;

import java.io.Serializable;

/**
 * Created by ealtaca on 07.03.2017.
 */

public class ImageModel implements Serializable {

    private String imageURL;

    public ImageModel(String imageURL) {
        this.imageURL = imageURL;
    }

    public ImageModel() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

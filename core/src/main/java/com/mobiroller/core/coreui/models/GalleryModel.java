package com.mobiroller.core.coreui.models;

import java.io.File;
import java.io.Serializable;

/**
 * Created by ealtaca on 30.01.2017.
 */

public class GalleryModel implements Serializable {

    private String URL;
    private String caption;
    private File file;

    public GalleryModel(File file) {
        this.file = file;
    }

    public GalleryModel(String imageURL, String caption) {
        this.URL = imageURL;
        this.caption = caption;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}

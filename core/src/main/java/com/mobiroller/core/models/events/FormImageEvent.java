package com.mobiroller.core.models.events;

import android.net.Uri;

public class FormImageEvent {

    int id;
    Uri imagePath;

    public FormImageEvent(int id, Uri imagePath) {
        this.id = id;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public Uri getImagePath() {
        return imagePath;
    }
}

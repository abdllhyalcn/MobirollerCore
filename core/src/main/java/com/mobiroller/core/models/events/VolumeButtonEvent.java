package com.mobiroller.core.models.events;

import android.view.KeyEvent;

/**
 * Created by ealtaca on 12/5/17.
 */

public class VolumeButtonEvent {

    KeyEvent keyEvent;
    int keyCode;

    public VolumeButtonEvent(KeyEvent keyEvent, int keyCode) {
        this.keyEvent = keyEvent;
        this.keyCode = keyCode;
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    public void setKeyEvent(KeyEvent keyEvent) {
        this.keyEvent = keyEvent;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}

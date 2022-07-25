package com.mobiroller.core.models.events;

/**
 * Created by ealtaca on 6/23/17.
 */

public class LoginEvent {

    public String screenId;

    public LoginEvent(String screenId) {
        if (screenId != null)
            this.screenId = screenId;
    }

    public LoginEvent() {
    }
}

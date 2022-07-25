package com.mobiroller.core.util.exceptions;

import android.content.Intent;

public class IntentException extends MobirollerException {

    Intent intent;

    public IntentException(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}

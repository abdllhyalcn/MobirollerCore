package com.mobiroller.core.services;

import android.app.Activity;
import android.os.Bundle;

public class RestoreAppActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Now finish, which will drop the user in to the activity that was at the top
        //  of the task stack
        finish();
    }
}
package com.mobiroller.core.activities.preview;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.util.PreviewUtil;

public class PreviewNotificationHandler extends AppCompatActivity {

    LocalizationHelper localizationHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
        localizationHelper = new LocalizationHelper();
        if (SharedApplication.isAppKilled()) {
            Intent intent = new Intent(this, SharedApplication.app.getSplashClass());
            if (getIntent().hasExtra(Constants.MobiRoller_Preview_Notification_Model)) {
                intent.putExtra(Constants.MobiRoller_Preview_Notification_Model, getIntent().getSerializableExtra(Constants.MobiRoller_Preview_Notification_Model));
            }
            startActivity(intent);
            finish();
        } else {
            PreviewUtil.openWebFromNotification(this);
            finish();
        }
    }
}

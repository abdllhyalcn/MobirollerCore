package com.mobiroller.core.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobiroller.core.activities.base.AveActivity;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.interfaces.ActivityComponent;
import com.mobiroller.core.R;

import javax.inject.Inject;


/**
 * Created by ealtaca on 06.01.2017.
 */

public class aveShareView extends AveActivity {

    @Inject
    LegacyToolbarHelper toolbarHelper;
    @Inject
    LocalizationHelper localizationHelper;

    private String contentText;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenModel = JSONStorage.getScreenModel(screenId);
        if (screenModel.getContentText() != null) {
            this.contentText = localizationHelper.getLocalizedTitle(screenModel.getContentText());
            this.url = screenModel.getGooglePlayLink();
            shareApp();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.common_error), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public AppCompatActivity injectActivity(ActivityComponent component) {
        component.inject(this);
        return this;
    }

    private void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        if (url == null)
            url = "";
        if (contentText == null)
            contentText = "";
        sendIntent.putExtra(Intent.EXTRA_TEXT, this.contentText + "  " + this.url);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.action_share)));
        finish();
    }
}

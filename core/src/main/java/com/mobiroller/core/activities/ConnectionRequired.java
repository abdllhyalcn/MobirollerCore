package com.mobiroller.core.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.mobiroller.core.R2;
import com.mobiroller.core.activities.base.AveActivity;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.helpers.JSONParser;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.helpers.NetworkHelper;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.helpers.ScreenHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.interfaces.ActivityComponent;
import com.mobiroller.core.R;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ealtaca on 6/5/17.
 */

public class ConnectionRequired extends AveActivity {

    @Inject
    NetworkHelper networkHelper;
    @Inject
    JSONParser jsonParser;

    private LegacyProgressViewHelper legacyProgressViewHelper;

    @Inject
    LegacyToolbarHelper toolbarHelper;
    @Inject
    SharedPrefHelper sharedPrefHelper;
    @Inject
    ScreenHelper screenHelper;

    @BindView(R2.id.try_again)
    Button try_again;
    @BindView(R2.id.wifi_check)
    Button wifiCheck;
    @BindView(R2.id.mobile_check)
    Button mobileCheck;

    @BindView(R2.id.connection_main_layout)
    LinearLayout mainLayout;

    private Intent intent;
    private String screenType;
    private String subScreenType;
    private String screenId;
    private String updateDate;
    private ArrayList<String> roles = new ArrayList<>();
    private boolean hasScreenModel = true;

    private Snackbar snackbar;
    private boolean hideTryAgain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_required_layout);
        ButterKnife.bind(this);
        toolbarHelper.setToolbarTitle(this, "");
        setBackgroundColor();
        legacyProgressViewHelper = new LegacyProgressViewHelper(this);
//        bundle = getIntent().getExtras();
        if (getIntent().hasExtra("intent"))
            intent = getIntent().getParcelableExtra("intent");
        else {
            hasScreenModel = false;
        }
        if (getIntent().hasExtra(Constants.KEY_SCREEN_ID))
            screenId = getIntent().getStringExtra(Constants.KEY_SCREEN_ID);
        if (getIntent().hasExtra(Constants.KEY_SCREEN_TYPE))
            screenType = getIntent().getStringExtra(Constants.KEY_SCREEN_TYPE);
        if (getIntent().hasExtra(Constants.KEY_SUB_SCREEN_TYPE))
            subScreenType = getIntent().getStringExtra(Constants.KEY_SUB_SCREEN_TYPE);
        if (getIntent().hasExtra(Constants.KEY_UPDATE_DATE))
            updateDate = getIntent().getStringExtra(Constants.KEY_UPDATE_DATE);
        if (getIntent().hasExtra(Constants.KEY_SCREEN_ROLES))
            roles = getIntent().getStringArrayListExtra(Constants.KEY_SCREEN_ROLES);

        if(intent == null && screenId == null)
            hideTryAgain = true;

        if(hideTryAgain)
            try_again.setVisibility(View.GONE);

        snackbar = Snackbar
                .make(mainLayout, getString(R.string.connection_required_error), Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.OK), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
    }

    public void setBackgroundColor() {
        float darkFactor = 0.5f;
        float lightFactor = 0f;
        mainLayout.setBackground(
                ScreenHelper.getGradientBackground(
                        sharedPrefHelper.getActionBarColor(),
                        lightFactor,
                        darkFactor
                )
        );
    }


    @Override
    public AppCompatActivity injectActivity(ActivityComponent component) {
        component.inject(this);
        return this;
    }

    @OnClick(R2.id.try_again)
    public void tryAgain() {
        if (networkHelper.isConnected()) {
            if (hasScreenModel && intent != null) {
                startActivity(intent);
                finish();
            } else {
                if (getIntent().hasExtra(Constants.KEY_SCREEN_RSS_PUSH_TITLE))
                    ActivityHandler.startActivity(this, screenId, screenType, subScreenType, updateDate, roles, getIntent().getStringExtra(Constants.KEY_SCREEN_RSS_PUSH_TITLE));
                else
                    ActivityHandler.startActivity(this, screenId, screenType, subScreenType, updateDate, roles);
            }

        } else {
            snackbar.show();
        }
    }

    @OnClick(R2.id.wifi_check)
    public void openWifiSettings() {
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    @OnClick(R2.id.mobile_check)
    public void openMobileSettings() {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }

}

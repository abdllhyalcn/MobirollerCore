package com.mobiroller.core.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobiroller.core.R2;
import com.mobiroller.core.activities.base.AveActivity;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.helpers.ScreenHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.interfaces.ActivityComponent;
import com.mobiroller.core.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PermissionRequiredActivity extends AveActivity {

    @Inject
    LegacyToolbarHelper toolbarHelper;
    @Inject
    SharedPrefHelper sharedPrefHelper;
    @Inject
    ScreenHelper screenHelper;

    @BindView(R2.id.try_again)
    Button try_again;
    @BindView(R2.id.permission_check)
    Button permissionCheck;
    @BindView(R2.id.connection_main_layout)
    LinearLayout mainLayout;
    @BindView(R2.id.permission_description)
    TextView description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_required_layout);
        ButterKnife.bind(this);
        toolbarHelper.setToolbarTitlePermission(this, "");
        setBackgroundColor();
        legacyProgressViewHelper = new LegacyProgressViewHelper(this);
        description.setText(Html.fromHtml(getString(R.string.permission_required_description, getString(R.string.app_name))));

    }


    public void setBackgroundColor() {
        float darkFactor = 0.5f;
        float lightFactor = 0f;
        mainLayout.setBackground(
                ScreenHelper.getGradientBackground(
                        Color.parseColor("#517fff"),
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

    @OnClick(R2.id.permission_check)
    public void openPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @OnClick(R2.id.try_again)
    public void restartApp() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}

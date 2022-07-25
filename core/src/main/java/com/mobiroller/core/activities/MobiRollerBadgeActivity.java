package com.mobiroller.core.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mobiroller.core.R2;
import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.models.MobirollerBadgeModel;
import com.mobiroller.core.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MobiRollerBadgeActivity extends AppCompatActivity {

    private MobirollerBadgeModel mMobirollerBadge;

    @BindView(R2.id.web_view_layout)
    ConstraintLayout webViewLayout;

    @BindView(R2.id.design_layout)
    ConstraintLayout designLayout;

    @BindView(R2.id.web_view)
    WebView webView;

    @BindView(R2.id.description)
    TextView description;

    @BindView(R2.id.button_text)
    TextView buttonText;

    private String mWebUrl = "https://mobiroller.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobiroller_badge_layout);
        ButterKnife.bind(this);
        mMobirollerBadge = JSONStorage.getMobirollerBadgeModel();
        loadUi();
    }

    private void loadUi() {
        if (mMobirollerBadge != null) {
            if (mMobirollerBadge.displayUrl != null) {
                if(mMobirollerBadge.displayUrl.contains("{package_name}"))
                    mMobirollerBadge.displayUrl = mMobirollerBadge.displayUrl.replace("{package_name}",getPackageName());
                webView.loadUrl(mMobirollerBadge.displayUrl);
                webViewLayout.setVisibility(View.VISIBLE);
                designLayout.setVisibility(View.GONE);
            } else if (mMobirollerBadge.design != null) {
                if (mMobirollerBadge.design.buttonText != null)
                    buttonText.setText(mMobirollerBadge.design.buttonText);
                if (mMobirollerBadge.design.content != null)
                    description.setText(mMobirollerBadge.design.content);
                if (mMobirollerBadge.design.launchUrl != null) {
                    if(mMobirollerBadge.design.launchUrl.contains("{package_name}"))
                        mMobirollerBadge.design.launchUrl = mMobirollerBadge.design.launchUrl.replace("{package_name}",getPackageName());
                    mWebUrl = URLUtil.guessUrl(mMobirollerBadge.design.launchUrl);
                }
            }
        }
    }

    @OnClick(R2.id.button)
    public void onClickGoButton() {
        Bundle bundle = new Bundle();
        bundle.putString("app_name", getString(R.string.app_name));
        bundle.putString("package", getPackageName());
        FirebaseAnalytics.getInstance(this).logEvent("badge_detail_click", bundle);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Theme.primaryColor);
        builder.setShowTitle(true);
        builder.addDefaultShareMenuItem();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(MobiRollerBadgeActivity.this, Uri.parse(mWebUrl));
    }

    @OnClick(R2.id.close_button)
    public void onClickCloseButton()
    {
        onBackPressed();
    }


}

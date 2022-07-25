package com.mobiroller.core.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobiroller.core.activities.base.AveActivity;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.fragments.aveWebViewFragment;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.interfaces.ActivityComponent;
import com.mobiroller.core.models.WebContent;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ShareUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class aveWebView extends AveActivity {

    @Inject
    LegacyToolbarHelper toolbarHelper;
    @Inject
    LocalizationHelper localizationHelper;
    private aveWebViewFragment webViewFragment;
    private boolean isPreviewSignUp = false;
    private WebContent webContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_with_toolbar);
        ButterKnife.bind(this);

        setMobirollerToolbar(findViewById(R.id.toolbar_top));
        new LegacyToolbarHelper().setStatusBar(this);
        if (getIntent().hasExtra("isPreviewSignUp"))
            isPreviewSignUp = true;
        if (getIntent().hasExtra(Constants.NOTIFICATION_TYPE_WEB_CONTENT))
            webContent = (WebContent) getIntent().getSerializableExtra(Constants.NOTIFICATION_TYPE_WEB_CONTENT);
        if (getIntent().hasExtra("webContent"))
            webContent = (WebContent) getIntent().getSerializableExtra("webContent");
        Bundle bundle = new Bundle();
        if (screenId != null && screenModel != null) {
            JSONStorage.putScreenModel(screenId, screenModel);
            bundle.putString(Constants.KEY_SCREEN_TYPE, screenType);
            bundle.putString(Constants.KEY_SCREEN_ID, screenId);
        } else if (webContent != null) {
            bundle.putSerializable(Constants.NOTIFICATION_TYPE_WEB_CONTENT, webContent);
        }

        setToolbar();
        bundle.putBoolean("isPreviewSignUp", isPreviewSignUp);
        webViewFragment = new aveWebViewFragment();
        webViewFragment.setArguments(bundle);
        setFragment(webViewFragment, "webViewFragment");
    }

    @Override
    public AppCompatActivity injectActivity(ActivityComponent component) {
        component.inject(this);
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            if (screenModel != null)
                ShareUtil.shareURL(aveWebView.this, screenModel.getTitle(), screenModel.getURL());
            else if (webContent != null)
                ShareUtil.shareURL(aveWebView.this, webContent.title, webContent.url);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (screenModel != null)
            return screenModel.isRssContent();
        else if (webContent != null)
            return webContent.shareable;
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if ((screenModel != null && screenModel.isRssContent()) || webContent != null && webContent.shareable)
            getToolbar().inflateMenu(R.menu.share_menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (webViewFragment != null) {
            webViewFragment.onActivityResult(requestCode, resultCode, intent);
        }
    }

    private void setToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar_top);
        if (screenModel != null) {
            if (screenModel.isRssContent()) {
                toolbarHelper.setToolbarTitle(this, screenModel.getTitle());
                myToolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
            } else {
                if (screenModel.isHideToolbar()) {
                    myToolbar.setVisibility(View.GONE);
                } else {
                    toolbarHelper.setToolbarTitle(this, localizationHelper.getLocalizedTitle(screenModel.getTitle()));
                    myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
                }
            }
        } else if (webContent != null) {
            toolbarHelper.setToolbarTitle(this, webContent.title);
            myToolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        } else {
            toolbarHelper.setToolbarTitle(this, "");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        webViewFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
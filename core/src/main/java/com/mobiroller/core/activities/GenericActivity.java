package com.mobiroller.core.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.mobiroller.core.R2;
import com.mobiroller.core.activities.base.AveActivity;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.views.legacy.MobirollerSwipeRefreshLayout;
import com.mobiroller.core.fragments.BackHandledFragment;
import com.mobiroller.core.fragments.avePDFViewFragment;
import com.mobiroller.core.helpers.FragmentHandlerHelper;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.interfaces.ActivityComponent;
import com.mobiroller.core.models.events.ShowToolbarEvent;
import com.mobiroller.core.R;
import com.mobiroller.core.coreui.views.legacy.MobirollerToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenericActivity extends AveActivity implements BackHandledFragment.BackHandlerInterface {

    @Inject
    LegacyToolbarHelper toolbarHelper;
    @Inject
    LocalizationHelper localizationHelper;
    @BindView(R2.id.swipe_refresh_layout)
    MobirollerSwipeRefreshLayout swipeRefreshLayout;

    Fragment fragment;
    MobirollerToolbar toolbar;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_with_toolbar);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolbar_top);
        setMobirollerToolbar(toolbar);
        mAppBarLayout = findViewById(R.id.app_bar_layout);

        if (screenModel != null) {
            toolbarHelper.setToolbarTitle(this, localizationHelper.getLocalizedTitle(screenModel.getTitle()));
        } else {
            toolbarHelper.setToolbarTitle(this, "");
        }
        if (getIntent().hasExtra("pdf_url")) {
            bundle = new Bundle();
            bundle.putString("pdf_url",getIntent().getStringExtra("pdf_url"));
            fragment = new avePDFViewFragment();
            fragment.setArguments(bundle);
            setFragment(fragment,"Fragment_");
        } else {
            bundle = new Bundle();
                toolbarHelper.setStatusBar(this);
            JSONStorage.putScreenModel(screenId,screenModel);
            fragment = FragmentHandlerHelper.getFragment(screenType, subScreenType);
            setBundleAndStartFragment(fragment);
        }

    }

    private void setBundleAndStartFragment(Fragment fragment) {
        bundle.putString(Constants.KEY_SCREEN_TYPE, screenType);
        bundle.putString(Constants.KEY_SUB_SCREEN_TYPE, subScreenType);
        bundle.putString(Constants.KEY_SCREEN_ID, screenId);
        bundle.putString(Constants.TOURVISIO_MOBIROLLER_USER_ID, UtilManager.sharedPrefHelper().getUserId());
        fragment.setArguments(bundle);
        setFragment(fragment, screenType + "_Fragment_" + screenId);
    }

    @Override
    public AppCompatActivity injectActivity(ActivityComponent component) {
        component.inject(this);
        return this;
    }

    @Override
    public void setSelectedFragment(BackHandledFragment backHandledFragment) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe
    public void onPostShowToolbarEvent(ShowToolbarEvent e) {
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        toolbarHelper.setStatusBar(this);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public MobirollerSwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

}

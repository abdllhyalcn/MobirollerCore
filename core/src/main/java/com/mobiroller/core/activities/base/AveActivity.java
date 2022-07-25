package com.mobiroller.core.activities.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.appbar.AppBarLayout;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.coreui.activity.CoreActivity;
import com.mobiroller.core.helpers.FontSizeHelper;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.interfaces.ActivityComponent;
import com.mobiroller.core.interfaces.DaggerActivityComponent;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.models.events.MobirollerBadgeEvent;
import com.mobiroller.core.models.events.VolumeButtonEvent;
import com.mobiroller.core.module.ActivityModule;
import com.mobiroller.core.R;
import com.mobiroller.core.util.AnalyticsUtil;
import com.mobiroller.core.views.MobiRollerBadgeView;
import com.mobiroller.core.coreui.views.legacy.MobirollerToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.mobiroller.core.constants.Constants.MobiRoller_Preferences_MobiRoller_Badge_Check_Response;
import static com.mobiroller.core.constants.Constants.MobiRoller_Preferences_MobiRoller_Badge_First_Open;
import static com.mobiroller.core.constants.Constants.MobiRoller_Preferences_Show_MobiRoller_Badge;

public abstract class AveActivity extends CoreActivity {

    public LegacyProgressViewHelper legacyProgressViewHelper;
    private ActivityComponent mActivityComponent;

    public ScreenModel screenModel;
    public String screenId;
    public String screenType;
    public String subScreenType;
    private MobirollerToolbar mToolbar;
    public AppBarLayout mAppBarLayout;
    public ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Theme.primaryColor == 0) {
            Theme.primaryColor = UtilManager.sharedPrefHelper().getActionBarColor();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (this.getClass().getSimpleName().equalsIgnoreCase("aveRssContentViewPager")) {
            int contentOrder = new FontSizeHelper(this).getContentFontOrder();
            if (contentOrder != -1) {
                getTheme().applyStyle(new FontSizeHelper(this).getContentFontStyle().getResId(), true);
            } else {
                getTheme().applyStyle(new FontSizeHelper(this).getFontStyle().getResId(), true);
            }
        } else
            getTheme().applyStyle(new FontSizeHelper(this).getFontStyle().getResId(), true);
        super.onCreate(savedInstanceState);
        legacyProgressViewHelper = new LegacyProgressViewHelper(this);
        ActivityComponent appComponent = activityComponent();
        injectActivity(appComponent);
        if (getIntent().hasExtra(Constants.KEY_SCREEN_ID))
            screenId = getIntent().getStringExtra(Constants.KEY_SCREEN_ID);
        if (getIntent().hasExtra(Constants.KEY_SCREEN_TYPE))
            screenType = getIntent().getStringExtra(Constants.KEY_SCREEN_TYPE);
        if (getIntent().hasExtra(Constants.KEY_SUB_SCREEN_TYPE))
            subScreenType = getIntent().getStringExtra(Constants.KEY_SUB_SCREEN_TYPE);
        if (screenId != null && JSONStorage.containsScreen(screenId))
            screenModel = JSONStorage.getScreenModel(screenId);
        checkStats();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        addMobiRollerBadge();
    }

    public abstract AppCompatActivity injectActivity(ActivityComponent component);

    protected final ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .appComponent(SharedApplication.getMainComponent())
                    .build();
        }
        return mActivityComponent;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (EventBus.getDefault().hasSubscriberForEvent(VolumeButtonEvent.class))
            EventBus.getDefault().post(new VolumeButtonEvent(event, keyCode));
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (legacyProgressViewHelper != null)
                legacyProgressViewHelper.dismiss();
        }

        return super.onKeyDown(keyCode, event);
    }

    protected void onPause() {
        super.onPause();
        if (legacyProgressViewHelper != null)
            legacyProgressViewHelper.dismiss();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    public void setFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.commit();
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void checkStats() {
        try {
            if (screenId != null && screenModel != null) {
                AnalyticsUtil.sendStats(this, screenModel, screenType);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    @Subscribe
    public void onPostMobirollerBadgeEvent(MobirollerBadgeEvent event) {
        addMobiRollerBadge();
    }

    /**
     * Add mobiroller badge to root view
     */
    private void addMobiRollerBadge() {
        if (UtilManager.sharedPrefHelper().getBoolean(MobiRoller_Preferences_Show_MobiRoller_Badge) && isValidActivityForBadge())
            MobiRollerBadgeView.addView(this);
        else
            MobiRollerBadgeView.removeView(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (UtilManager.sharedPrefHelper().getBoolean(MobiRoller_Preferences_Show_MobiRoller_Badge) && isValidActivityForBadge())
            MobiRollerBadgeView.addView(this);
    }

    private boolean isValidActivityForBadge() {
        if (!UtilManager.sharedPrefHelper().getBoolean(MobiRoller_Preferences_MobiRoller_Badge_Check_Response, false)) {
            return false;
        }
        if (this.getClass().getSimpleName().equalsIgnoreCase("SplashApp")) {
            if (UtilManager.sharedPrefHelper().getInt(Constants.MobiRoller_Preferences_MobiRoller_Badge_Total_Count) < UtilManager.sharedPrefHelper().getInt(Constants.MobiRoller_Preferences_MobiRoller_Badge_Count))
                UtilManager.sharedPrefHelper().put(Constants.MobiRoller_Preferences_MobiRoller_Badge_Count, 0);
            UtilManager.sharedPrefHelper().incrementInt(Constants.MobiRoller_Preferences_MobiRoller_Badge_Count);
            return false;
        }
        if (UtilManager.sharedPrefHelper().getBoolean(MobiRoller_Preferences_MobiRoller_Badge_First_Open, true)) {
            UtilManager.sharedPrefHelper().put(MobiRoller_Preferences_MobiRoller_Badge_First_Open, false);
            return true;
        }
        if (UtilManager.sharedPrefHelper().getInt(Constants.MobiRoller_Preferences_MobiRoller_Badge_Total_Count) == 0)
            return true;
        return (UtilManager.sharedPrefHelper().getInt(Constants.MobiRoller_Preferences_MobiRoller_Badge_Total_Count) == UtilManager.sharedPrefHelper().getInt(Constants.MobiRoller_Preferences_MobiRoller_Badge_Count));
    }

    public void setMobirollerToolbar(MobirollerToolbar toolbar) {
        mToolbar = toolbar;
    }

    public MobirollerToolbar getToolbar() {
        return mToolbar;
    }

    public AppBarLayout getAppBarLayout() {
        return mAppBarLayout;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }
}

package com.mobiroller.core;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.helpers.LocaleHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.ShakeEventListener;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.helpers.godmode.GodMode;
import com.mobiroller.core.jcplayer.JcNotificationPlayerService;
import com.mobiroller.core.services.ClosingService;
import com.mobiroller.core.util.radio.MediaNotificationManager;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public abstract class SharedApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks, LifecycleObserver {

    public static Context context;
    public static SharedApplication app;
    public static boolean isChatActivityOpen = false;
    public static String aveChatViewId = null;
    public static String openChatId = null;
    public static int mScreensCounter;
    private SharedPrefHelper sharedPrefHelper;

    public static List<String> jsonIdList = new ArrayList<String>();
    public static AppComponent mainComponent;

    public static String assetList[] = null;
    public static boolean isInterstitialShown = false;
    public static boolean isAppForeground = false;
    public static Activity lastActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();

        LocaleHelper.setLocale(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        LocalizationHelper.context = this;

        context = SharedApplication.this;
        app = getApp();

        new UtilManager.Builder()
                .setContext(this)
                .build();

        if (!UtilManager.sharedPrefHelper().getLanguageSetByUser())
            UtilManager.sharedPrefHelper().setDisplayLanguage(Locale.getDefault().getLanguage().toLowerCase());

        try {
            assetList = getResources().getAssets().list("Files");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        registerActivityLifecycleCallbacks(this);

        this.setTheme(R.style.FullNoTitle);
        Charset.defaultCharset();

        Constants.Mobiroller_Preferences_FilePath = context.getFilesDir().getPath();
        Charset.forName(Constants.MobiRoller_Preferences_CharSet);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);

        Intent stickyService = new Intent(this, ClosingService.class);
        if (isAppForeground)
            startService(stickyService);

        destroyNotifications();

        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ShakeEventListener mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(() -> {
            if (GodMode.isActive) {
                new GodMode().auth();
            }
        });

        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.setLocale(base));
//        Paper.init(base);
    }

    public SharedApplication getApp() {
        return null;
    }

    public static AppComponent getMainComponent() {
        return mainComponent;
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mScreensCounter++;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        lastActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mScreensCounter--;

        if (mScreensCounter < 0)
            mScreensCounter = 0;
        else if (mScreensCounter == 1)
            onLastActivity();

    }

    public abstract void onLastActivity();

    public static boolean isAppBackgroundForChat() {
        return mScreensCounter == 1;
    }

    public static boolean isAppBackground() {
        return mScreensCounter == 1;
    }

    public static boolean isAppBackgroundChat() {
        return mScreensCounter >= 1;
    }

    public static boolean isAppKilled() {
        return !(mScreensCounter > 1);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleHelper.setLocale(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void appInStartState() {
        isAppForeground = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void appInStopState() {
        isAppForeground = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void appInDestroyState() {
        destroyNotifications();
    }

    private void destroyNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(MediaNotificationManager.NOTIFICATION_ID);// Remove radio notification
        notificationManager.cancel(JcNotificationPlayerService.MP3_NOTIFICATION_ID); // Remove mp3 notification
    }

    public abstract Class getSplashClass();

    public abstract FragmentHelper getFragmentHelper();

    public abstract MenuHelper getMenuHelper();

}

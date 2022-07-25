package com.mobiroller.core.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.coreui.helpers.DataStoreHelper;
import com.mobiroller.core.coreui.helpers.LocaleHelper;
import com.mobiroller.core.models.IntroModel;
import com.mobiroller.core.models.response.UserLoginResponse;
import com.mobiroller.core.models.youtube.ChannelDetailModel;
import com.mobiroller.core.R;
import com.mobiroller.core.coreui.util.ScreenUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.mobiroller.core.constants.Constants.MobiRoller_Preferences_LoginBackgroundColor;
import static com.mobiroller.core.constants.Constants.MobiRoller_Preferences_LoginPrimaryColor;
import static com.mobiroller.core.constants.Constants.MobiRoller_Preferences_LoginThemeType;

/**
 * Created by ealtaca on 20.03.2017.
 */

public class SharedPrefHelper {

    private final String MobiRoller_Preferences = "MobiRoller_Preferences";
    private SharedPreferences appSharedPrefs;
    private Context context;

    public Context getContext() {
        return context;
    }


    public SharedPrefHelper(Context context) {
        this.context = context;
        getSharedPrefs(context);
    }

    public boolean hasValue(String key) {
        return appSharedPrefs.contains(key);
    }

    /**
     * set & get application's shared prefs
     *
     * @param context : application's context
     * @return shared preferences
     */
    public SharedPreferences getSharedPrefs(Context context) {
        appSharedPrefs = context.getSharedPreferences(MobiRoller_Preferences, Activity.MODE_PRIVATE);
        return appSharedPrefs;
    }

    /**
     * set & get methods of application notification value
     * <p>
     * default value of notification is true
     *
     * @param value : true or false
     */
    public void setNotification(boolean value) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_Notification, value).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getNotification() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_Notification, true);
    }

    /**
     * set & get methods of application notification sound value
     * <p>
     * default value of notification sound is true
     *
     * @param value : true or false
     */
    public void setNotificationSound(boolean value) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_NotificationSound, value).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getNotificationSound() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_NotificationSound, true);
    }

    /**
     * set & get user logged in status
     * set & get user email if logged in
     * <p>
     * set user login status to avoid asking login again, reset on app restart
     */

    public void setUserLoginStatus(boolean isLogged) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_UserLoginStatus, isLogged).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getUserLoginStatus() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_UserLoginStatus, false);
    }

    public void setIsChatActive(boolean isChatAvailable) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_IsChatAvailable, isChatAvailable).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getIsChatActive() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_IsChatAvailable, false);
    }

    public void setIsInAppPurchaseActive(boolean isChatAvailable) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_InAppPurchaseActive, isChatAvailable).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getIsInAppPurchaseActive() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_InAppPurchaseActive, false);
    }

    public void setIsUserAgremeentActive(boolean isChatAvailable) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_IsUserAgremeentActive, isChatAvailable).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getIsUserAgremeentActive() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_IsUserAgremeentActive, false);
    }

    public void setUserAgremeent(String userAgremeent) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_UserAgremeent, userAgremeent).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public String getUserAgremeent() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_UserAgremeent, "");
    }

    public void setUserIsAvailableForChat(boolean isChatAvailable) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_UserIsAvailableForChat, isChatAvailable).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getUserIsAvailableForChat() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_UserIsAvailableForChat, false);
    }

    public String getUserRole() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_UserRole, "0");
    }

    public String getLogoURL() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_LogoURL, "");
    }

    public String getLoginBackgroundURL() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_LoginBackgroundURL, "");
    }

    public String getLoginBackgroundColor() {
        return appSharedPrefs.getString(MobiRoller_Preferences_LoginBackgroundColor, "");
    }

    public String getLoginPrimaryColor() {
        return appSharedPrefs.getString(MobiRoller_Preferences_LoginPrimaryColor, "");
    }

    public String getLoginThemeType() {
        return appSharedPrefs.getString(MobiRoller_Preferences_LoginThemeType, "");
    }

    public void setUserRole(String role) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_UserRole, role).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void setLogoURL(String url) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_LogoURL, url).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void setLoginBackgroundURL(String url) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_LoginBackgroundURL, url).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void setLoginBackgroundColor(String color) {
        try {
            appSharedPrefs.edit().putString(MobiRoller_Preferences_LoginBackgroundColor, color).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void setLoginPrimaryColor(String color) {
        try {
            appSharedPrefs.edit().putString(MobiRoller_Preferences_LoginPrimaryColor, color).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void setLoginThemeType(String themeType) {
        try {
            appSharedPrefs.edit().putString(MobiRoller_Preferences_LoginThemeType, themeType).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get user user login registration active or not
     */
    public void setUserLoginRegistrationActive(boolean isActive) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_UserLogin_IsRegisterationActive, isActive).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get user user login registration active or not
     */
    public void setUserLoginActive(boolean isActive) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_UserLogin_IsLoginActive, isActive).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getUserLoginActive() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_UserLogin_IsLoginActive, false);
    }


    public boolean getUserLoginRegistrationActive() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_UserLogin_IsRegisterationActive, false);
    }


    /**
     * set & get refresh intro shown or not
     * <p>
     * refresh intro will be shown only once at preview
     */
    public void setRefreshIntroStatus() {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_RefreshIntro, true).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getRefreshIntroStatus() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_RefreshIntro, false);
    }


    /**
     * set & get methods of device language
     * <p>
     * we support multi language if device language exist
     * for this application, otherwise use default language
     * of application
     *
     * @param context : application's context
     */

    public void setDeviceLang(Context context, String appLang) {
        appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_CurrentLang, appLang.toLowerCase()).apply();
        LocaleHelper.setAppLanguage(context,appLang);
    }

    public String getDeviceLang() {
        return new DataStoreHelper(SharedApplication.context).getDisplayLanguage();
    }

    public int getTabHeight() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_TabHeight, 0);
    }

    /**
     * return Status Bar height if supports else return 0
     *
     * @return int
     */
    int getStatusBarHeight() {
        int statusBarHeight = 0;
        try {
            statusBarHeight = (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }


    /**
     * set & get action bar color
     */

    public void setActionBarColor(int color) {
        try {
            Theme.primaryColor = color;
            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_ActionBarColor, color).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public int getActionBarColor() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_ActionBarColor, R.color.gray);
    }


    /**
     * get unique device id value
     *
     * @return Device Id
     */
    @SuppressLint("HardwareIds")
    public String getDeviceId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /*
     * set & get unique device id value
     */
    public void setDeviceId() {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_Device_ID, Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get motion heigth and width value
     * this value is scaled for device's height
     *
     * @param motion_height : device height scaled value
     */

    public void setMotionHeight(int motion_height) {
        try {
            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_MotionHeight, motion_height).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public int getMotionHeight() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_MotionHeight, 0);
    }

    //

    public void setMotionWidth(int motion_width) {
        try {
            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_MotionWidth, motion_width).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public int getMotionWidth() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_MotionWidth, 0);
    }


    /**
     * For classical menu option we must get & set tab activity
     *
     * @param isActive : set boolean value if classical menu is activated or not
     */
    public void setTabActive(boolean isActive) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.isTabActive, isActive).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }


    public boolean getTabActive() {
        return appSharedPrefs.getBoolean(Constants.isTabActive, false);
    }


    /**
     * Gets the current registration id for application on GCM service.
     * <p>
     * If result is empty, the registration has failed.
     *
     * @param context : application's context
     * @return registration id, or empty string if the registration is not
     * complete.
     */
    public String getRegistrationId(Context context) {
        String registrationId = appSharedPrefs.getString(Constants.MobiRoller_Preferences_Registration_ID, "");
        if (registrationId.length() == 0) {
            Log.v("AppMain", "Registration not found.");
            return "";
        }
        // check if app was updated; if so, it must clear registration id to
        // avoid a race condition if GCM sends a message
        int registeredVersion = appSharedPrefs.getInt(Constants.MobiRoller_Preferences_App_Version, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(this.context);
        if (registeredVersion != currentVersion) {
            Log.v("AppMain", "App version changed or registration expired.");
            return "";
        }
        return registrationId;
    }


    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    @Deprecated
    String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public String getAppVersionName() {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    /**
     * Stores the registration id, app versionCode, and expiration time in the
     * application's {@code SharedPreferences}.
     *
     * @param context : application's context.
     * @param regId   : registration id
     */
    public void setRegistrationId(Context context, String regId) {
        int appVersion = getAppVersion(context);
        Log.v("AppMain", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = appSharedPrefs.edit();
        editor.putString(Constants.MobiRoller_Preferences_Registration_ID, regId);
        editor.putInt(Constants.MobiRoller_Preferences_App_Version, appVersion);
        long expirationTime = System.currentTimeMillis() + Constants.REGISTRATION_EXPIRY_TIME_MS;

        Log.v("AppMain", "Setting registration expiry time to " +
                new Timestamp(expirationTime));
        editor.putLong(Constants.MobiRoller_Preferences_Expiration_Time, expirationTime);
        editor.apply();
    }

    /**
     * Check if application running first time or not
     *
     * @return boolean
     */
    public boolean getFirstTime() {
        return appSharedPrefs.getBoolean(Constants.firstTime, true);
    }

    public void setFirstTime() {
        try {
            appSharedPrefs.edit().putBoolean(Constants.firstTime, false).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * Check if application running first time or not
     *
     * @return boolean
     */
    public boolean getLocationPermissionFirstTime() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Location_Permission_First_Time, true);
    }

    public void setLocationPermissionFirstTime() {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Location_Permission_First_Time, false).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }


    public boolean getFirstTimeForLanguage() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_First_Time_Language, true);
    }

    public void setFirstTimeForLanguage() {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_First_Time_Language, false).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }


    /*******************************************
     * * Twitter tokenize process starts here ***
     *******************************************/

    public void setUserToken(String token) {
        try {
            appSharedPrefs.edit().putString(Constants.USER_TOKEN, token).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public String getUserToken() {
        return appSharedPrefs.getString(Constants.USER_TOKEN, null);
    }

    public void setUserTokenSecret(String token) {
        try {
            appSharedPrefs.edit().putString(Constants.USER_SECRET, token).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public String getUserTokenSecret() {
        return appSharedPrefs.getString(Constants.USER_SECRET, null);
    }


    /******************************
     * Intro Message starts here **
     * ****************************
     *
     * @param msg to set intro msg of application
     */

    public void setIntroMessage(IntroModel msg) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_IntroMessage, new Gson().toJson(msg)).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public IntroModel getIntroMessage() {
        return new Gson().fromJson(appSharedPrefs.getString(Constants.MobiRoller_Preferences_IntroMessage, null), IntroModel.class);
    }

    /******************************
     * AdMob starts here **
     * ****************************
     *
     * @param adUnitID to set admob unit id for ads
     */

    void setAdUnitID(String adUnitID) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_AdUnitID, adUnitID).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public String getAdUnitID() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_AdUnitID, "");
    }

    /******************************
     * AdMob starts here **
     * ****************************
     *
     * @param adUnitID to set admob unit id for ads
     */

    void setNativeAddUnitID(String adUnitID) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_NativeAdUnitID, adUnitID).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public String getNativeAddUnitID() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_NativeAdUnitID, "");
//        return "685565768145735_1457454037623567";
    }


    void setBannerAdUnitID(String adUnitID) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_BannerAdUnitID, adUnitID).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }


    /*
       Kullanıcının uygulama içerisindeki tıklama sayısı döndürür.
       Eğer daha önce tıklama yaratılmadıysa 0 değeri döner
    */
    public int getInterstitialClickCount() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_Interstitial_Click_Count, 0);
    }

    /*
        Kullanıcın uygulama içerindeki tıklama sayısını set eder.
     */
    public void setInterstitialClickCount(int count) {
        try {
            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_Interstitial_Click_Count, count).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /*
    Yollanan süreyle uygulama başlangıçında
    set edilen süre arasındaki süreyi hesaplar.
    Eğer belirlenen süreden yüksek ise true döner.
    Eğer belirlenen süreden düşük ise false döner.
     */
    public boolean getInterstitialTimer(Date date) {
        long lastTime = appSharedPrefs.getLong(Constants.MobiRoller_Preferences_Interstitial_Timer, new Date().getTime());
        return getDateDiff(new Date(lastTime), date, TimeUnit.SECONDS) > getInterstitialTimeInterval();
    }

    /*
      Sadece uygulama başlangıçında ya da
       reklam gösterildikten sonra sıfırlanır.
    */
    public void setInterstitialTimer(Date date) {

        try {
            appSharedPrefs.edit().putLong(Constants.MobiRoller_Preferences_Interstitial_Timer, date.getTime()).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public AdView getBannerAd() {
        return Constants.MobiRoller_Preferences_BannerAdView;
    }

    public void setBannerAd(Context context) {
        if (getIsAdmobBannerAdEnabled() && getBannerAdUnitID() != null && !getBannerAdUnitID().equalsIgnoreCase("")) {
            AdView adView = new AdView(context);

            //setBannerHeight(context, adSize.getHeight());
            adView.setAdSize(AdSize.SMART_BANNER);
            //adView.setAdUnitId(app.getBannerAdUnitID());
            adView.setAdUnitId(getBannerAdUnitID());

            // Create an ad request.
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
//        adRequestBuilder.addTestDevice("54ACFBC3FD6707083428A5B63D1EED49");
//        view.addView(adView);
            // Start loading the ad.
            adView.loadAd(adRequestBuilder.build());

            Constants.MobiRoller_Preferences_BannerAdView = adView;
        }

    }

    void setBannerAd(final Context context, final View view, final RelativeLayout layout) {
        final AdView adView = new AdView(context);
        //setBannerHeight(context, adSize.getHeight());
        adView.setAdSize(AdSize.SMART_BANNER);
        //adView.setAdUnitId(app.getBannerAdUnitID());
        adView.setAdUnitId(getBannerAdUnitID());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (!getBannerAdUnitID().isEmpty()
                        && getBannerAd() != null) {

                    RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.FILL_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

                    lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                    if (getBannerAd().getParent() != null) {
                        ((ViewGroup) getBannerAd().getParent()).removeView(getBannerAd());
                    }

//                     Add the AdView to the view hierarchy.
                    layout.addView(getBannerAd(), lay);
                }
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if(view!=null) {
                    RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    relativeParams.setMargins(0, 0, 0, 0);  // left, top, right, bottom
                    view.setLayoutParams(relativeParams);
                }
                Constants.MobiRoller_Preferences_BannerAdView = null;
                // Code to be executed when an ad request fails.
            }
        });
        // Create an ad request.
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        // Start loading the ad.
        adView.loadAd(adRequestBuilder.build());
        Constants.MobiRoller_Preferences_BannerAdView = adView;

    }

    public String getBannerAdUnitID() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_BannerAdUnitID, "");
    }


    public void setIsAdmobInterstitialAdEnabled(boolean adStatus) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_IsAdEnabled, adStatus).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getIsAdmobInterstitialAdEnabled() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_IsAdEnabled, false);
    }


    public void setIsBannerAdEnabled(boolean adStatus) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_IsBannerAdEnabled, adStatus).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getIsAdmobBannerAdEnabled() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_IsBannerAdEnabled, false);
    }

    public boolean isBannerActive(){
        return getIsAdmobBannerAdEnabled() || getThirdPartyAdsStatus();
    }

    public void setIsNativeAdEnabled(boolean adStatus) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_IsNativeAdEnabled, adStatus).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getIsNativeAdEnabled() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_IsNativeAdEnabled, false);
    }

    void setInterstitialMultipleDisplayEnabled(boolean adStatus) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_InterstitialMultipleDisplayEnabled, adStatus).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getInterstitialMultipleDisplayEnabled() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_InterstitialMultipleDisplayEnabled, false);
    }

    // AdMob ends here ***


    public String getDeviceModel() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_DeviceModel, android.os.Build.MANUFACTURER + " - " + android.os.Build.MODEL);
    }


    /**
     * Default Username is Empty
     * Default App Name is Empty
     */
    public String getUsername() {
        if (!DynamicConstants.MobiRoller_Stage)
            return AppConfigurations.Companion.getAccountEmail();
        else
            return Constants.MobiRoller_Preview_UserName;
    }

    public String getUsernameForPreview() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_UserName, AppConfigurations.Companion.getAccountEmail());
    }

    public void setUsername(String username) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_UserName, username).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public String getPassword() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_Password, " ");
    }

    public void setPassword(String pass) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_Password, pass).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public String getUserEmail() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_UserEmail, null);
    }

    public void setUserEmail(String userEmail) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_UserEmail, userEmail).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void setUserPassword(String userPassword) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_UserPassword, userPassword).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public String getUserPassword() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_UserPassword, null);
    }

    int getProgressAnimationType() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_ProgressAnimationType, 8);
    }

    public void setProgressAnimationType(int type) {
        Theme.progressType = type;
        appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_ProgressAnimationType, type).apply();
    }


    public String getUserId() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_UserId, "");
    }

    public void setUserId(String userId) {
        appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_UserId, userId).apply();
    }

    public String getFirebaseToken() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_FirebaseToken, "");
    }

    public void setFirebaseToken(String firebaseToken) {
        appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_FirebaseToken, firebaseToken).apply();
    }

    public boolean getAskBeforeExit() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_AskBeforeExit, false);
    }

    public void setAskBeforeExit(boolean value) {
        appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_AskBeforeExit, value).apply();
    }


    public boolean getIsChatVersionSupported() {
        return !appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_Chat_Minimum_Version_Setted, false) || appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_Chat_Minimum_Version, false);
    }

    public void setIsChatVersionSupported(boolean value) {
        appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_Chat_Minimum_Version, value).apply();
        appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_Chat_Minimum_Version_Setted, true).apply();
    }


    public boolean getRateApp() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_RateApp, true);
    }

    void setRateApp(boolean value) {
        appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_RateApp, value).apply();
    }


    /**
     * set & get progress animation color
     */

    public void setProgressAnimationColor(int color) {
        try {
            Theme.progressColor = color;
            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_ProgressAnimationColor, color).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    int getProgressAnimationColor() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_ProgressAnimationColor, R.color.gray);
    }

    /**
     * set & get 3rd party ads activity control
     *
     * @return true to activate 3rd party ads & false to close 3rd party ads network
     */
    public boolean getThirdPartyAdsStatus() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_ThirdPartyAdsStatus, false);
    }

    void setThirdPartyAdsStatus(boolean status) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_ThirdPartyAdsStatus, status).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get video ads status control
     *
     * @return true to activate video ads & false to close video ads
     */
    public boolean getVideoAdsStatus() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_VideoAdsStatus, false);
    }

    public void setVideoAdsStatus(boolean status) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_VideoAdsStatus, status).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get splash ads status control
     * <p>
     * A Splash Ad is a full-page ad that is displayed immediately
     * after the application is launched. A Splash Ad first displays
     * a full page splash screen that you define followed by a full page ad.
     *
     * @return true to activate splash ads & false to disable splash ads
     */
    public boolean getSplashAdsStatus() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_SplashAdsStatus, false);
    }

    public void setSplashAdsStatus(boolean status) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_SplashAdsStatus, status).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get return ads status control
     * <p>
     * The Return Ad is a new ad unit which is displayed once the user returns
     * to your application after a certain period of time. To minimize the intrusiveness,
     * short time periods are ignored. For example, the Return Ad won't be displayed
     * if the user leaves your application to take a short phone call before returning.
     *
     * @return true to activate return ads & false to disable return ads
     */
    public boolean getReturnAdsStatus() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_ReturnAdsStatus, false);
    }

    public void setReturnAdsStatus(boolean status) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_ReturnAdsStatus, status).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get return pollfish status control
     *
     * @return true to activate pollfish & false to disable pollfish
     */
    public boolean getPollfishStatus() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_PollfishStatus, false);
    }

    void setPollfishStatus(boolean status) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_PollfishStatus, status).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get return pollfish status control
     *
     * @return true to activate pollfish & false to disable pollfish
     */
    public String getPollfishApiKey() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_PollfishApiKey, "");
    }

    void setPollfishApiKey(String status) {
        if (status == null)
            status = "";
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_PollfishApiKey, status).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get return Autostitials status control
     * <p>
     * "Autostitial" stands for "Auto Interstitial"; use this
     * integration to show an Interstitial Ad each time an activity is changed.
     * Simply call StartAppAd.enableAutoInterstitial(); after calling StartAppSDK.init.
     * You can gain more control over the frequency of
     * Autostitial Ads using two methods: time frequency and activity frequency.
     *
     * @return true to activate Autostitials & false to deactivate Autostitials
     */
    public boolean getAutoInterstitialStatus() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_AutoInterstitialStatus, false);
    }

    public void setAutoInterstitialStatus(boolean status) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_AutoInterstitialStatus, status).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get return Autostitials type
     * <p>
     * There are 2 frequency types for Autostitials
     * time frequency; You can set a minimum time interval between consecutive Autostitial Ads.
     * activity frequency; You can set a minimum number of activities between consecutive Autostitial Ads.
     *
     * @return type of Autostitials frequency
     */
    public String getAutoInterstitialType() {
        return appSharedPrefs.getString(Constants.MobiRoller_Preferences_StappAdsAutoInterstitialType, "time");
    }

    public void setAutoInterstitialType(String autostitialType) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_StappAdsAutoInterstitialType, autostitialType).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get return Autostitials frequency value
     * <p>
     * Value depending on autostitial type, if type is time we gonna consider value as seconds,
     * if type is activity than we consider value as total activity count between ads
     *
     * @return int value of Autostitials frequency value
     */
    public int getAutoInterstitialValue() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_StappAdsAutoInterstitialValue, 60);
    }

    public void setAutoInterstitialValue(int autostitialFreqValue) {
        try {
            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_StappAdsAutoInterstitialValue, autostitialFreqValue).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get notification count to display on icon as badge
     *
     * @return number of unread notifications
     */
    public int getUnreadNotificationCount() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_NotificationCount, 0);
    }

    public void setUnreadNotificationCount(int count) {
        try {
            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_NotificationCount, count).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get select language on app first run
     * <p>
     * default value is false
     *
     * @return status
     */
    public boolean getSelectLangOnStart() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_SelectLangOnStart, false);
    }

    void setSelectLangOnStart(boolean value) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_SelectLangOnStart, value).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get select user admin role
     * <p>
     * default value is false
     *
     * @return status
     */
    public boolean getUserIsChatAdmin() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_UserIsChatAdmin, false);
    }

    public void setUserIsChatAdmin(boolean value) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_UserIsChatAdmin, value).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get select user admin role
     * <p>
     * default value is false
     *
     * @return status
     */
    public boolean getUserIsChatSuperUser() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_UserIsSuperUser, false);
    }

    public void setUserIsChatSuperUser(boolean value) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_UserIsSuperUser, value).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * set & get select user moderator role
     * <p>
     * default value is false
     *
     * @return status
     */
    public boolean getUserIsChatMod() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_UserIsChatMod, false);
    }

    public void setUserIsChatMod(boolean value) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_UserIsChatMod, value).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * Get time to wait between two interstitial ads
     *
     * @return second
     */
    private int getInterstitialTimeInterval() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_InterstitialTimeInterval, 60);
    }

    /**
     * Set time to wait between two interstitial ads
     *
     * @param second (must be second)
     */
    void setInterstitialTimeInterval(int second) {
        try {
            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_InterstitialTimeInterval, second).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }


    /**
     * Get click count between two interstitial ads
     *
     * @return count (click count)
     */
    public int getInterstitialClickInterval() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_InterstitialClickInterval, 1);
    }

    /**
     * Set click count between two interstitial ads
     *
     * @param count (click count)
     */
    void setInterstitialClickInterval(int count) {
        try {
            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_InterstitialClickInterval, count).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * Get login layout type
     *
     * @return loginLayoutType (type 1 or 2) default 2.
     */
    public int getLoginLayoutType() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_InterstitialClickInterval, 2);
    }

    /**
     * Set login layout type
     *
     * @param loginLayoutType (type 1 or 2)
     */
    void setLoginLayoutType(int loginLayoutType) {
        try {
            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_InterstitialClickInterval, loginLayoutType).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * Get a diff between two dates
     *
     * @param date1    the oldest date
     * @param date2    the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


    /**
     * Set playing radio link
     *
     * @param radioBroadcastLink Radio link
     */
    public void setRadioBroadcastLink(String radioBroadcastLink) {
        try {
            appSharedPrefs.edit().putString("RadioLink", radioBroadcastLink).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return playing radio link
     *
     * @return Radio link
     */
    public String getRadioBroadcastLink() {
        return appSharedPrefs.getString("RadioLink", "");
    }


    /**
     * Set firebase-chat settings is valid
     *
     * @param isValid Chat validation
     */
    public void setChatValidated(boolean isValid) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_IsChatValidated, isValid).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return boolean firebase-chat settings is valid
     *
     * @return boolean
     */
    public boolean getChatValidated() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_IsChatValidated, false);
    }

    public void setLocationPermissionAskCount(int count) {
        try {
            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_Location_Permission_Count, count).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLocationPermissionAskCount() {
        return appSharedPrefs.getInt(Constants.MobiRoller_Preferences_Location_Permission_Count, 0);
    }


    public void setLocationPermission(boolean isValid) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_Location_Permission_Given, isValid).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLocationPermissionGiven() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_Location_Permission_Given, false);
    }

    public void setStorageNeverAsk(boolean isValid) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_Storage_Never_Ask, isValid).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isStorageNeverAsk() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_Storage_Never_Ask, false);
    }

    public void setLocationNeverAsk(boolean isValid) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_Location_Never_Ask, isValid).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLocationNeverAsk() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_Location_Never_Ask, false);
    }

    public void setUserLoginModel(UserLoginResponse userLoginModel) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(userLoginModel);
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_User_Login_Model, json).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserLoginResponse getUserLoginModel() {
        try {
            Gson gson = new Gson();
            String json = appSharedPrefs.getString(Constants.MobiRoller_Preferences_User_Login_Model, null);
            UserLoginResponse obj = gson.fromJson(json, UserLoginResponse.class);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isFavoriteActive() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_Is_Favorite_Active, true);
    }

    public void setIsFavoriteActive(boolean isFavoriteActive) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_Is_Favorite_Active, isFavoriteActive).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public GoogleSignInAccount getGoogleSignInAccount() {
        String json = appSharedPrefs.getString(Constants.MobiRoller_Preferences_Google_Sign_Account, null);
//        return json;
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriTypeHierarchyAdapter())
                    .create();
            return json == null ? null : gson.fromJson(json, GoogleSignInAccount.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setGoogleSignInAccount(GoogleSignInAccount account) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriTypeHierarchyAdapter())
                    .create();

            String json = account == null ? null : gson.toJson(account);
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_Google_Sign_Account, json).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public ChannelDetailModel getYoutubeChannelInfo(String channelId) {
        String json = appSharedPrefs.getString(Constants.MobiRoller_Preferences_Youtube_Channel_Detail + "_" + channelId, null);
        return json == null ? null : new Gson().fromJson(json, ChannelDetailModel.class);
    }

    public void setYoutubeChannelInfo(String channelId, ChannelDetailModel channelDetailModel) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_Youtube_Channel_Detail + "_" + channelId, new Gson().toJson(channelDetailModel)).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }


    public void setGoogleSignInActive(boolean isActive) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_IsGoogleSignInActive, isActive).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getGoogleSignInActive() {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_IsGoogleSignInActive, false);
    }

    public void setYoutubeChannelSubscriptionStatus(String channelId, String userId, boolean isSubscribed) {
        try {
            appSharedPrefs.edit().putBoolean(Constants.MobiRoller_Preferences_IsYoutubeChannelSubscribed + channelId + "_" + userId, isSubscribed).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public boolean getYoutubeChannelSubscriptionStatus(String channelId, String userId) {
        return appSharedPrefs.getBoolean(Constants.MobiRoller_Preferences_IsYoutubeChannelSubscribed + channelId + "_" + userId, false);
    }


    /**
     * @param fcmToken FCM Token
     */
    public void setFCMToken(String fcmToken) {
        try {
            appSharedPrefs.edit().putString(Constants.MobiRoller_Preferences_FCM_Token, fcmToken).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set & get MobiRoller badge y value (percentage)
     * this value is scaled for device's height
     */
    public void setMobiRollerBadgeY(float y, Activity activity) {
        try {
            int yPosition;
            if (activity.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT)
                yPosition = (int) ((y * 100) / ScreenUtil.getScreenHeight());
            else
                yPosition = (int) ((y * 100) / ScreenUtil.getScreenWidth());

            appSharedPrefs.edit().putInt(Constants.MobiRoller_Preferences_MobiRollerBadgeY, yPosition).apply();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public int getMobiRollerBadgeY(Activity activity) {
        int y;
        if (activity.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT)
            y = appSharedPrefs.getInt(Constants.MobiRoller_Preferences_MobiRollerBadgeY, 0) * ScreenUtil.getScreenHeight();
        else
            y = appSharedPrefs.getInt(Constants.MobiRoller_Preferences_MobiRollerBadgeY, 0) * ScreenUtil.getScreenWidth();

        return y / 100;
    }


    public void put(String key, String val) {
        appSharedPrefs.edit().putString(key, val).apply();
    }

    public void put(String key, int val) {
        appSharedPrefs.edit().putInt(key, val).apply();
    }

    public void put(String key, boolean val) {
        appSharedPrefs.edit().putBoolean(key, val).apply();
    }

    public void put(String key, float val) {
        appSharedPrefs.edit().putFloat(key, val).apply();
    }

    /**
     * Convenience method for storing doubles.
     * <p/>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to store.
     * @param val The new value for the preference.
     */
    public void put(String key, double val) {
        appSharedPrefs.edit().putString(key, String.valueOf(val)).apply();
    }

    public void put(String key, long val) {
        appSharedPrefs.edit().putLong(key, val).apply();
    }

    public String getString(String key, String defaultValue) {
        return appSharedPrefs.getString(key, defaultValue);
    }

    public String getString(String key) {
        return appSharedPrefs.getString(key, null);
    }

    public int getInt(String key) {
        return appSharedPrefs.getInt(key, 0);
    }

    public int incrementInt(String key) {
        int value=  appSharedPrefs.getInt(key, 0);
        value+=1;
        put(key,value);
        return value;
    }

    public int getInt(String key, int defaultValue) {
        return appSharedPrefs.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return appSharedPrefs.getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        return appSharedPrefs.getLong(key, defaultValue);
    }

    public float getFloat(String key) {
        return appSharedPrefs.getFloat(key, 0);
    }

    public float getFloat(String key, float defaultValue) {
        return appSharedPrefs.getFloat(key, defaultValue);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p/>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to fetch.
     */
    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p/>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to fetch.
     */
    public double getDouble(String key, double defaultValue) {
        try {
            return Double.valueOf(appSharedPrefs.getString(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return appSharedPrefs.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return appSharedPrefs.getBoolean(key, false);
    }



    public void setDisplayLanguage(String value) {
        new DataStoreHelper(context).setDisplayLanguage(value);
    }

    public String getDisplayLanguage() {
        return new DataStoreHelper(context).getDisplayLanguage();
    }

    public void setDefaultLang(String lang) {
        new DataStoreHelper(context).setDefaultLanguage(lang);
    }

    public String getDefaultLang() {
        return new DataStoreHelper(context).getDefaultLanguage();
    }

    public void setLocaleCodes(String languages) {
        new DataStoreHelper(context).setLocaleCodes(languages);
    }

    @Nullable
    public String getLocaleCodes() {
        return new DataStoreHelper(context).getLocaleCodes();
    }

    public boolean getLanguageSetByUser() {
        return new DataStoreHelper(context).getLanguageSetByUser();
    }

    public void languageSetByUser() {
        new DataStoreHelper(context).languageSetByUser();
    }


}

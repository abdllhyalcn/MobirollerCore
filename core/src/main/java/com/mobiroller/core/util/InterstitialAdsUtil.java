package com.mobiroller.core.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.activities.ConnectionRequired;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.helpers.NetworkHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.NavigationItemModel;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.R;

import java.util.Date;

/**
 * Created by ealtaca on 22.04.2017.
 */

public class InterstitialAdsUtil {

    private SharedPrefHelper sharedPrefHelper;
    private Activity activity;
    private FragmentManager fragmentManager;
    private NetworkHelper networkHelper;


    public InterstitialAdsUtil(AppCompatActivity activity) {
        this.activity = activity;
        this.fragmentManager = activity.getSupportFragmentManager();
        sharedPrefHelper = UtilManager.sharedPrefHelper();
        networkHelper = UtilManager.networkHelper();
    }

    public InterstitialAdsUtil(Activity activity) {
        this.activity = activity;
        this.fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
        sharedPrefHelper = UtilManager.sharedPrefHelper();
        networkHelper = UtilManager.networkHelper();
    }

    /*
     * Increase Check Auto Interstitial Ads count
     */
    private void increaseClickCount() {
        sharedPrefHelper.setInterstitialClickCount(sharedPrefHelper.getInterstitialClickCount() + 1);
    }

    /*
     * Check Auto Interstitial Ads for activity transition
     */
    public void checkInterstitialAds(Intent interstitialIntent) {
        increaseClickCount();
        if (checkConditions()) {
            loadInterstitialAds(interstitialIntent);
        } else {
            if (!networkHelper.isConnected() && checkConnectionRequired(interstitialIntent))
                activity.startActivity(new Intent(activity, ConnectionRequired.class).putExtra("intent", interstitialIntent));
            else
                checkFirstAd(interstitialIntent, null);

        }
    }

    /*
     * Check First Interstitial Ads for activity transition
     */
    private void checkFirstAd(final Intent intent, final Bundle bundle) {
        final AdManager adManager = AdManager.getInstance();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adManager.isInterstitialAdReady() && !SharedApplication.isInterstitialShown) {
                    adManager.showInterstitialAd(new AdManager.InterstitialAdCallBack() {
                        @Override
                        public void onAdClosed() {
                            startActivity(intent, bundle);
                        }
                    });
                } else {
                    startActivity(intent, bundle);
                }
            }
        });


    }

    /*
     * Check First Interstitial Ads for fragment transition
     */
    private void checkFirstAdFragment(final Fragment interstitialFragment) {
        AdManager adManager = AdManager.getInstance();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adManager.isInterstitialAdReady() && !SharedApplication.isInterstitialShown) {
                    adManager.showInterstitialAd(new AdManager.InterstitialAdCallBack() {
                        @Override
                        public void onAdClosed() {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_container, interstitialFragment).commitAllowingStateLoss();
                        }
                    });
                } else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, interstitialFragment).commitAllowingStateLoss();
                }
            }
        });

    }

    private void startActivity(Intent intent, Bundle bundle) {
        if (bundle != null)
            activity.startActivity(intent, bundle);
        else
            activity.startActivity(intent);
    }

    /*
     * Check Auto Interstitial Ads for activity transition
     */
    public void checkInterstitialAds(Intent interstitialIntent, Bundle bundle) {
        increaseClickCount();
        if (checkConditions()) {
            loadInterstitialAds(interstitialIntent, bundle);
        } else if (!networkHelper.isConnected() && checkConnectionRequired(interstitialIntent))
            activity.startActivity(new Intent(activity, ConnectionRequired.class).putExtra("intent", interstitialIntent));
        else
            checkFirstAd(interstitialIntent, bundle);
    }

    /*
     * Check Auto Interstitial Ads for fragment transition
     */
    public void checkInterstitialAds(Fragment interstitialFragment) {
        increaseClickCount();
        if (checkConditions()) {
            loadInterstitialAds(interstitialFragment);
        } else
            checkFirstAdFragment(interstitialFragment);
    }

    /*
     * Check Auto Interstitial Ads for fragment transition
     */
    public boolean checkInterstitialAds(Fragment interstitialFragment, ScreenModel screenModel, String screenId, String screenType,String subScreenType, String updateDate) {
        increaseClickCount();
        if (checkConditions()) {
            loadInterstitialAds(interstitialFragment);
            return true;
        } else if (!networkHelper.isConnected() && checkConnectionRequired(screenType)) {
            Intent newIntent = new Intent(activity, ConnectionRequired.class);
            newIntent.putExtra(Constants.KEY_SCREEN_ID,screenId);
            if (screenModel != null)
                JSONStorage.putScreenModel(screenId,screenModel);
            newIntent.putExtra(Constants.KEY_SCREEN_TYPE, screenType);
            newIntent.putExtra(Constants.KEY_SUB_SCREEN_TYPE, subScreenType);
            newIntent.putExtra(Constants.KEY_UPDATE_DATE, updateDate);
            activity.startActivity(newIntent);
            return false;
        } else {
            checkFirstAdFragment(interstitialFragment);
            return true;
        }
    }

    /*
     * Check Auto Interstitial Ads for fragment transition
     */
    public boolean checkInterstitialAds(Fragment interstitialFragment, ScreenModel screenModel, NavigationItemModel navigationItemModel) {
        increaseClickCount();
        if (checkConditions()) {
            loadInterstitialAds(interstitialFragment);
            return true;
        } else if (!networkHelper.isConnected() && checkConnectionRequired(navigationItemModel.getScreenType())) {
            Intent newIntent = new Intent(activity, ConnectionRequired.class);
            newIntent.putExtra(Constants.KEY_SCREEN_ID,navigationItemModel.getAccountScreenID());
            if (screenModel != null)
                JSONStorage.putScreenModel(navigationItemModel.getAccountScreenID(),screenModel);
            newIntent.putExtra(Constants.KEY_SCREEN_TYPE, navigationItemModel.getScreenType());
            newIntent.putExtra(Constants.KEY_SUB_SCREEN_TYPE, navigationItemModel.screenSubtype);
            newIntent.putExtra(Constants.KEY_UPDATE_DATE, navigationItemModel.getUpdateDate());
            activity.startActivity(newIntent);
            return false;
        } else {
            checkFirstAdFragment(interstitialFragment);
            return true;
        }
    }

    private void loadInterstitialAds(final Intent interstitialIntent) {
        final AdManager adManager = AdManager.getInstance();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adManager.isInterstitialAdReady()) {
                    adManager.showInterstitialAd(new AdManager.InterstitialAdCallBack() {
                        @Override
                        public void onAdClosed() {
                            activity.startActivity(interstitialIntent);
                        }
                    });
                } else {
                    adManager.createInterstitialAd();
                    activity.startActivity(interstitialIntent);
                }
            }
        });

    }

    private void loadInterstitialAds(final Intent interstitialIntent, final Bundle bundle) {
        final AdManager adManager = AdManager.getInstance();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adManager.isInterstitialAdReady()) {
                    adManager.showInterstitialAd(new AdManager.InterstitialAdCallBack() {
                        @Override
                        public void onAdClosed() {
                            activity.startActivity(interstitialIntent,bundle);
                        }
                    });
                } else {
                    adManager.createInterstitialAd();
                    activity.startActivity(interstitialIntent,bundle);
                }
            }
        });
    }

    private void loadInterstitialAds(final Fragment interstitialFragment) {

        AdManager adManager = AdManager.getInstance();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adManager.isInterstitialAdReady()) {
                    adManager.showInterstitialAd(new AdManager.InterstitialAdCallBack() {
                        @Override
                        public void onAdClosed() {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_container, interstitialFragment).commitAllowingStateLoss();
                        }
                    });
                } else {
                    adManager.createInterstitialAd();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, interstitialFragment).commitAllowingStateLoss();
                }
            }
        });

    }

    private boolean checkConditions() {
        return (networkHelper.isConnected()
                && !DynamicConstants.MobiRoller_Stage
                && sharedPrefHelper.getIsAdmobInterstitialAdEnabled()
                && sharedPrefHelper.getInterstitialClickCount() >= sharedPrefHelper.getInterstitialClickInterval()
                && sharedPrefHelper.getInterstitialTimer(new Date())
                && sharedPrefHelper.getInterstitialMultipleDisplayEnabled()
                && SharedApplication.isInterstitialShown);
    }

    private boolean checkConnectionRequired(Intent intent) {
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("android.intent.action.DIAL"))
            return false;
        String[] connectionRequiredActivities = activity.getResources().getStringArray(R.array.connection_required_activities);
        for (String connectionRequiredActivity : connectionRequiredActivities) {
            if (("com.mobiroller.core.activities." + connectionRequiredActivity).equalsIgnoreCase(intent.getComponent().getClassName()))
                return true;
        }
        return false;
    }

    private boolean checkConnectionRequired(String screenType) {
        String[] connectionRequiredActivities = activity.getResources().getStringArray(R.array.connection_required_activities);
        for (String connectionRequiredActivity : connectionRequiredActivities) {
            if ((connectionRequiredActivity).equalsIgnoreCase(screenType))
                return true;
        }
        return false;
    }
}

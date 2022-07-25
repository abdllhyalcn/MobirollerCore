package com.mobiroller.core.util;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.events.StartMedia;
import com.mobiroller.core.models.events.StopMedia;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

/**
 * Created by qosmio on 19.03.2018.
 */

public class AdManager {

    private static AdManager singleton = new AdManager();
    private InterstitialAd interstitialAd;
    private InterstitialAdCallBack callBack;
    public static boolean isAdOpen = false;

    private AdManager() {
    }

    /***
     * returns an instance of this class. if singleton is null create an instance
     * else return  the current instance
     * @return AdManager
     */
    public synchronized static AdManager getInstance() {
        return singleton;
    }

    /***
     * Create an interstitial ad
     */
    private void createAdmobInterstitialAd(final String adUnitId) {
        if (interstitialAd == null) {
            interstitialAd = new InterstitialAd(SharedApplication.context);
            interstitialAd.setAdUnitId(adUnitId);
            interstitialAd.setAdListener(new AdListener() {

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                    isAdOpen=true;
                    EventBus.getDefault().post(new StopMedia());
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();

                    if(callBack!=null)
                        callBack.onAdClosed();

                    isAdOpen=false;
                    SharedApplication.isInterstitialShown = true;
                    if ( UtilManager.sharedPrefHelper().getInterstitialMultipleDisplayEnabled()) {
                        UtilManager.sharedPrefHelper().setInterstitialClickCount(0);
                        UtilManager.sharedPrefHelper().setInterstitialTimer(new Date());
                    }
                    EventBus.getDefault().post(new StartMedia());
                }
            });
        }
        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded())
            interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public boolean isInterstitialAdReady() {
        return interstitialAd != null && interstitialAd.isLoaded() && SharedApplication.isAppForeground;
    }

    public void createInterstitialAd(){
        if (interstitialAd == null) {
            AdManager.getInstance().createAdmobInterstitialAd(UtilManager.sharedPrefHelper().getAdUnitID());
        } else {
            interstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }


    public void showInterstitialAd(InterstitialAdCallBack interstitialAdCallBack) {
        callBack = interstitialAdCallBack;
        interstitialAd.show();
    }

    public interface InterstitialAdCallBack {

        void onAdClosed();

    }

}

package com.mobiroller.core.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.helpers.DataStoreHelper;
import com.mobiroller.core.coreui.helpers.LocaleHelper;
import com.mobiroller.core.models.MainModel;

import java.util.Date;

//import com.facebook.ads.NativeAdsManager;

/**
 * Created by ealtaca on 12/29/17.
 */

public class ConfigurationHelper {

    SharedPrefHelper sharedPrefHelper;
    Context context;
    ScreenHelper screenHelper;

    public ConfigurationHelper(SharedPrefHelper sharedPrefHelper, Context context, ScreenHelper screenHelper) {
        this.sharedPrefHelper = sharedPrefHelper;
        this.context = context;
        this.screenHelper = screenHelper;
    }

    //set logoUrl, navBarTintColor, adMobCode and adMobBannerCode
    public void setAppSettings(MainModel localObj) {
        try {
            if (localObj.ecommerceScreenID != null)
                Constants.E_COMMERCE_SCREEN_ID = localObj.ecommerceScreenID;
            AppSettingsHelper.setIsECommerceActive(localObj.isECommerceActive);
            if (localObj.eCommerceCredentials != null && localObj.eCommerceCredentials.androidChannelID != null)
                new DataStoreHelper(context).setECommerceAliasKey(localObj.eCommerceCredentials.androidChannelID);
            AppSettingsHelper.setIsTourVisioActive(localObj.isHotelViewActive);
            UtilManager.sharedPrefHelper().put(Constants.MobiRoller_Preferences_Show_MobiRoller_Badge, localObj.isBadgeActive);
            if (localObj.isBadgeActive)
                sharedPrefHelper.put(Constants.MobiRoller_Preferences_MobiRoller_Badge_Url, localObj.badgeInfo);
            /* Set logo and background image for user login page */
            if (localObj.getLogoImage() != null) {
                sharedPrefHelper.setLogoURL(localObj.getLogoImage().getImageURL());
            } else {
                sharedPrefHelper.setLogoURL("");
            }
            if (localObj.getLoginBackgroundImage() != null) {
                sharedPrefHelper.setLoginBackgroundURL(localObj.getLoginBackgroundImage().getImageURL());
            } else {
                sharedPrefHelper.setLoginBackgroundURL("");
            }
            if (localObj.getLoginThemeType() != null) {
                sharedPrefHelper.setLoginThemeType(localObj.getLoginThemeType());
            } else {
                sharedPrefHelper.setLoginThemeType("");
            }
            if (localObj.getLoginBackgroundColor() != null) {
                sharedPrefHelper.setLoginBackgroundColor(localObj.getLoginBackgroundColor());
            } else {
                sharedPrefHelper.setLoginBackgroundColor("");
            }
            if (localObj.getLoginPrimaryColor() != null) {
                sharedPrefHelper.setLoginPrimaryColor(localObj.getLoginPrimaryColor());
            } else {
                sharedPrefHelper.setLoginPrimaryColor("");
            }

            if (localObj.loginProviders != null) {
                if (localObj.loginProviders.google) {
                    sharedPrefHelper.setGoogleSignInActive(true);
                } else {
                    sharedPrefHelper.setGoogleSignInActive(false);
                }
            } else {
                sharedPrefHelper.setGoogleSignInActive(false);
            }

            if (localObj.userAgreementForm != null && localObj.userAgreementForm.isActive != null && localObj.userAgreementForm.isActive) {
                sharedPrefHelper.setIsUserAgremeentActive(true);
                sharedPrefHelper.setUserAgremeent(localObj.userAgreementForm.url);
            } else
                sharedPrefHelper.setIsUserAgremeentActive(false);
            if (!DynamicConstants.MobiRoller_Stage) {

                /** Set 3rd Party ads activated or not **/
                if (localObj.isThirdPartyAdsService() == null || !localObj.isThirdPartyAdsService()) {
                    sharedPrefHelper.setThirdPartyAdsStatus(false);
                } else {
                    sharedPrefHelper.setThirdPartyAdsStatus(true);

                    // check video ads status
                    if (localObj.isVideoAdsEnabled() == null || !localObj.isVideoAdsEnabled())
                        sharedPrefHelper.setVideoAdsStatus(false);
                    else
                        sharedPrefHelper.setVideoAdsStatus(true);

                    // check splash ads status
                    if (localObj.isSplashAdsEnabled() == null || !localObj.isSplashAdsEnabled())
                        sharedPrefHelper.setSplashAdsStatus(false);
                    else
                        sharedPrefHelper.setSplashAdsStatus(true);

                    // check return ads status
                    if (localObj.isReturnAdsEnabled() == null || !localObj.isReturnAdsEnabled())
                        sharedPrefHelper.setReturnAdsStatus(false);
                    else
                        sharedPrefHelper.setReturnAdsStatus(true);

                    // check auto interstitial status
                    if (localObj.isAutoInterstitialEnabled() == null || !localObj.isAutoInterstitialEnabled())
                        sharedPrefHelper.setAutoInterstitialStatus(false);
                    else
                        sharedPrefHelper.setAutoInterstitialStatus(true);

                    // set auto interstitial type
                    if (localObj.getAutoInterstitialType() != null && (localObj.getAutoInterstitialType().equals("time") || localObj.getAutoInterstitialType().equals("activities")))
                        sharedPrefHelper.setAutoInterstitialType(localObj.getAutoInterstitialType());

                    // set auto interstitial value
                    try {
                        if (localObj.getAutoInterstitialValue() != 0)
                            sharedPrefHelper.setAutoInterstitialValue(localObj.getAutoInterstitialValue());
                    } catch (Exception ignore) {
                        // auto interstitial parse error
                        ignore.printStackTrace();
                    }
                }

                /** Ads setting for interstitial (if not first time, check for update) **/
                if (localObj.getAdMobCode() != null && !localObj.getAdMobCode().equals("null")) {
                    sharedPrefHelper.setAdUnitID(localObj.getAdMobCode());
                    sharedPrefHelper.setIsAdmobInterstitialAdEnabled(localObj.isAdEnabled());
                } else {
                    sharedPrefHelper.setIsAdmobInterstitialAdEnabled(false);
                }

                /** Ads setting for banner (if not first time, check for update) **/
                if (localObj.getAdMobBannerCode() != null && !localObj.getAdMobBannerCode().equals("null")) {
                    sharedPrefHelper.setBannerAdUnitID(localObj.getAdMobBannerCode());
                    sharedPrefHelper.setIsBannerAdEnabled(localObj.isBannerAdEnabled());
                } else {
                    sharedPrefHelper.setIsBannerAdEnabled(false);
                }

                if (localObj.getAutoIntAdClickInterval() != 0) {
                    sharedPrefHelper.setInterstitialClickInterval(localObj.getAutoIntAdClickInterval());
                }
                if (localObj.getAutoIntAdTimeInterval() != 0) {
                    sharedPrefHelper.setInterstitialTimeInterval(localObj.getAutoIntAdTimeInterval());
                }
                sharedPrefHelper.setInterstitialTimer(new Date());
                if (localObj.getLoginLayoutType() != 0)
                    sharedPrefHelper.setLoginLayoutType(localObj.getLoginLayoutType());

                sharedPrefHelper.setInterstitialMultipleDisplayEnabled(localObj.getInterstitialMultipleDisplayEnabled());
//            sharedPrefHelper.setInterstitialMultipleDisplayEnabled(false);

                /** Ads setting for native (if not first time, check for update) **/
                if (localObj.getAdMobNativeCode() != null && !localObj.getAdMobNativeCode().equals("null")) {
                    sharedPrefHelper.setNativeAddUnitID(localObj.getAdMobNativeCode());
                    sharedPrefHelper.setIsNativeAdEnabled(localObj.isNativeAdEnabled());
                } else {
                    sharedPrefHelper.setIsNativeAdEnabled(false);
                }
                if (sharedPrefHelper.getIsNativeAdEnabled() && !sharedPrefHelper.getNativeAddUnitID().startsWith("ca-")) {
//                new NativeAdsManager(context, sharedPrefHelper.getNativeAddUnitID(), 10);
                }
            }

            /** actionbar color **/
            if (localObj.getNavBarTintColor() != null && !localObj.getNavBarTintColor().equals("null")) {
                sharedPrefHelper.setActionBarColor(screenHelper.setColorUnselected(localObj.getNavBarTintColor()));
            }

            if (localObj.isChatActive()) {
                sharedPrefHelper.setIsChatActive(true);
            } else
                sharedPrefHelper.setIsChatActive(false);

            if (localObj.isInAppPurchaseActive != null && localObj.isInAppPurchaseActive) {
                clearInAppPurchaseCache();
                sharedPrefHelper.setIsInAppPurchaseActive(true);
            } else
                sharedPrefHelper.setIsInAppPurchaseActive(false);

            if (localObj.isFavoriteActive()) {
                sharedPrefHelper.setIsFavoriteActive(true);
            } else
                sharedPrefHelper.setIsFavoriteActive(false);

            /** progress animation type **/
            if (localObj.getProgressAnimationType() != 0) {
                sharedPrefHelper.setProgressAnimationType((localObj.getProgressAnimationType()));
            }

            /** set askBeforeExit **/
            sharedPrefHelper.setAskBeforeExit((localObj.isAskBeforeExit()));

            /** set SelectLangOnStart **/
            if (localObj.isSelectLangOnStart() != null)
                sharedPrefHelper.setSelectLangOnStart((localObj.isSelectLangOnStart()));

            /** set rateApp **/
            sharedPrefHelper.setRateApp((localObj.isRateApp()));

            /** progress animation color **/
            if (localObj.getProgressAnimationColor() != null) {
                try {
                    int progressAnimationColor = screenHelper.setColorUnselected(localObj.getProgressAnimationColor());
                    sharedPrefHelper.setProgressAnimationColor(progressAnimationColor);
                } catch (Exception ignore) {
                    // animation color error
                    ignore.printStackTrace();
                }
            }

        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    //set language, locale codes
    public void setAppLanguage(MainModel localObj) {

        try {
            sharedPrefHelper.setDefaultLang(localObj.getDefaultLanguage());
            sharedPrefHelper.setLocaleCodes(localObj.getLocaleCodes());

            LocaleHelper.setLocale(context);
        } catch (Exception e1) {
            e1.printStackTrace();
            sharedPrefHelper.setDefaultLang("tr");
            sharedPrefHelper.setLocaleCodes("tr");
            if(!UtilManager.sharedPrefHelper().getLanguageSetByUser())
                UtilManager.sharedPrefHelper().setDisplayLanguage(sharedPrefHelper.getDefaultLang().toLowerCase());
        }
    }

    private void clearInAppPurchaseCache() {
        SharedPreferences settings = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

}

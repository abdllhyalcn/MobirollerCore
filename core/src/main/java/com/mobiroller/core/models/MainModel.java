package com.mobiroller.core.models;

import com.mobiroller.core.coreui.models.ColorModel;
import com.mobiroller.core.coreui.models.ImageModel;

import java.io.Serializable;

/**
 * Created by ealtaca on 07.03.2017.
 */

public class MainModel implements Serializable {

    private ImageModel tabBarBackgroundImageName;
    private ImageModel splashImage;
    private ImageModel logoImage;
    private ImageModel loginBackgroundImage;
    private ColorModel navBarTintColor;
    private String updateDate;
    private String localeCodes;
    private String defaultLanguage;
    private String GATrackingId;
    private String adMobCode;
    private String adMobBannerCode;
    private String adMobNativeCode;
    private String adType;
    private boolean isAdEnabled;
    private boolean isBannerAdEnabled;
    private boolean isNativeAdEnabled;
    private boolean isTrialExpired;
    private int progressAnimationType;
    private ColorModel progressAnimationColor;
    private boolean askBeforeExit;
    private boolean rateApp;
    private Boolean selectLangOnStart;
    private int autoIntAdTimeInterval;
    private int autoIntAdClickInterval;
    private boolean autoIntAdStatus;
    private boolean thirdPartyAdsService;
    private boolean videoAdsStatus; // Video ads service status
    private boolean splashAdsStatus; // StartApp Splash ads service status
    private boolean returnAdsStatus; // StartApp Return ads service status
    private boolean autoInterstitialStatus; // auto interstitial ads status
    private String autoInterstitialType; // auto interstitial ads type (time or activity)
    private int autoInterstitialValue; // auto interstitial ads value (seconds for time, steps for activity)
    private boolean isBleshActive; // blesh service status
    private int loginLayoutType;
    private String loginThemeType;
    private boolean isPollServiceEnabled;
    private String pollServiceKey;
    private IntroModel introMessage;
    private boolean isChatActive; // firebase chat status
    private boolean isFavoriteActive; // favorite status
    public UserAgreementModel userAgreementForm; // user agreement status
    public LoginProviderModel loginProviders; // login provider list
    public Boolean isInAppPurchaseActive; // in app purchase status
    public boolean isECommerceActive;// ecommerce status
    public boolean isBadgeActive;// mobiroller badge status
    public String badgeInfo;// mobiroller badge url
    public String ecommerceScreenID;// ecommerce view screenId
    public boolean isHotelViewActive;// tourvisio status
    public ECommerceCredentials eCommerceCredentials;
    private String loginBackgroundColor;
    private String loginPrimaryColor;

    public IntroModel getIntroMessage() {
        return introMessage;
    }

    public void setIntroMessage(IntroModel introMessage) {
        this.introMessage = introMessage;
    }

    public boolean isPollServiceEnabled() {
        return isPollServiceEnabled;
    }

    public void setPollServiceEnabled(boolean pollServiceEnabled) {
        isPollServiceEnabled = pollServiceEnabled;
    }

    public String getPollServiceKey() {
        return pollServiceKey;
    }

    public void setPollServiceKey(String pollServiceKey) {
        this.pollServiceKey = pollServiceKey;
    }

    public int getLoginLayoutType() {
        return loginLayoutType;
    }

    public void setLoginLayoutType(int loginLayoutType) {
        this.loginLayoutType = loginLayoutType;
    }

    public boolean isChatActive() {
        return isChatActive;
    }

    public void setChatActive(boolean chatActive) {
        isChatActive = chatActive;
    }

    public ImageModel getLoginBackgroundImage() {
        return loginBackgroundImage;
    }

    public void setLoginBackgroundImage(ImageModel loginBackgroundImage) {
        this.loginBackgroundImage = loginBackgroundImage;
    }

    public Boolean getSelectLangOnStart() {
        return selectLangOnStart;
    }

    public void setSelectLangOnStart(Boolean selectLangOnStart) {
        this.selectLangOnStart = selectLangOnStart;
    }

    public boolean isAutoIntAdStatus() {
        return autoIntAdStatus;
    }

    public boolean getInterstitialMultipleDisplayEnabled() {
        return autoIntAdStatus;
    }

    public void setAutoIntAdStatus(boolean autoIntAdStatus) {
        this.autoIntAdStatus = autoIntAdStatus;
    }

    public int getAutoIntAdTimeInterval() {
        return autoIntAdTimeInterval;
    }

    public void setAutoIntAdTimeInterval(int autoIntAdTimeInterval) {
        this.autoIntAdTimeInterval = autoIntAdTimeInterval;
    }

    public int getAutoIntAdClickInterval() {
        return autoIntAdClickInterval;
    }

    public void setAutoIntAdClickInterval(int autoIntAdClickInterval) {
        this.autoIntAdClickInterval = autoIntAdClickInterval;
    }

    public Boolean isSelectLangOnStart() {
        if (selectLangOnStart == null)
            return false;
        return selectLangOnStart;
    }

    public void setSelectLangOnStart(boolean selectLangOnStart) {
        this.selectLangOnStart = selectLangOnStart;
    }

    public boolean isNativeAdEnabled() {
        return isNativeAdEnabled;
    }

    public void setNativeAdEnabled(boolean nativeAdEnabled) {
        isNativeAdEnabled = nativeAdEnabled;
    }

    public String getAdMobNativeCode() {
        return adMobNativeCode;
    }

    public void setAdMobNativeCode(String adMobNativeCode) {
        this.adMobNativeCode = adMobNativeCode;
    }

    public boolean isAskBeforeExit() {
        return askBeforeExit;
    }

    public void setAskBeforeExit(boolean askBeforeExit) {
        this.askBeforeExit = askBeforeExit;
    }

    public boolean isRateApp() {
        return rateApp;
    }

    public void setRateApp(boolean rateApp) {
        this.rateApp = rateApp;
    }

    public Boolean isBleshServiceActive() {
        return isBleshActive;
    }

    public void setBleshServiceStatus(boolean bleshStatus) {
        this.isBleshActive = bleshStatus;
    }

    public ImageModel getTabBarBackgroundImageName() {
        return tabBarBackgroundImageName;
    }

    public void setTabBarBackgroundImageName(ImageModel tabBarBackgroundImageName) {
        this.tabBarBackgroundImageName = tabBarBackgroundImageName;
    }

    public ImageModel getSplashImage() {
        return splashImage;
    }

    public void setSplashImage(ImageModel splashImage) {
        this.splashImage = splashImage;
    }

    public ImageModel getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(ImageModel logoImage) {
        this.logoImage = logoImage;
    }

    public ColorModel getNavBarTintColor() {
        return navBarTintColor;
    }

    public void setNavBarTintColor(ColorModel navBarTintColor) {
        this.navBarTintColor = navBarTintColor;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getLocaleCodes() {
        return localeCodes;
    }

    public void setLocaleCodes(String localeCodes) {
        this.localeCodes = localeCodes;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getGATrackingId() {
        return GATrackingId;
    }

    public void setGATrackingId(String GATrackingId) {
        this.GATrackingId = GATrackingId;
    }

    public String getAdMobCode() {
        return adMobCode;
    }

    public void setAdMobCode(String adMobCode) {
        this.adMobCode = adMobCode;
    }

    public String getAdMobBannerCode() {
        return adMobBannerCode;
    }

    public void setAdMobBannerCode(String adMobBannerCode) {
        this.adMobBannerCode = adMobBannerCode;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public boolean isAdEnabled() {
        return isAdEnabled;
    }

    public void setAdEnabled(boolean adEnabled) {
        isAdEnabled = adEnabled;
    }

    public boolean isBannerAdEnabled() {
        return isBannerAdEnabled;
    }

    public void setBannerAdEnabled(boolean bannerAdEnabled) {
        isBannerAdEnabled = bannerAdEnabled;
    }

    public boolean isTrialExpired() {
        return isTrialExpired;
    }

    public void setTrialExpired(boolean trialExpired) {
        isTrialExpired = trialExpired;
    }

    public int getProgressAnimationType() {
        return progressAnimationType;
    }

    public void setProgressAnimationType(int progressAnimationType) {
        this.progressAnimationType = progressAnimationType;
    }

    public ColorModel getProgressAnimationColor() {
        return progressAnimationColor;
    }

    public void setProgressAnimationColor(ColorModel progressAnimationColor) {
        this.progressAnimationColor = progressAnimationColor;
    }

    public Boolean isThirdPartyAdsService() {
        return thirdPartyAdsService;
    }

    public void setThirdPartyAdsService(boolean thirdPartyAdsService) {
        this.thirdPartyAdsService = thirdPartyAdsService;
    }

    public Boolean isVideoAdsEnabled() {
        return videoAdsStatus;
    }

    public void setVideoAdsStatus(boolean videoAdsStatus) {
        this.videoAdsStatus = videoAdsStatus;
    }

    public Boolean isSplashAdsEnabled() {
        return splashAdsStatus;
    }

    public void setSplashAdsStatus(boolean splashAdsStatus) {
        this.splashAdsStatus = splashAdsStatus;
    }

    public Boolean isReturnAdsEnabled() {
        return returnAdsStatus;
    }

    public void setReturnAdsStatus(boolean returnAdsStatus) {
        this.returnAdsStatus = returnAdsStatus;
    }

    public Boolean isAutoInterstitialEnabled() {
        return autoInterstitialStatus;
    }

    public void setAutoInterstitialStatus(boolean autoInterstitialStatus) {
        this.autoInterstitialStatus = autoInterstitialStatus;
    }

    public String getAutoInterstitialType() {
        return autoInterstitialType;
    }

    public void setAutoInterstitialType(String autoInterstitialType) {
        this.autoInterstitialType = autoInterstitialType;
    }

    public int getAutoInterstitialValue() {
        return autoInterstitialValue;
    }

    public void setAutoInterstitialValue(int autoInterstitialValue) {
        this.autoInterstitialValue= autoInterstitialValue;
    }

    public boolean isFavoriteActive() {
        return isFavoriteActive;
    }

    public String getLoginBackgroundColor() {
        return loginBackgroundColor;
    }

    public void setLoginBackgroundColor(String loginBackgroundColor) {
        this.loginBackgroundColor = loginBackgroundColor;
    }

    public String getLoginPrimaryColor() {
        return loginPrimaryColor;
    }

    public void setLoginPrimaryColor(String loginPrimaryColor) {
        this.loginPrimaryColor = loginPrimaryColor;
    }

    public String getLoginThemeType() {
        return loginThemeType;
    }

    public void setLoginThemeType(String loginThemeType) {
        this.loginThemeType = loginThemeType;
    }

    @Override
    public String toString() {
        return "MainModel{" +
                ", tabBarBackgroundImageName=" + tabBarBackgroundImageName +
                ", splashImage=" + splashImage +
                ", logoImage=" + logoImage +
                ", loginBackgroundImage=" + loginBackgroundImage +
                ", navBarTintColor=" + navBarTintColor +
                ", updateDate='" + updateDate + '\'' +
                ", localeCodes='" + localeCodes + '\'' +
                ", defaultLanguage='" + defaultLanguage + '\'' +
                ", GATrackingId='" + GATrackingId + '\'' +
                ", adMobCode='" + adMobCode + '\'' +
                ", adMobBannerCode='" + adMobBannerCode + '\'' +
                ", adMobNativeCode='" + adMobNativeCode + '\'' +
                ", adType='" + adType + '\'' +
                ", isAdEnabled=" + isAdEnabled +
                ", isBannerAdEnabled=" + isBannerAdEnabled +
                ", isNativeAdEnabled=" + isNativeAdEnabled +
                ", isTrialExpired=" + isTrialExpired +
                ", progressAnimationType=" + progressAnimationType +
                ", progressAnimationColor=" + progressAnimationColor +
                ", askBeforeExit=" + askBeforeExit +
                ", rateApp=" + rateApp +
                ", selectLangOnStart=" + selectLangOnStart +
                ", autoIntAdTimeInterval=" + autoIntAdTimeInterval +
                ", autoIntAdClickInterval=" + autoIntAdClickInterval +
                ", autoIntAdStatus=" + autoIntAdStatus +
                ", thirdPartyAdsService=" + thirdPartyAdsService +
                ", videoAdsStatus=" + videoAdsStatus +
                ", splashAdsStatus=" + splashAdsStatus +
                ", returnAdsStatus=" + returnAdsStatus +
                ", autoInterstitialStatus=" + autoInterstitialStatus +
                ", autoInterstitialType='" + autoInterstitialType + '\'' +
                ", autoInterstitialValue=" + autoInterstitialValue +
                ", isBleshActive=" + isBleshActive +
                ", loginLayoutType=" + loginLayoutType +
                ", isPollServiceEnabled=" + isPollServiceEnabled +
                ", pollServiceKey='" + pollServiceKey + '\'' +
                ", introMessage=" + introMessage +
                ", loginThemeType=" + loginThemeType +
                ", loginBackgroundColor=" + loginBackgroundColor +
                ", loginPrimaryColor=" + loginPrimaryColor +
                '}';
    }
}

package com.mobiroller.core.constants;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.Tracker;
import com.mobiroller.core.helpers.AppConfigurations;

/**
 * Created by ealtaca on 14.12.2016.
 */

public class Constants {

    public static final String MODULE_HOTEL_VIEW = "aveHotelFlightBookingView";
    public static final String MODULE_ECOMMERCE_VIEW = "aveECommerceView";
    public static final String MODULE_ECOMMERCE_PRO_VIEW = "aveECommerceProView";
    public static final String INTENT_EXTRA_MAIN_MODEL = "liveObj";
    public static final String INTENT_EXTRA_NAVIGATION_MODEL = "navObj";
    public static final String INTENT_EXTRA_INTRO_MESSAGE = "introMsg";
    public static final String INTENT_EXTRA_PUSH_NOTIFIED = "pushNotified";
    public static final String INTENT_EXTRA_LOCAL_NAVIGATION_MODEL = "localObj";
    public static final String INTENT_EXTRA_IS_BACK_AVAILABLE = "isBackAvailable";
    public static final String INTENT_EXTRA_REGISTER_FIRST = "registerFirst";
    public static final String INTENT_EXTRA_SLIDER_MODEL = "SliderModel";
    public static final String INTENT_EXTRA_IS_MAIN_CATEGORY_LIST = "isMainCategoryList";
    public static final String INTENT_EXTRA_SUB_CATEGORY_MODEL = "subCategoryModel";
    public static final String INTENT_EXTRA_CATEGORIES_MOBILE_SETTINGS = "categoriesMobileSettings";
    public static final String INTENT_EXTRA_CATEGORY_MODEL = "categoryModel";
    public static final String INTENT_EXTRA_IS_SLIDER_INTENT = "categorySlider";
    public static final String INTENT_EXTRA_SHOWCASE_MODEL = "showcaseModel";

    public static String E_COMMERCE_SCREEN_ID = "";

    public static final String NEW_LINE = "\n";
    public static final String KEY_SCREEN_ID = "screenId";
    public static final String KEY_SCREEN_TYPE = "screenType";
    public static final String KEY_SUB_SCREEN_TYPE = "screenSubType";
    public static final String KEY_SCREEN_RSS_PUSH_TITLE = "rssPushTitle";
    public static final String KEY_SCREEN_ROLES = "roles";
    public static final String KEY_UPDATE_DATE = "updateDate";
    public static final String KEY_FROM_NO_NETWORK = "fromNoNetwork";
    public static final String KEY_IS_FROM_ACTIVITY = "isFromActivity";
    public static final String KEY_IS_FROM_CLASSIC_MENU = "isFromClassicMenu";
    public static final String KEY_LOCAL_NAVIGATION_MODEL = "localObj";
    public static final String KEY_SHOW_INTRO = "showIntro";
    public static final String KEY_PUSH_NOTIFIED = "pushNotified";

    /**
     * Rss constants
     */
    public static final String KEY_RSS_TITLE = "rssTitle";
    public static final String KEY_RSS_POSITION = "position";
    public static final String KEY_RSS_LAYOUT_TYPE = "layoutType";

    /**
     * App config data (must be changed for each application)
     */
    public static String MobiRoller_Preferences_UserName = "MobiRoller_Preferences_UserName";


    public static String MobiRoller_Preview_UserName = "preview";
    public static String MobiRoller_Preview_Notification_Model = "previewNotification";

    /**
     * Base URL
     */
     public static String MobiRoller_BaseURL = AppConfigurations.Companion.getMobirollerBaseURL();

    public static String Applyze_BaseURL = AppConfigurations.Companion.getApplyzeBaseURL();
    public static String Shopiroller_BaseURL = AppConfigurations.Companion.getShopirollerBaseURL();

    public static String Applyze_ECommerce_BaseURL_Shopiroller = Shopiroller_BaseURL + "";
    public static String Applyze_ECommerce_BaseURL = Applyze_BaseURL + "ecommerce/";

    public static String Applyze_Payment_Complete = "http://applyze-ecommerce-service/v1/paymentSuccess";
    public static String Applyze_Payment_Complete_1 = "https://ecommerce.applyze.com/v2.0/paymentSuccess";
    public static String Applyze_Payment_Complete_2 = "/paymentSuccess";
    public static String Applyze_Payment_Failed = "http://applyze-ecommerce-service/v1/paymentFailure";
    public static String Applyze_Payment_Failed_1 = "https://ecommerce.applyze.com/v2.0/paymentFailure";
    public static String Applyze_Payment_Failed_2 = "/paymentFailure";

    /**
     * Sign Up URL for preview sign up
     */
    public static String MobiRoller_SignUpURL = "https://my.mobiroller.com/Register";

    /**
     * MobiRoller social media link list
     */
    public static String MobiRoller_FacebookURL = "https://www.facebook.com/MobiRoller";
    public static String MobiRoller_FacebookName = "MobiRoller";
    public static String MobiRoller_TwitterURL = "https://www.twitter.com/mobiroller";
    public static String MobiRoller_TwitterName = "mobiroller";
    public static String MobiRoller_GoogleURL = "https://plus.google.com/+Mobiroller";
    public static String MobiRoller_GooglePlusID = "114823517015940083565";
    public static String MobiRoller_LinkedinURL = "https://www.linkedin.com/company/mobiroller";
    public static String MobiRoller_YoutubeURL = "https://m.youtube.com/user/mobiroller";
    public static String MobiRoller_InstagramURL = "https://www.instagram.com/mobiroller";
    public static String MobiRoller_InstagramName = "mobiroller";


    /**
     * General setting
     */
    public static String MobiRoller_Preferences_Assets_Directory_Path = "Files/";
    public static String MobiRoller_Preferences_CharSet = "UTF-8";
    public static String MobiRoller_Preferences_DefaultLang = "MobiRoller_Preferences_DefaultLang";
    public static String MobiRoller_Preferences_Notification = "MobiRoller_Preferences_Notification";
    public static String MobiRoller_Preferences_NotificationSound = "MobiRoller_Preferences_NotificationSound";
    public static String MobiRoller_Preferences_LocaleCodes = "MobiRoller_Preferences_LocaleCodes";
    public static String MobiRoller_Preferences_CurrentLang = "MobiRoller_Preferences_CurrentLang";
    public static String MobiRoller_Preferences_RefreshIntro = "MobiRoller_Preferences_RefreshIntro";
    public static String MobiRoller_Preferences_LanguageSetByUser = "MobiRoller_Preferences_LanguageSetByUser";
    public static String Mobiroller_Preferences_FilePath;
    public static String MobiRoller_Preferences_Expiration_Time = "MobiRoller_Preferences_Expiration_Time";
    public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;
    public static String Mobiroller_Preferences_PackageName_Core_Activities = "com.mobiroller.core.activities";
    public static String Mobiroller_Preferences_PackageName_Chat_Activities = "com.mobiroller.chat.activities";
    public static String Mobiroller_Preferences_PackageName_Youtube_Activities = "com.mobiroller.youtube.activities";
    public static String firstTime = "firstTime";
    public static String MobiRoller_Location_Permission_First_Time = "MobiRoller_Location_Permission_First_Time";
    public static String MobiRoller_Preferences_First_Time_Language = "MobiRoller_Preferences_First_Time_Language";
    public static String isTabActive = "isTabActive";        // Determine if screen contains Tab active or not+
    public static String Mobiroller_Preferences_TabMenu = "Mobiroller_Preferences_TabMenu";
    public static final int TIMEOUT_MILLI_SECONDS = 3000;    // Connection Timeout in milliseconds
    public static final String DISPLAY_MESSAGE_ACTION = "com.mobiroller.preview.DISPLAY_MESSAGE";

    public static final String DISPLAY_LANGUAGE = "Display_Language";
    /**
     * Intent's extra that contains the message to be displayed
     */
    public static final String EXTRA_MESSAGE = "message";

    /**
     * Progress animation type & color
     */
    public static String MobiRoller_Preferences_AskBeforeExit = "MobiRoller_Preferences_AskBeforeExit";
    public static String MobiRoller_Preferences_RateApp = "MobiRoller_Preferences_RateApp";
    public static String MobiRoller_Preferences_ProgressAnimationType = "MobiRoller_Preferences_ProgressAnimationType";
    public static String MobiRoller_Preferences_ProgressAnimationColor = "MobiRoller_Preferences_ProgressAnimationColor";


    public static String MobiRoller_Preferences_ValueSetIndex = "MobiRoller_Preferences_ValueSetIndex";
    public static String MobiRoller_Preferences_UserId = "MobiRoller_Preferences_UserId";
    public static String MobiRoller_Preferences_UserSessionToken = "MobiRoller_Preferences_UserSessionToken";
    public static String MobiRoller_Preferences_PasswordId = "MobiRoller_Preferences_PasswordId";

    /**
     * Firebase Token
     */
    public static String MobiRoller_Preferences_FirebaseToken = "MobiRoller_Preferences_FirebaseToken";


    /**
     * Action Bar Color
     */
    public static String MobiRoller_Preferences_ActionBarColor = "MobiRoller_Preferences_ActionBarColor";

    /**
     * JSON SETTING
     */
    public static int MobiRoller_Preferences_StandardHeight = 568;

    public static String MobiRoller_Preferences_TabHeight = "MobiRoller_Preferences_TabHeight";
    public static String MobiRoller_Preferences_PaddingHeight = "MobiRoller_Preferences_PaddingHeight";

    public static String MobiRoller_Preferences_MotionWidth = "MobiRoller_Preferences_MotionWidth";
    public static String MobiRoller_Preferences_MotionHeight = "MobiRoller_Preferences_MotionHeight";

    public static String MobiRoller_Preferences_MobiRollerBadgeY = "MobiRoller_Preferences_MobiRollerBadgeY";
    public static String MobiRoller_Preferences_MobiRollerBadgeX = "MobiRoller_Preferences_MobiRollerBadgeX";

    /**
     * Base URL for JSON Data
     */
    public static String MobiRoller_Preferences_NAVUrl = "aveNavigation_";
    public static String MobiRoller_Preferences_IN_APP_PURCHASE_URL = "aveInAppPurchase_";

    /**
     * Intro message
     */
    public static String MobiRoller_Preferences_IntroMessage = "MobiRoller_Preferences_IntroMessage";

    /**
     * User password for stage
     */
    public static String MobiRoller_Preferences_Password = "MobiRoller_Preferences_Password";

    /**
     * AdMob Interstitial settings
     */
    public static String MobiRoller_Preferences_Interstitial_Timer = "MobiRoller_Preferences_Interstitial_Timer";
    public static String MobiRoller_Preferences_Interstitial_Click_Count = "MobiRoller_Preferences_Interstitial_Click_Count";
    public static String MobiRoller_Preferences_Interstitial_Click = "MobiRoller_Preferences_Interstitial_Click";

    /**
     * User App Code for stage
     */
    public static String MobiRoller_Preferences_AppCode = "MobiRoller_Preferences_AppCode";
    public static String MobiRoller_Preferences_UserEmail = "MobiRoller_Preferences_UserEmail";
    public static String MobiRoller_Preferences_UserPassword = "MobiRoller_Preferences_UserPassword";
    public static String MobiRoller_Preferences_RememberMe = "MobiRoller_Preferences_RememberMe";

    /**
     * Admob setting
     */
    public static String MobiRoller_Preferences_AdUnitID = "MobiRoller_Preferences_AdUnitID";
    public static String MobiRoller_Preferences_NativeAdUnitID = "MobiRoller_Preferences_NativeAdUnitID";
    public static String MobiRoller_Preferences_BannerAdUnitID = "MobiRoller_Preferences_BannerAdUnitID";
    public static String MobiRoller_Preferences_IsAdEnabled = "MobiRoller_Preferences_IsAdEnabled";
    public static String MobiRoller_Preferences_IsBannerAdEnabled = "MobiRoller_Preferences_IsBannerAdEnabled";
    public static String MobiRoller_Preferences_IsNativeAdEnabled = "MobiRoller_Preferences_IsNativeAdEnabled";
    public static String MobiRoller_Preferences_InterstitialMultipleDisplayEnabled = "MobiRoller_Preferences_InterstitialMultipleDisplayEnabled";

    public static AdView MobiRoller_Preferences_BannerAdView = null;

    /**
     * Preferences for logs
     */
    public static final String PREVIEW_LOGIN_LOG_URL = MobiRoller_BaseURL + "MobiRoller/LogStageLogin";

    /**
     * Preferences for statics
     */
    public static String MobiRoller_Preferences_AndroidVersion = "MobiRoller_Preferences_AndroidVersion";
    public static String MobiRoller_Preferences_DeviceModel = "MobiRoller_Preferences_DeviceModel";
    public static String MobiRoller_Preferences_App_Version = "MobiRoller_Preferences_App_Version";
    public static final String SERVER_SESSION_START_URL = MobiRoller_BaseURL + "AveAnalytics/SessionStart";

    /**
     * Google analytics
     **/
    public static Tracker MobiRoller_Preferences_AnalyticsTracker = null;

    /**
     * Bar color setting
     */
    public static String MobiRoller_Preferences_TitleColor = "Mobiroller_Preferences_TitleColor";
    public static String MobiRoller_Preferences_TitleSelectedColor = "Mobiroller_Preferences_TitleSelectedColor";


    /**
     * Push Notification
     */
    public static String MobiRoller_Preferences_Device_ID = "Mobiroller_Preferences_Device_ID";
    public static String MobiRoller_Preferences_Registration_ID = "MobiRoller_Preferences_Registration_ID";

    /**
     * Twitter OAuth Token
     */
    public static final String USER_TOKEN = "user_token";
    public static final String USER_SECRET = "user_secret";
    public static final String CONSUMER_KEY = "Lnjil7tquAzUefgq7U1xg";
    public static final String CONSUMER_SECRET = "J2RnqX9rodvVd6XjgvxXggTcVJOI5Xgp7z7soBWPQ";
    public static final String CALLBACK_URL = "www.mobiroller.com";

    /**
     * User Login Preferences
     */
    public static String MobiRoller_Preferences_UserLoginStatus = "MobiRoller_Preferences_UserLoginStatus";
    public static String MobiRoller_Preferences_UserLoginNameSurname = "MobiRoller_Preferences_UserLoginNameSurname";
    public static String MobiRoller_Preferences_UserLogin_IsRegisterationActive = "MobiRoller_Preferences_UserLogin_IsRegisterationActive";
    public static String MobiRoller_Preferences_UserLogin_IsLoginActive = "MobiRoller_Preferences_UserLogin_IsLoginActive";
    public static String MobiRoller_Preferences_UserRole = "MobiRoller_Preferences_UserRole";
    public static String MobiRoller_Preferences_LoginThemeType = "MobiRoller_Preferences_LoginThemeType";
    public static String MobiRoller_Preferences_LogoURL = "MobiRoller_Preferences_LogoURL";
    public static String MobiRoller_Preferences_LoginBackgroundURL = "MobiRoller_Preferences_LoginBackgroundURL";
    public static String MobiRoller_Preferences_LoginBackgroundColor = "MobiRoller_Preferences_LoginBackgroundColor";
    public static String MobiRoller_Preferences_LoginPrimaryColor = "MobiRoller_Preferences_LoginPrimaryColor";


    // Chat control
    public static String MobiRoller_Preferences_IsChatAvailable = "MobiRoller_Preferences_IsChatAvailable";
    public static String MobiRoller_Preferences_IsChatValidated = "MobiRoller_Preferences_IsChatValidated";
    public static String MobiRoller_Preferences_UserIsAvailableForChat = "MobiRoller_Preferences_UserIsAvailableForChat";
    public static String MobiRoller_Preferences_UserIsChatAdmin = "MobiRoller_Preferences_UserIsChatAdmin";
    public static String MobiRoller_Preferences_UserIsSuperUser = "MobiRoller_Preferences_UserIsSuperUser";
    public static String MobiRoller_Preferences_UserIsChatMod = "MobiRoller_Preferences_UserIsChatMod";

    // Blesh service control
    public static String MobiRoller_Preferences_BleshServiceStatus = "MobiRoller_Preferences_BleshServiceStatus";

    /**
     * 3rd Party Ads control
     */
    public static String MobiRoller_Preferences_ThirdPartyAdsStatus = "MobiRoller_Preferences_ThirdPartyAdsStatus";
    public static String MobiRoller_Preferences_VideoAdsStatus = "MobiRoller_Preferences_VideoAdsStatus"; // Video Ads
    public static String MobiRoller_Preferences_SplashAdsStatus = "MobiRoller_Preferences_SplashAdsStatus"; // StartApp Splash Ads
    public static String MobiRoller_Preferences_ReturnAdsStatus = "MobiRoller_Preferences_ReturnAdsStatus"; // StartApp Return Ads
    public static String MobiRoller_Preferences_AutoInterstitialStatus = "MobiRoller_Preferences_AutoInterstitialStatus"; // StartApp ads auto interstitial status
    public static String MobiRoller_Preferences_StappAdsAutoInterstitialType = "MobiRoller_Preferences_StappAdsAutoInterstitialType"; // StartApp ads auto interstitial type (time or activities)
    public static String MobiRoller_Preferences_StappAdsAutoInterstitialValue = "MobiRoller_Preferences_StappAdsAutoInterstitialValue"; // StartApp ads auto interstitial type
    public static String MobiRoller_Preferences_PollfishStatus = "MobiRoller_Preferences_PollfishStatus"; // StartApp ads auto interstitial type
    public static String MobiRoller_Preferences_PollfishApiKey = "MobiRoller_Preferences_PollfishApiKey"; // StartApp ads auto interstitial type

    /**
     * Notification badge counter
     **/
    public static String MobiRoller_Preferences_NotificationCount = "MobiRoller_Preferences_NotificationCount";
    /**
     * Login register layout choose
     **/
    public static String MobiRoller_Preferences_LoginRegisterLayoutChoose = "MobiRoller_Preferences_LoginRegisterLayoutChoose";

    /**
     * Select Language on app start
     **/
    public static String MobiRoller_Preferences_SelectLangOnStart = "MobiRoller_Preferences_SelectLangOnStart";


    /* Interstitial Ads settings */
    public static String MobiRoller_Preferences_InterstitialTimeInterval = "MobiRoller_Preferences_InterstitialTimeInterval";
    public static String MobiRoller_Preferences_InterstitialClickInterval = "MobiRoller_Preferences_InterstitialClickInterval";

    public static String MobiRoller_Preferences_Storage_Never_Ask = "MobiRoller_Preferences_Storage_Never_Ask";
    public static String MobiRoller_Preferences_Location_Never_Ask = "MobiRoller_Preferences_Location_Never_Ask";
    public static String MobiRoller_Preferences_Location_Permission_Given = "MobiRoller_Preferences_Location_Permission_Given";
    public static String MobiRoller_Preferences_Location_Permission_Count = "MobiRoller_Preferences_Location_Permission_Count";
    public static String MobiRoller_Preferences_Is_Favorite_Active = "MobiRoller_Preferences_Is_Favorite_Active";
    public static String MobiRoller_Preferences_Google_Sign_Account = "MobiRoller_Preferences_Google_Sign_Account";

    public static String MobiRoller_Preferences_User_Login_Model = "MobiRoller_Preferences_User_Login_Model";

    public static String MobiRoller_Preferences_Chat_Minimum_Version = "MobiRoller_Preferences_Chat_Minimum_Version";
    public static String MobiRoller_Preferences_Chat_Minimum_Version_Setted = "MobiRoller_Preferences_Chat_Minimum_Version_Setted";


    public static String MobiRoller_Preferences_Youtube_Channel_Detail = "MobiRoller_Preferences_Youtube_Channel_Detail";


    public static String MobiRoller_Preferences_IsUserAgremeentActive = "MobiRoller_Preferences_IsUserAgremeentActive";
    public static String MobiRoller_Preferences_UserAgremeent = "MobiRoller_Preferences_UserAgremeent";

    public static String MobiRoller_Preferences_IsGoogleSignInActive = "MobiRoller_Preferences_IsGoogleSignInActive";

    public static String MobiRoller_Preferences_IsYoutubeChannelSubscribed = "MobiRoller_Preferences_IsYoutubeChannelSubscribed";

    public static String MobiRoller_Preferences_InAppPurchaseActive = "MobiRoller_Preferences_InAppPurchaseActive";

    public static String MobiRoller_Preferences_FCM_Token = "MobiRoller_Preferences_FCM_Token";


    public static String MobiRoller_Preferences_Show_MobiRoller_Badge = "MobiRoller_Preferences_Show_MobiRoller_Badge";
    public static String MobiRoller_Preferences_MobiRoller_Badge_Url = "MobiRoller_Preferences_MobiRoller_Badge_Url";

    public static String MobiRoller_Preferences_MobiRoller_Badge_Total_Count = "MobiRoller_Preferences_MobiRoller_Badge_Total_Count";
    public static String MobiRoller_Preferences_MobiRoller_Badge_First_Open = "MobiRoller_Preferences_MobiRoller_Badge_First_Open";
    public static String MobiRoller_Preferences_MobiRoller_Badge_Count = "MobiRoller_Preferences_MobiRoller_Badge_Count";
    public static String MobiRoller_Preferences_MobiRoller_Badge_Check_Response = "MobiRoller_Preferences_MobiRoller_Badge_Check_Response";

    public static String NOTIFICATION_CONTENT = "content";
    public static String NOTIFICATION_TYPE_CONTENT = "content";
    public static String NOTIFICATION_TYPE_WEB = "web";
    public static String NOTIFICATION_TYPE_APP = "app";
    public static String NOTIFICATION_TYPE_WEB_CONTENT = "webContent";
    public static String NOTIFICATION_INTERNAL = "internal";
    public static String NOTIFICATION_EXTERNAL = "external";

    public static final String SLIDER_TYPE_WEB = "Web";
    public static final String SLIDER_TYPE_PRODUCT = "Product";
    public static final String SLIDER_TYPE_CATEGORY = "Category";
    public static final String SLIDER_TYPE_NONE = "None";

    /**
     * Tourvisio Mobiroller User Id
     **/

    public static final String TOURVISIO_MOBIROLLER_USER_ID = "mobirollerUserId";
    public static final String TOURVISIO_B2C_BUSINESS_TYPE = "B2C";
    public static final String TOURVISIO_B2B_BUSINESS_TYPE = "B2B";

}

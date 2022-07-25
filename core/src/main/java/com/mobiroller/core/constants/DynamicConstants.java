package com.mobiroller.core.constants;

public class DynamicConstants {

    /**
     * MobiRoller Application Type (stage or normal app)
     */
    public static boolean MobiRoller_Stage = false;
    public static String AppKey = "";

    public static String TOPIC_ALL_DEVICES = "ALL_DEVICES";
    public static String TOPIC_FREE_PACKAGE = "FREE_PACKAGE";
    public static String TOPIC_TRIAL_PACKAGE = "TRIAL_PACKAGE";
    public static String TOPIC_ECO_PACKAGE = "ECO_PACKAGE";
    public static String TOPIC_PRO_PACKAGE = "PRO_PACKAGE";
    public static String TOPIC_BUSINESS_PACKAGE = "BUSINESS_PACKAGE";
    public static String TOPIC_RESELLER_PACKAGE = "RESELLER_PACKAGE";
    public static String TOPIC_PREMIUM_MEMBER = "PREMIUM_MEMBER";
    public static String TOPIC_FREE_MEMBER = "FREE_MEMBER";
    public static String[] TOPIC_LIST = new String[]{
            TOPIC_FREE_PACKAGE,
            TOPIC_ECO_PACKAGE,
            TOPIC_PRO_PACKAGE,
            TOPIC_BUSINESS_PACKAGE,
            TOPIC_RESELLER_PACKAGE,
            TOPIC_PREMIUM_MEMBER,
            TOPIC_FREE_MEMBER,
            TOPIC_TRIAL_PACKAGE
    };


    public static String IDENTITY_SERVICE_BASE_URL = "https://identity.applyze.com";
    public static String IDENTITY_SERVICE_VERSION_2_1 = "2.1";

    public static String LEGACY_SERVICE_BASE_URL = "https://legacy.mobiroller.com";
    public static String LEGACY_SERVICE_VERSION_1 = "1";


}

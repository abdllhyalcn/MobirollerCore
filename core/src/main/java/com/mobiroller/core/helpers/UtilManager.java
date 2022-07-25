package com.mobiroller.core.helpers;

import android.content.Context;

import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;

import java.util.Locale;

public class UtilManager {

    private static SharedPrefHelper sharedPrefHelper;
    private static NetworkHelper networkHelper;
    private static ApiRequestManager apiRequestManager;
    private static LocalizationHelper localizationHelper;

    private static void init(Context context) {

        new JSONStorage.Builder()
                .setContext(context)
                .build();

        sharedPrefHelper = new SharedPrefHelper(context);
        if(!UtilManager.sharedPrefHelper().getLanguageSetByUser())
            UtilManager.sharedPrefHelper().setDisplayLanguage(Locale.getDefault().getLanguage().toLowerCase());
        networkHelper = new NetworkHelper(context);
        localizationHelper = new LocalizationHelper();
        apiRequestManager = new ApiRequestManager();


        new AsyncRequestHelper.Builder()
                .setContext(context)
                .build();

        new InAppPurchaseHelper.Builder()
                .setContext(context)
                .build();
    }

    public static SharedPrefHelper sharedPrefHelper() {
        if (sharedPrefHelper == null)
            sharedPrefHelper = new SharedPrefHelper(SharedApplication.context);
        return sharedPrefHelper;
    }

    public static NetworkHelper networkHelper() {
        if (networkHelper == null)
            networkHelper = new NetworkHelper(SharedApplication.context);
        return networkHelper;
    }

    public static LocalizationHelper localizationHelper() {
        if (localizationHelper == null)
            localizationHelper = new LocalizationHelper();
        return localizationHelper;
    }

    public static ApiRequestManager apiRequestManager() {
        if (apiRequestManager == null)
            apiRequestManager = new ApiRequestManager();
        return apiRequestManager;
    }

    /**
     * Builder class for the EasyPrefs instance. You only have to call this once in the Application
     * onCreate. And in the rest of the code base you can call Prefs.method name.
     */
    public final static class Builder {

        private Context mContext;

        /**
         * Set the Context used to instantiate the SharedPreferences
         *
         * @param context the application context
         * @return the {@link JSONStorage.Builder} object.
         */
        public UtilManager.Builder setContext(final Context context) {
            mContext = context;
            return this;
        }

        /**
         * Initialize the SharedPreference instance to used in the application.
         *
         * @throws RuntimeException if Context has not been set.
         */
        public void build() {
            if (mContext == null) {
                throw new RuntimeException("Context not set, please set context before building the Prefs instance.");
            }

            init(mContext);

        }

    }


}

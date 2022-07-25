package com.mobiroller.core.helpers;

import android.content.Context;

import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.util.JSONStorageCore;
import com.mobiroller.core.models.InAppPurchaseModel;
import com.mobiroller.core.models.MainModel;
import com.mobiroller.core.models.MobirollerBadgeModel;
import com.mobiroller.core.models.NavigationModel;

import io.paperdb.Paper;
import timber.log.Timber;

import static com.esotericsoftware.minlog.Log.LEVEL_TRACE;


public class JSONStorage extends JSONStorageCore {

    private final static String TAG = "JSONStorage";
    private final static String PAPER_SCREEN_BOOK_TAG = "screens";
    private final static String MAIN_MODEL_KEY = "MainModel";
    private final static String NAVIGATION_MODEL_KEY = "NavigationModel";
    private final static String IN_APP_PURCHASE_MODEL_KEY = "InAppPurchaseModel";
    private final static String MOBIROLLER_BADGE_MODEL_KEY = "MobirollerBadgeModel";

    private static void initStorage(Context context) {
        Paper.init(context);
//        Paper.setLogLevel(LEVEL_TRACE);
    }

    public static MainModel getMainModel() {
        if (Paper.book(PAPER_SCREEN_BOOK_TAG).contains(MAIN_MODEL_KEY)) {
            try {
                return Paper.book(PAPER_SCREEN_BOOK_TAG).read(MAIN_MODEL_KEY);
            }catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static void putMainModel(MainModel mainModel) {
        putObject(PAPER_SCREEN_BOOK_TAG,MAIN_MODEL_KEY,mainModel);
    }

    public static MobirollerBadgeModel getMobirollerBadgeModel() {
        if (Paper.book(PAPER_SCREEN_BOOK_TAG).contains(MOBIROLLER_BADGE_MODEL_KEY)) {
            try {
                return Paper.book(PAPER_SCREEN_BOOK_TAG).read(MOBIROLLER_BADGE_MODEL_KEY);
            }catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    static void putMobirollerBadgeModel(MobirollerBadgeModel mobirollerBadgeModel) {
        if(mobirollerBadgeModel.displayRate == -1)
            UtilManager.sharedPrefHelper().put(Constants.MobiRoller_Preferences_Show_MobiRoller_Badge,false);
        int totalCount = UtilManager.sharedPrefHelper().getInt(Constants.MobiRoller_Preferences_MobiRoller_Badge_Total_Count, -1);
        if (mobirollerBadgeModel.displayRate != totalCount && mobirollerBadgeModel.displayRate != 0) {
            UtilManager.sharedPrefHelper().put(Constants.MobiRoller_Preferences_MobiRoller_Badge_Count, 0);
            UtilManager.sharedPrefHelper().put(Constants.MobiRoller_Preferences_MobiRoller_Badge_Total_Count, mobirollerBadgeModel.displayRate);
        }
        Paper.book(PAPER_SCREEN_BOOK_TAG).write(MOBIROLLER_BADGE_MODEL_KEY, mobirollerBadgeModel);
    }


    public static NavigationModel getNavigationModel() {
        if (Paper.book(PAPER_SCREEN_BOOK_TAG).contains(NAVIGATION_MODEL_KEY)) {
            try {
                return Paper.book(PAPER_SCREEN_BOOK_TAG).read(NAVIGATION_MODEL_KEY);
            }catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static void putNavigationModel(NavigationModel navigationModel) {
        if(navigationModel != null) {
            putObject(PAPER_SCREEN_BOOK_TAG, NAVIGATION_MODEL_KEY, navigationModel.getConfiguredNavigationModel());
        } else {
            Timber.tag(TAG).d( "NavigationModel object is NULL");
        }
    }


    public static InAppPurchaseModel getInAppPurchaseModel() {
        if (Paper.book(PAPER_SCREEN_BOOK_TAG).contains(IN_APP_PURCHASE_MODEL_KEY)) {
            try {
                return Paper.book(PAPER_SCREEN_BOOK_TAG).read(IN_APP_PURCHASE_MODEL_KEY);
            }catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static void putInAppPurchase(InAppPurchaseModel inAppPurchaseModel) {
        putObject(PAPER_SCREEN_BOOK_TAG,IN_APP_PURCHASE_MODEL_KEY,inAppPurchaseModel);
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
        public Builder setContext(final Context context) {
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

            initStorage(mContext);
        }

    }

}

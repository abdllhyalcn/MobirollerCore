package com.mobiroller.core.helpers;

import android.content.Context;

import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.models.InAppPurchaseModel;
import com.mobiroller.core.models.MobirollerBadgeModel;
import com.mobiroller.core.models.events.MobirollerBadgeEvent;
import com.mobiroller.core.serviceinterfaces.MobirollerServiceInterface;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsyncRequestHelper {

    public static Context mContext;
    public static NetworkHelper networkHelper;
    public static SharedPrefHelper sharedPrefHelper;
    public static JSONParser jParserNew;
    private static RequestHelper mRequestHelper;
    private static ApiRequestManager mApiRequestHelper;

    /**
     * Builder class for the EasyPrefs instance. You only have to call this once in the Application
     * onCreate. And in the rest of the code base you can call Prefs.method name.
     */
    public final static class Builder {


        /**
         * Set the Context used to instantiate the SharedPreferences
         *
         * @param context the application context
         * @return the {@link InAppPurchaseHelper.Builder} object.
         */
        public AsyncRequestHelper.Builder setContext(final Context context) {
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
            networkHelper = new NetworkHelper(mContext);
            sharedPrefHelper = UtilManager.sharedPrefHelper();
            mRequestHelper = new RequestHelper(mContext, sharedPrefHelper);
            mApiRequestHelper = new ApiRequestManager(sharedPrefHelper, mRequestHelper);
            jParserNew = new JSONParser(new FileDownloader(), mApiRequestHelper, networkHelper);
        }
    }


    public static void getInAppPurchaseItems() {
        if (networkHelper.isConnected()) {
            MobirollerServiceInterface mobirollerServiceInterface = new RequestHelper(mContext, sharedPrefHelper).getAPIService();
            mobirollerServiceInterface.getInAppPurchaseProducts("aveInAppPurchase_" + AppConfigurations.Companion.getAccountEmail()).enqueue(new Callback<InAppPurchaseModel>() {
                @Override
                public void onResponse(Call<InAppPurchaseModel> call, Response<InAppPurchaseModel> response) {
                    if (response.isSuccessful()) {
                        JSONStorage.putInAppPurchase(response.body());
                    }
                }

                @Override
                public void onFailure(Call<InAppPurchaseModel> call, Throwable t) {
                    JSONStorage.putInAppPurchase(getInAppPurchaseModelFromLocal());
                }
            });
        } else {
            JSONStorage.putInAppPurchase(getInAppPurchaseModelFromLocal());
        }
    }

    public static void getBadgeModel(String url) {
        if (networkHelper.isConnected()) {
            MobirollerServiceInterface mobirollerServiceInterface = new RequestHelper(mContext, sharedPrefHelper).getAPIService();
            mobirollerServiceInterface.getBadgeInfo(url).enqueue(new Callback<MobirollerBadgeModel>() {
                @Override
                public void onResponse(Call<MobirollerBadgeModel> call, Response<MobirollerBadgeModel> response) {
                    if (response.isSuccessful()) {
                        JSONStorage.putMobirollerBadgeModel(response.body());
                        UtilManager.sharedPrefHelper().put(Constants.MobiRoller_Preferences_MobiRoller_Badge_Check_Response,true);
                        EventBus.getDefault().post(new MobirollerBadgeEvent());
                    }
                }

                @Override
                public void onFailure(Call<MobirollerBadgeModel> call, Throwable t) {
                    UtilManager.sharedPrefHelper().put(Constants.MobiRoller_Preferences_MobiRoller_Badge_Check_Response,true);
                    if (JSONStorage.getMobirollerBadgeModel() == null) {
                        UtilManager.sharedPrefHelper().put(Constants.MobiRoller_Preferences_Show_MobiRoller_Badge,false);
                    }
                    EventBus.getDefault().post(new MobirollerBadgeEvent());
                }
            });
        } else {
            UtilManager.sharedPrefHelper().put(Constants.MobiRoller_Preferences_MobiRoller_Badge_Check_Response,true);
            if (JSONStorage.getMobirollerBadgeModel() == null) {
                UtilManager.sharedPrefHelper().put(Constants.MobiRoller_Preferences_Show_MobiRoller_Badge,false);
            }
            EventBus.getDefault().post(new MobirollerBadgeEvent());
        }
    }


    /**
     * Get main model from storage or resource
     *
     * @return Main Model
     */
    private static InAppPurchaseModel getInAppPurchaseModelFromLocal() {
        InAppPurchaseModel inAppPurchaseModel = JSONStorage.getInAppPurchaseModel();
        if (inAppPurchaseModel == null)
            inAppPurchaseModel = getInAppPurchaseModelFromAssets();
        return inAppPurchaseModel;
    }


    private static InAppPurchaseModel getInAppPurchaseModelFromAssets() {
        InAppPurchaseModel localObj = null;
        localObj = jParserNew.getLocalInAppPurchaseJSON(mContext, sharedPrefHelper.getUsername());
        JSONStorage.putInAppPurchase(localObj);
        return localObj;
    }

}

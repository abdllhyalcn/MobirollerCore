package com.mobiroller.core.helpers;

import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.models.MainModel;
import com.mobiroller.core.models.NavigationModel;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.serviceinterfaces.MobirollerServiceInterface;
import com.mobiroller.core.serviceinterfaces.MobirollerServicePreviewInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by ealtaca on 08.03.2017.
 */

public class ApiRequestManager {

    private SharedPrefHelper sharedPrefHelper;
    private MobirollerServiceInterface mobirollerServiceInterface;
    private MobirollerServicePreviewInterface mobirollerServicePreviewInterface;

    public ApiRequestManager(SharedPrefHelper sharedPrefHelper, RequestHelper requestHelper) {
        this.sharedPrefHelper = sharedPrefHelper;
        if (DynamicConstants.MobiRoller_Stage)
            this.mobirollerServicePreviewInterface = requestHelper.getPreviewAPIService();
        this.mobirollerServiceInterface = requestHelper.getAPIService();
    }

    public ApiRequestManager() {
        this.sharedPrefHelper = UtilManager.sharedPrefHelper();
        RequestHelper requestHelper = new RequestHelper();
        if (DynamicConstants.MobiRoller_Stage)
            this.mobirollerServicePreviewInterface = requestHelper.getPreviewAPIService();
        this.mobirollerServiceInterface = requestHelper.getAPIService();
    }


    public MainModel getMainJSON(String url) {
        MainModel mainModel = null;
        Call<MainModel> call;
        if (DynamicConstants.MobiRoller_Stage)
            call = mobirollerServicePreviewInterface.getMainJSON(url);
        else
            call = mobirollerServiceInterface.getMainJSON(url);
        try {
            mainModel = call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainModel;
    }

    public Call<MainModel> getMainJSONAsync(String url) {
        Call<MainModel> call;
        if (DynamicConstants.MobiRoller_Stage)
            call = mobirollerServicePreviewInterface.getMainJSON(url);
        else
            call = mobirollerServiceInterface.getMainJSON(url);
        return call;
    }

    public NavigationModel getNavigationJSON(String url) {
        NavigationModel navigationModel = null;
        Call<NavigationModel> call;

        if (DynamicConstants.MobiRoller_Stage)
            call = mobirollerServicePreviewInterface.getNavigationJSON(Constants.MobiRoller_Preferences_NAVUrl + url);
        else
            call = mobirollerServiceInterface.getNavigationJSON(Constants.MobiRoller_Preferences_NAVUrl + url);

        try {
            navigationModel = call.execute().body();
            JSONStorage.putNavigationModel(navigationModel);
        } catch (Exception e) {
            navigationModel = null;
            e.printStackTrace();
        }
        return navigationModel;
    }

    public Call<NavigationModel> getNavigationJSONAsync(String url) {
        Call<NavigationModel> call;
        if (DynamicConstants.MobiRoller_Stage)
            call = mobirollerServicePreviewInterface.getNavigationJSON(Constants.MobiRoller_Preferences_NAVUrl + url);
        else
            call = mobirollerServiceInterface.getNavigationJSON(Constants.MobiRoller_Preferences_NAVUrl + url);

        return call;
    }

    public ScreenModel getScreenJSON(String screenId) {
        ScreenModel screenModel;
        Call<ScreenModel> call;

        if (DynamicConstants.MobiRoller_Stage)
            call = mobirollerServicePreviewInterface.getScreenJSON(screenId);
        else
            call = mobirollerServiceInterface.getScreenJSON(screenId);
        try {
            screenModel = call.execute().body();
            JSONStorage.putScreenModel(screenId,screenModel);
        } catch (Exception e) {
            screenModel = null;
            e.printStackTrace();
        }
        return screenModel;
    }

    public Call<ScreenModel> getScreenJSONAsync(String url) {
        Call<ScreenModel> call;
        if (DynamicConstants.MobiRoller_Stage)
            call = mobirollerServicePreviewInterface.getScreenJSON(url);
        else
            call = mobirollerServiceInterface.getScreenJSON(url);
        return call;
    }

    public void sendFeedback(String message, int rating) {
        Call<ResponseBody> call = mobirollerServiceInterface.sendFeedback(sharedPrefHelper.getUsername(), message, rating);
        try {
            call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIPAddress() {
        Call<String> call = mobirollerServiceInterface.getIpAddress();
        try {
            return call.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

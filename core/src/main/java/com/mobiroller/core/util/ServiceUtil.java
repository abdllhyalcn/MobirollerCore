package com.mobiroller.core.util;

import com.mobiroller.core.MenuHelper;
import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.helpers.ApiRequestManager;
import com.mobiroller.core.helpers.JSONParser;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.NavigationItemModel;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.util.exceptions.IntentException;
import com.mobiroller.core.util.exceptions.UserFriendlyException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceUtil {

    private int itemPosition;
    private ScreenModel screenModel;
    private ServiceInterface serviceInterface;

    public void fetchScreenDetails(NavigationItemModel navigationItemModel,ServiceInterface serviceInterface,int itemPosition) {
        screenModel = null;
        this.serviceInterface = serviceInterface;
        this.itemPosition = itemPosition;

        if (DynamicConstants.MobiRoller_Stage) {
            if (UtilManager.networkHelper().isConnected()) {
                getLiveScreenModelAsync(navigationItemModel);
            } else {
                serviceInterface.onThrowIntentException(new IntentException(MobirollerIntent.getConnectionRequiredIntent(navigationItemModel)));
            }
        } else {
            //Local Screen Model
            screenModel = getLocalScreenModel(navigationItemModel);
            if (UtilManager.networkHelper().isConnected()) {
                if (screenModel != null
                        && screenModel.getUpdateDate() != null
                        && DateUtil.dateControlString(screenModel.getUpdateDate(), navigationItemModel.getUpdateDate())) {
                    handleResult(navigationItemModel,screenModel);
                } else {
                    getLiveScreenModelAsync(navigationItemModel);
                }
            } else {
                handleResult(navigationItemModel,screenModel);
            }

        }
    }

    private void getLiveScreenModelAsync(final NavigationItemModel navigationItemModel) {

        ApiRequestManager requestManager = new ApiRequestManager();
        requestManager.getScreenJSONAsync(navigationItemModel.getAccountScreenID()).enqueue(new Callback<ScreenModel>() {
            @Override
            public void onResponse(Call<ScreenModel> call, Response<ScreenModel> response) {
                if (response.body() != null) {
                    screenModel = response.body();
                    handleResult(navigationItemModel,screenModel);
                } else {
                    getLocalScreenModelAndDisplay(navigationItemModel);
                }
            }

            @Override
            public void onFailure(Call<ScreenModel> call, Throwable t) {
                getLocalScreenModelAndDisplay(navigationItemModel);
            }
        });
    }

    private void getLocalScreenModelAndDisplay(NavigationItemModel navigationItemModel) {
        if (DynamicConstants.MobiRoller_Stage)
            serviceInterface.onThrowIntentException(new IntentException(MobirollerIntent.getConnectionRequiredIntent(navigationItemModel)));
        else {
            screenModel = getLocalScreenModel(navigationItemModel);
            handleResult(navigationItemModel,screenModel);
        }
    }

    private void handleResult(NavigationItemModel navigationItemModel, ScreenModel screenModel)
    {
        try {
            serviceInterface.onScreenModelFetched(new MenuHelper().getScreenModel(navigationItemModel, screenModel),itemPosition);
        } catch (UserFriendlyException e) {
            serviceInterface.onThrowUserFriendlyException(e);
        } catch (IntentException e) {
            serviceInterface.onThrowIntentException(e);
        }
    }

    private ScreenModel getLocalScreenModel(NavigationItemModel navigationItemModel) {
        return new JSONParser().getLocalScreenModel(SharedApplication.context, navigationItemModel.getAccountScreenID());
    }

    public interface ServiceInterface{
        void onScreenModelFetched(ScreenModel screenModel,int position);
        void onThrowUserFriendlyException(UserFriendlyException userFriendlyException);
        void onThrowIntentException(IntentException intentException);
    }

}

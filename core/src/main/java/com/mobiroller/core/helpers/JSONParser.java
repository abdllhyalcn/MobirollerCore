package com.mobiroller.core.helpers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.models.InAppPurchaseModel;
import com.mobiroller.core.models.MainModel;
import com.mobiroller.core.models.NavigationModel;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.util.DateUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import timber.log.Timber;

/**
 * Created by ealtaca on 07.03.2017.
 */

public class JSONParser {

    private FileDownloader fileDownloader;
    private ApiRequestManager requestManager;

    public JSONParser(FileDownloader fileDownloader, ApiRequestManager requestManager, NetworkHelper networkHelper) {
        this.fileDownloader = fileDownloader;
        this.requestManager = requestManager;
    }

    public JSONParser() {
        this.fileDownloader = new FileDownloader();
        this.requestManager = new ApiRequestManager();
    }

    public ScreenModel getScreenJSONFromLocalByID(Context context, String accountScreenID, boolean isUpdate, boolean isOnline, boolean isCopyLocal) {
        if (DynamicConstants.MobiRoller_Stage) {
            return getPreviewJSON(accountScreenID);
        } else {
            try {
                ScreenModel screenModel = JSONStorage.getScreenModel(accountScreenID);
                ScreenModel liveMainModel = null;
                if (!isUpdate || (SharedApplication.jsonIdList != null && !SharedApplication.jsonIdList.contains(accountScreenID))) {
                    if (isOnline && !isCopyLocal) {
                        liveMainModel = requestManager.getScreenJSON(accountScreenID);
                        if (liveMainModel != null) {
                            if (screenModel == null || !isUpdate) {
                                JSONStorage.putScreenModel(accountScreenID, liveMainModel);
                            }
                        }
                        SharedApplication.jsonIdList.add(accountScreenID);
                    }
                }
                if (screenModel == null && !isCopyLocal) {
                    fileDownloader.copyMainLocalJSONFile(context, accountScreenID);
                    return getScreenJSONFromLocalByID(context, accountScreenID, isUpdate, isOnline, true);
                }
                screenModel = JSONStorage.getScreenModel(accountScreenID);
                if (isOnline && liveMainModel != null && !DateUtil.dateControlString(screenModel.getUpdateDate(), liveMainModel.getUpdateDate())) {
                    return getScreenJSONFromLocalByID(context, accountScreenID, false, true, false);
                }
                return screenModel;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * @param context         context
     * @param accountScreenID screenId
     * @param isOnline        connection status
     * @param updateDate      last update date (which comes from parent menu json)
     * @return
     */
    public ScreenModel getScreenJSONFromLocalByID(Context context, String accountScreenID, boolean isOnline, String updateDate) {
        Timber.tag("JSON OP").d("================================");
        Timber.tag("JSON OP").d("getScreenJSONFromLocalByID Started");
        Timber.tag("JSON OP").d("ScreenId: %1s ",accountScreenID);
        if (DynamicConstants.MobiRoller_Stage) {
            Timber.tag("JSON OP").d("STAGE");
            return getPreviewJSON(accountScreenID);
        } else {
            try {
                ScreenModel screenModel = JSONStorage.getScreenModel(accountScreenID);
                ScreenModel liveScreenModel = null;

                try {
                    liveScreenModel = isLocalJSONLatest(updateDate, context, accountScreenID);
                    if (liveScreenModel != null) {
                        return liveScreenModel;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if ( (SharedApplication.jsonIdList != null && !SharedApplication.jsonIdList.contains(accountScreenID))) {
                    if (isOnline) {
                        Timber.tag("JSON OP").d("Device is Online");
                        Timber.tag("JSON OP").d("Fetching json from online");
                        liveScreenModel = requestManager.getScreenJSON(accountScreenID);
                        if (liveScreenModel != null) {
                            if (screenModel == null) {
                                screenModel = liveScreenModel;
                                JSONStorage.putScreenModel(accountScreenID, liveScreenModel);
                            }
                        }
                        SharedApplication.jsonIdList.add(accountScreenID);
                    }
                }
                if (screenModel == null) {
                    Timber.tag("JSON OP").d("Fetching json from assets");
                    fileDownloader.copyMainLocalJSONFile(context, accountScreenID);
                    return getScreenJSONFromLocalByID(context, accountScreenID, true, isOnline, true);
                }
                screenModel = JSONStorage.getScreenModel(accountScreenID);
                if (isOnline && liveScreenModel != null) {
                    if (!DateUtil.dateControlString(screenModel.getUpdateDate(), liveScreenModel.getUpdateDate())) {
                        return getScreenJSONFromLocalByID(context, accountScreenID, false, isOnline, false);
                    }
                }
                return screenModel;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     *
     * @param updateDate last updated date
     * @param context context
     * @param accountScreenID screenId
     * @return ScreenModel
     */
    private ScreenModel isLocalJSONLatest(String updateDate, Context context, String accountScreenID) {
        ScreenModel screenModel = JSONStorage.getScreenModel(accountScreenID);

        if (screenModel == null) {
            screenModel = fileDownloader.copyScreenLocalJSONFile(context, accountScreenID);
        }

        if(updateDate == null)
            return screenModel;

        if (screenModel != null) {
            if (DateUtil.dateControlString(screenModel.getUpdateDate(), updateDate)) {
                return screenModel;
            }
        }

        return null;
    }

    public ScreenModel getLocalScreenModel(Context context, String accountScreenID) {
        ScreenModel screenModel = JSONStorage.getScreenModel(accountScreenID);
        if (screenModel != null) {
            return screenModel;
        } else {
            screenModel = fileDownloader.copyScreenLocalJSONFile(context, accountScreenID);
        }

        return screenModel;
    }

    public NavigationModel getLocalNavigationJSON(Context context, String accountScreenID) {
        NavigationModel navigationModel = null;
        try {
            navigationModel = JSONStorage.getNavigationModel();
            if (navigationModel == null) {
                fileDownloader.copyNavigationLocalJSONFile(context, accountScreenID);
            }
            navigationModel = JSONStorage.getNavigationModel();
            return navigationModel;
        } catch (Exception e) {
            return null;
        }
    }


    public InAppPurchaseModel getLocalInAppPurchaseJSON(Context context, String accountScreenID) {
        InAppPurchaseModel inAppPurchaseModel = null;
        try {
            inAppPurchaseModel = JSONStorage.getInAppPurchaseModel();
            if (inAppPurchaseModel == null) {
                fileDownloader.copyInAppPurchaseLocalJSONFile(context, accountScreenID);
            }
            inAppPurchaseModel = JSONStorage.getInAppPurchaseModel();
            return inAppPurchaseModel;
        } catch (Exception e) {
            return null;
        }
    }


    public MainModel getJSONMainOffline(InputStream in) {
        BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
        JsonReader reader = new JsonReader(bReader);
        return new Gson().fromJson(reader, MainModel.class);
    }


    public NavigationModel getNavigationJSONFromLocal(Context context, String accountScreenID, boolean isUpdate, boolean isOnline, boolean isCopyLocal) {
        NavigationModel navigationModel = null;
        if (DynamicConstants.MobiRoller_Stage) {
            return requestManager.getNavigationJSON(accountScreenID);
        } else {
            try {
                navigationModel = JSONStorage.getNavigationModel();
                NavigationModel liveJson = null;
                if (!isUpdate || (SharedApplication.jsonIdList != null && !SharedApplication.jsonIdList.contains(Constants.MobiRoller_Preferences_NAVUrl + accountScreenID))) {
                    if (isOnline && !isCopyLocal) {
                        liveJson = requestManager.getNavigationJSON(accountScreenID);
                        if (liveJson != null) {
                            if (navigationModel == null || !isUpdate) {
                                JSONStorage.putNavigationModel(liveJson);
                            }
                        }
                        SharedApplication.jsonIdList.add(Constants.MobiRoller_Preferences_NAVUrl + accountScreenID);
                    }
                }
                if (navigationModel == null && !isCopyLocal) {
                    fileDownloader.copyNavigationLocalJSONFile(context, accountScreenID);
                    return getNavigationJSONFromLocal(context, accountScreenID, isUpdate, isOnline, true);
                }
                navigationModel = JSONStorage.getNavigationModel();
                if (isOnline && liveJson != null) {
                    if (!DateUtil.dateControlString(navigationModel.getUpdateDate(), liveJson.getUpdateDate())) {
                        return getNavigationJSONFromLocal(context, accountScreenID, false, isOnline, false);
                    }
                }
            } catch (Exception e) {
                return null;
            }
            return navigationModel;
        }
    }

    private ScreenModel getPreviewJSON(String screenId){
        return requestManager.getScreenJSON(screenId);
    }

}

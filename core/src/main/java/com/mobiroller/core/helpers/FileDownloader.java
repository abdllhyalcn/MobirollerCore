package com.mobiroller.core.helpers;

import android.content.Context;

import com.google.gson.Gson;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.models.InAppPurchaseModel;
import com.mobiroller.core.models.MainModel;
import com.mobiroller.core.models.NavigationModel;
import com.mobiroller.core.coreui.models.ScreenModel;

import java.io.File;
import java.io.InputStream;

import timber.log.Timber;

/**
 * Created by ealtaca on 07.03.2017.
 */

public class FileDownloader {

    public FileDownloader() {
    }

    ScreenModel copyScreenLocalJSONFile(Context context, String name) {
        String fileName = name + ".json";
        try {
            Timber.tag("JSON OP").d("=========================");
            Timber.tag("JSON OP").d("Assets - Screen Json");
            Timber.tag("JSON OP").d("Fetching json from assets");
            InputStream is = context.getAssets().open(Constants.MobiRoller_Preferences_Assets_Directory_Path + fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            Gson gson = new Gson();
            String json = new String(buffer, Constants.MobiRoller_Preferences_CharSet);
            ScreenModel screenModel = gson.fromJson(json, ScreenModel.class);
            JSONStorage.putScreenModel(name, screenModel);
            return screenModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    void copyMainLocalJSONFile(Context context, String name) {
        String fileName = name + ".json";
        try {
            Timber.tag("JSON OP").d("=========================");
            Timber.tag("JSON OP").d("Assets - Main Json");
            Timber.tag("JSON OP").d("Fetching json from assets");
            InputStream is = context.getAssets().open(Constants.MobiRoller_Preferences_Assets_Directory_Path + fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            Gson gson = new Gson();
            String json = new String(buffer, Constants.MobiRoller_Preferences_CharSet);
            MainModel mainModel = gson.fromJson(json, MainModel.class);
            JSONStorage.putMainModel(mainModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void copyNavigationLocalJSONFile(Context context, String name) {
        String fileName = Constants.MobiRoller_Preferences_NAVUrl + name + ".json";
        try {
            Timber.tag("JSON OP").d("=========================");
            Timber.tag("JSON OP").d("Assets - Navigation Json");
            Timber.tag("JSON OP").d("Fetching json from assets");
            InputStream is = context.getAssets().open(Constants.MobiRoller_Preferences_Assets_Directory_Path + fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            Gson gson = new Gson();
            String json = new String(buffer, Constants.MobiRoller_Preferences_CharSet);
            NavigationModel navigationModel = gson.fromJson(json, NavigationModel.class);
            JSONStorage.putNavigationModel(navigationModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void copyInAppPurchaseLocalJSONFile(Context context, String name) {
        String fileName = Constants.MobiRoller_Preferences_IN_APP_PURCHASE_URL + name + ".json";
        try {

            Timber.tag("JSON OP").d("=========================");
            Timber.tag("JSON OP").d("Assets - In App Purchase Json");
            Timber.tag("JSON OP").d("Fetching json from assets");
            InputStream is = context.getAssets().open(Constants.MobiRoller_Preferences_Assets_Directory_Path + fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            Gson gson = new Gson();
            String json = new String(buffer, Constants.MobiRoller_Preferences_CharSet);
            InAppPurchaseModel inAppPurchaseModel = gson.fromJson(json, InAppPurchaseModel.class);
            JSONStorage.putInAppPurchase(inAppPurchaseModel);
        } catch (Exception e) {
            Timber.tag("JSON OP").d("Fetching json from assets");
            e.printStackTrace();
        }
    }

    public void clearLocalFiles(Context context) {
        String path = Constants.Mobiroller_Preferences_FilePath + "/";
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

}

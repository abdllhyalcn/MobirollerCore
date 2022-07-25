package com.mobiroller.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.activities.ConnectionRequired;
import com.mobiroller.core.activities.inapppurchase.InAppPurchaseActivity;
import com.mobiroller.core.activities.inapppurchase.InAppPurchaseDetailActivity;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.helpers.InAppPurchaseHelper;
import com.mobiroller.core.models.InAppPurchaseProduct;
import com.mobiroller.core.models.NavigationItemModel;

/**
 * Created by ealtaca on 9/17/18.
 */

public class MobirollerIntent {

    public static final int APP_SETTINGS_REQUEST_CODE = 5;

    public static Intent getConnectionRequiredIntent(NavigationItemModel navigationItemModel) {
        Intent connectionRequiredIntent = new Intent(SharedApplication.context, ConnectionRequired.class);
        connectionRequiredIntent.putExtra(Constants.KEY_SCREEN_ID, navigationItemModel.getAccountScreenID());
        connectionRequiredIntent.putExtra(Constants.KEY_SCREEN_TYPE, navigationItemModel.getScreenType());
        connectionRequiredIntent.putExtra(Constants.KEY_UPDATE_DATE, navigationItemModel.getUpdateDate());
        return connectionRequiredIntent;
    }

    public static void startInAppPurchaseActivity(Context context, String screenId,String screenType, boolean isFromActivity) {
        if (InAppPurchaseHelper.getScreenProductList(screenId).size() == 1) {
            startInAppPurchaseDetailActivity(context, InAppPurchaseHelper.getScreenProductList(screenId).get(0), screenId,screenType, isFromActivity);
        } else {
            Intent intent = new Intent(context, InAppPurchaseActivity.class);
            intent.putExtra(Constants.KEY_SCREEN_ID, screenId);
            intent.putExtra(Constants.KEY_SCREEN_TYPE, screenType);
            if (isFromActivity)
                intent.putExtra(Constants.KEY_IS_FROM_ACTIVITY, true);
            context.startActivity(intent);
        }
    }

    public static void startInAppPurchaseDetailActivity(Context context, InAppPurchaseProduct inAppPurchaseProduct, String screenId,String screenType, boolean isFromActivity) {
        Intent intent = new Intent(context, InAppPurchaseDetailActivity.class);
        intent.putExtra(InAppPurchaseDetailActivity.INTENT_EXTRA_IN_APP_PURCHASE_PRODUCT, inAppPurchaseProduct);
        intent.putExtra(Constants.KEY_SCREEN_ID, screenId);
        intent.putExtra(Constants.KEY_SCREEN_TYPE, screenType);
        if (isFromActivity)
            intent.putExtra(Constants.KEY_IS_FROM_ACTIVITY, true);
        context.startActivity(intent);
    }
    public static void startConnectionRequiredActivity(Context context, NavigationItemModel navigationItemModel) {
        Intent connectionRequiredIntent = new Intent(SharedApplication.context, ConnectionRequired.class);
        connectionRequiredIntent.putExtra(Constants.KEY_SCREEN_ID, navigationItemModel.getAccountScreenID());
        connectionRequiredIntent.putExtra(Constants.KEY_SCREEN_TYPE, navigationItemModel.getScreenType());
        connectionRequiredIntent.putExtra(Constants.KEY_UPDATE_DATE, navigationItemModel.getUpdateDate());
        context.startActivity(connectionRequiredIntent);
    }

    public static void startAppSettings(Activity context) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        context.startActivityForResult(myAppSettings, APP_SETTINGS_REQUEST_CODE);
    }

}

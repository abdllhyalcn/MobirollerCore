package com.mobiroller.core.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.activities.preview.NotSupportedActivity;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.helpers.ApiRequestManager;
import com.mobiroller.core.helpers.FileDownloader;
import com.mobiroller.core.helpers.InAppPurchaseHelper;
import com.mobiroller.core.helpers.JSONParser;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.helpers.LegacyProgressViewHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.NetworkHelper;
import com.mobiroller.core.helpers.RequestHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.NavigationItemModel;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.coreui.models.TableItemsModel;
import com.mobiroller.core.R;
import com.mobiroller.core.util.InterstitialAdsUtil;
import com.mobiroller.core.util.MobirollerIntent;

import java.util.ArrayList;

import static com.mobiroller.core.activities.inapppurchase.InAppPurchaseActivity.setWindowFlag;

public class ActivityHandler extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    public final static String INTENT_EXTRA_NAVIGATION_MODEL = "NavigationModel";
    public final static String INTENT_EXTRA_TABLE_ITEM_MODEL = "TableItemModel";
    private JSONParser mJSONParser;
    private NetworkHelper mNetworkHelper;
    private ScreenModel mScreenModel;
    private ApiRequestManager apiRequestManager;
    private InterstitialAdsUtil mInterstitialAdsUtil;
    private LocalizationHelper mLocalizationHelper;
    private LegacyProgressViewHelper mLegacyProgressViewHelper;
    private String mScreenId;
    private String mScreenType;
    public String mSubScreenType;
    private String mUpdateDate;
    private String mRssPushTitle;
    private ArrayList<String> roles = new ArrayList<>();
    private BillingProcessor mBillingProcessor;

    public static void startActivity(Context context, NavigationItemModel navigationItemModel) {
        Intent intent = new Intent(context, ActivityHandler.class);
        intent.putExtra(INTENT_EXTRA_NAVIGATION_MODEL, navigationItemModel);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, TableItemsModel tableItemsModel) {
        Intent intent = new Intent(context, ActivityHandler.class);
        intent.putExtra(INTENT_EXTRA_TABLE_ITEM_MODEL, tableItemsModel);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String screenId, String screenType, String subScreenType, String updateDate, ArrayList<String> roles) {
        Intent intent = new Intent(context, ActivityHandler.class);
        intent.putExtra(Constants.KEY_SCREEN_ID, screenId);
        intent.putExtra(Constants.KEY_SCREEN_TYPE, screenType);
        intent.putExtra(Constants.KEY_SUB_SCREEN_TYPE, subScreenType);
        intent.putExtra(Constants.KEY_SCREEN_ROLES, roles);
        intent.putExtra(Constants.KEY_UPDATE_DATE, updateDate);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String screenId, String screenType, String subScreenType, String updateDate, ArrayList<String> roles, String rssPushTitle) {
        context.startActivity(getIntent(context, screenId, screenType, subScreenType, updateDate, roles, rssPushTitle));
    }

    public static Intent getIntent(Context context, String screenId, String screenType, String subScreenType, String updateDate, ArrayList<String> roles, String rssPushTitle) {
        Intent intent = new Intent(context, ActivityHandler.class);
        intent.putExtra(Constants.KEY_SCREEN_ID, screenId);
        intent.putExtra(Constants.KEY_SCREEN_TYPE, screenType);
        intent.putExtra(Constants.KEY_SUB_SCREEN_TYPE, subScreenType);
        intent.putExtra(Constants.KEY_SCREEN_ROLES, roles);
        intent.putExtra(Constants.KEY_UPDATE_DATE, updateDate);
        intent.putExtra(Constants.KEY_SCREEN_RSS_PUSH_TITLE, rssPushTitle);
        return intent;
    }

    public static Intent getIntent(Context context, String screenId, String screenType, String subScreenType, String rssPushTitle) {
        Intent intent = new Intent(context, ActivityHandler.class);
        intent.putExtra(Constants.KEY_SCREEN_ID, screenId);
        intent.putExtra(Constants.KEY_SCREEN_TYPE, screenType);
        intent.putExtra(Constants.KEY_SUB_SCREEN_TYPE, subScreenType);
        intent.putExtra(Constants.KEY_SCREEN_RSS_PUSH_TITLE, rssPushTitle);
        return intent;
    }

    public static void startActivity(Context context, String screenId, String screenType, String subScreenType, String rssPushTitle) {
        context.startActivity(getIntent(context, screenId, screenType, subScreenType, rssPushTitle));
    }

    public static void startActivity(Context context, String screenId, String screenType, String subScreenType, String updateDate, ArrayList<String> roles, boolean isFromConnectionRequired) {
        Intent intent = new Intent(context, ActivityHandler.class);
        intent.putExtra(Constants.KEY_SCREEN_ID, screenId);
        intent.putExtra(Constants.KEY_SCREEN_TYPE, screenType);
        intent.putExtra(Constants.KEY_SUB_SCREEN_TYPE, subScreenType);
        intent.putExtra(Constants.KEY_SCREEN_ROLES, roles);
        intent.putExtra(Constants.KEY_UPDATE_DATE, updateDate);
        intent.putExtra(Constants.KEY_FROM_NO_NETWORK, true);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new LegacyToolbarHelper().setStatusBar(this);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if (getIntent().hasExtra(INTENT_EXTRA_NAVIGATION_MODEL) || getIntent().hasExtra(INTENT_EXTRA_TABLE_ITEM_MODEL) || getIntent().hasExtra(Constants.KEY_SCREEN_ID)) {
            if (getIntent().hasExtra(INTENT_EXTRA_TABLE_ITEM_MODEL)) {
                TableItemsModel mTableItemModel = (TableItemsModel) getIntent().getSerializableExtra(INTENT_EXTRA_TABLE_ITEM_MODEL);
                mScreenType = mTableItemModel.getScreenType();
                mSubScreenType = mTableItemModel.screenSubType;
                mScreenId = mTableItemModel.getAccountScreenID();
                mUpdateDate = mTableItemModel.getUpdateDate();
                if (mTableItemModel.getRoles() != null)
                    roles = mTableItemModel.getRoles();
            } else if (getIntent().hasExtra(INTENT_EXTRA_NAVIGATION_MODEL)) {
                NavigationItemModel mNavigationItemModel = (NavigationItemModel) getIntent().getSerializableExtra(INTENT_EXTRA_NAVIGATION_MODEL);
                mScreenType = mNavigationItemModel.getScreenType();
                mSubScreenType = mNavigationItemModel.screenSubtype;
                mScreenId = mNavigationItemModel.getAccountScreenID();
                mUpdateDate = mNavigationItemModel.getUpdateDate();
                if (mNavigationItemModel.getRoles() != null)
                    roles = mNavigationItemModel.getRoles();
            } else {
                mScreenId = getIntent().getStringExtra(Constants.KEY_SCREEN_ID);
                mScreenType = getIntent().getStringExtra(Constants.KEY_SCREEN_TYPE);

                if (getIntent().hasExtra(Constants.KEY_SUB_SCREEN_TYPE))
                    mSubScreenType = getIntent().getStringExtra(Constants.KEY_SUB_SCREEN_TYPE);

                if (getIntent().hasExtra(Constants.KEY_UPDATE_DATE))
                    mUpdateDate = getIntent().getStringExtra(Constants.KEY_UPDATE_DATE);
                if (getIntent().hasExtra(Constants.KEY_SCREEN_ROLES))
                    roles = getIntent().getStringArrayListExtra(Constants.KEY_SCREEN_ROLES);
                if (getIntent().hasExtra(Constants.KEY_SCREEN_RSS_PUSH_TITLE))
                    mRssPushTitle = getIntent().getStringExtra(Constants.KEY_SCREEN_RSS_PUSH_TITLE);
            }
            SharedPrefHelper sharedPrefHelper = UtilManager.sharedPrefHelper();
            apiRequestManager = new ApiRequestManager(sharedPrefHelper, new RequestHelper(this, sharedPrefHelper));
            mNetworkHelper = UtilManager.networkHelper();
            mJSONParser = new JSONParser(new FileDownloader(), apiRequestManager, mNetworkHelper);
            mInterstitialAdsUtil = new InterstitialAdsUtil(this);
            mLegacyProgressViewHelper = new LegacyProgressViewHelper(this);
            mLocalizationHelper = UtilManager.localizationHelper();
            mLegacyProgressViewHelper.show();
            if (InAppPurchaseHelper.checkIsInAppPurchaseValid()) {
                mBillingProcessor = new BillingProcessor(this, InAppPurchaseHelper.getInAppPurchaseLicenseKey(), this);
//                mBillingProcessor.connect(this);
                mBillingProcessor.initialize();
                mBillingProcessor.loadOwnedPurchasesFromGoogleAsync(new BillingProcessor.IPurchasesResponseListener() {
                    @Override
                    public void onPurchasesSuccess() {

                    }

                    @Override
                    public void onPurchasesError() {

                    }
                });
            }
            new ScreenModelOperation().execute();
        } else {
            Toast.makeText(this, getString(R.string.common_error), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
//
//    @Override
//    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//
//    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    private class ScreenModelOperation extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startScreenModelActivity();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            getScreenModel();
            if (mScreenModel == null)
                if (!mNetworkHelper.isConnected())
                    mScreenModel = mJSONParser.getLocalScreenModel(ActivityHandler.this, String.valueOf(mScreenId));
                else
                    mScreenModel = apiRequestManager.getScreenJSON(String.valueOf(mScreenId));
            return null;
        }

        @Override
        protected void onPreExecute() {
        }
    }

    private void getScreenModel() {
        mScreenModel = mJSONParser.getScreenJSONFromLocalByID(this,
                String.valueOf(mScreenId),
                mNetworkHelper.isConnected(),
                mUpdateDate
        );
    }

    private void startScreenModelActivity() {
        if (!mNetworkHelper.isConnected() && NetworkHelper.connectionRequiredModule(mScreenType, this)) {
            if (!isFinishing())
                mLegacyProgressViewHelper.dismiss();
            startConnectionRequiredActivity();
        } else {
            if (mScreenModel != null) {
                if (InAppPurchaseHelper.checkScreenIsInPurchaseList(String.valueOf(mScreenId))) {
                    if (InAppPurchaseHelper.checkIsInAppPurchaseValid()) {
                        if (InAppPurchaseHelper.isScreenPurchased(mBillingProcessor,mScreenId))
                            startAction();
                        else {
                            MobirollerIntent.startInAppPurchaseActivity(this, String.valueOf(mScreenId), mScreenType, true);
                            if (!isFinishing())
                                mLegacyProgressViewHelper.dismiss();
                            finish();
                        }
                    } else {
                        if (!isFinishing())
                            mLegacyProgressViewHelper.dismiss();
                        startConnectionRequiredActivity();
                    }
                } else {
                    startAction();
                }
            } else {
                if (!isFinishing())
                    mLegacyProgressViewHelper.dismiss();
                startConnectionRequiredActivity();
            }
        }
    }

    private void startAction() {
        if (DynamicConstants.MobiRoller_Stage && !isSupportedForPreview()) {
            startActivity(getPreviewNotSupportedIntent().putExtra("title", mScreenModel.getTitle()).putExtra(Constants.KEY_SCREEN_TYPE, mScreenType));
            finish();
            return;
        }
        if (isCallAction()) {
            if (!isFinishing())
                mLegacyProgressViewHelper.dismiss();
            startCallActionActivity();
        } else if (isShareAction()) {
            if (!isFinishing())
                mLegacyProgressViewHelper.dismiss();
            startShareActionActivity();
        } else {
            Intent intent;
            if (isCustomActionRequired()) {
                intent = getCustomActionIntent();
            } else {
                intent = new Intent(this, GenericActivity.class);
            }
            setIntentProperties(intent);
            if (!isFinishing())
                mLegacyProgressViewHelper.dismiss();
            mInterstitialAdsUtil.checkInterstitialAds(intent);
            finish();
        }
    }

    private void startConnectionRequiredActivity() {
        Intent connectionRequiredIntent = new Intent(this, ConnectionRequired.class);
        connectionRequiredIntent.putExtra(Constants.KEY_SCREEN_ID, mScreenId);
        connectionRequiredIntent.putExtra(Constants.KEY_SCREEN_TYPE, mScreenType);
        connectionRequiredIntent.putExtra(Constants.KEY_UPDATE_DATE, mUpdateDate);
        connectionRequiredIntent.putExtra(Constants.KEY_SCREEN_ROLES, roles);
        if (mRssPushTitle != null)
            connectionRequiredIntent.putExtra(Constants.KEY_SCREEN_RSS_PUSH_TITLE, mRssPushTitle);
        startActivity(connectionRequiredIntent);
        finish();
    }

    private void setIntentProperties(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.KEY_SCREEN_ID, mScreenId);
        intent.putExtra(Constants.KEY_UPDATE_DATE, mUpdateDate);
        if (roles != null)
            intent.putExtra(Constants.KEY_SCREEN_ROLES, roles);
        if (mRssPushTitle != null)
            intent.putExtra(Constants.KEY_SCREEN_RSS_PUSH_TITLE, mRssPushTitle);
        if (isExtendedView()) {
            intent.putExtra(Constants.KEY_SCREEN_TYPE, getExtendedViewScreenType());
            intent.putExtra(Constants.KEY_SUB_SCREEN_TYPE, mSubScreenType);
        } else {
            intent.putExtra(Constants.KEY_SCREEN_TYPE, mScreenType);
            intent.putExtra(Constants.KEY_SUB_SCREEN_TYPE, mSubScreenType);
        }
        JSONStorage.putScreenModel(mScreenId, mScreenModel);
    }

    private void startCallActionActivity() {
        if (mScreenModel.getPhoneNumber() != null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + mScreenModel.getPhoneNumber()));
            startActivity(callIntent);
            finish();
        }
    }

    private void startShareActionActivity() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String contentText = mLocalizationHelper.getLocalizedTitle(mScreenModel.getContentText());
        String url = mScreenModel.getGooglePlayLink();
        if (url == null)
            url = "";
        if (mLocalizationHelper.getLocalizedTitle(mScreenModel.getContentText()) == null)
            contentText = "";
        sendIntent.putExtra(Intent.EXTRA_TEXT, contentText + "  " + url);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.action_share)));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isCallAction() {
        return mScreenType.equalsIgnoreCase("aveCallNowView");
    }

    private boolean isSupportedForPreview() {
        String[] notSupportedModuleList = getResources().getStringArray(R.array.preview_not_supported_modules);
        for (String moduleType : notSupportedModuleList) {
            if (mScreenType.equalsIgnoreCase(moduleType))
                return false;
        }
        return true;
    }

    private boolean isShareAction() {
        return mScreenType.equalsIgnoreCase("aveShareView");
    }

    private boolean isExtendedView() {
        return mScreenType.equalsIgnoreCase("aveHtmlView") &&
                (mScreenModel.getScreenType().equalsIgnoreCase("aveFAQView") ||
                        mScreenModel.getScreenType().equalsIgnoreCase("aveEmergencyCallView") ||
                        mScreenModel.getScreenType().equalsIgnoreCase("aveCatalogView") ||
                        mScreenModel.getScreenType().equalsIgnoreCase("aveAboutUsView")
                );
    }

    private String getExtendedViewScreenType() {
        return mScreenModel.getScreenType();
    }

    private boolean isCustomActionRequired() {
        if (mScreenType.equalsIgnoreCase("aveShareView") ||
                mScreenType.equalsIgnoreCase("aveChatView") ||
                mScreenType.equalsIgnoreCase("aveRSSView") ||
                mScreenType.equalsIgnoreCase("aveYoutubeAdvancedView"))
            return true;
        else if (mScreenType.equalsIgnoreCase("aveWebView")) {
            return mSubScreenType == null || !mSubScreenType.equalsIgnoreCase("avePDFView");
        }

        return false;
    }

    private Intent getCustomActionIntent() {
        Class<?> act = null;
        try {
            // todo check before moving modules
            if (mScreenType.equalsIgnoreCase("aveChatView"))
                act = Class.forName(Constants.Mobiroller_Preferences_PackageName_Chat_Activities + "." + mScreenType);
            else if (mScreenType.equalsIgnoreCase("aveYoutubeAdvancedView"))
                act = Class.forName(Constants.Mobiroller_Preferences_PackageName_Youtube_Activities + "." + mScreenType);
            else
                act = Class.forName(Constants.Mobiroller_Preferences_PackageName_Core_Activities + "." + mScreenType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Intent(this, act);
    }

    private Intent getPreviewNotSupportedIntent() {
        return new Intent(this, NotSupportedActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLegacyProgressViewHelper.dismiss();
    }
}

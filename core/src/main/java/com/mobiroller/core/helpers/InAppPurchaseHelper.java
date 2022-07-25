package com.mobiroller.core.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.PurchaseState;
import com.mobiroller.core.models.InAppPurchaseModel;
import com.mobiroller.core.models.InAppPurchaseProduct;
import com.mobiroller.core.R;

import java.util.ArrayList;
import java.util.List;

import static com.anjlab.android.iab.v3.PurchaseState.PurchasedSuccessfully;

public class InAppPurchaseHelper {

    private static void init(Context context) {

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
         * @return the {@link InAppPurchaseHelper.Builder} object.
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

            init(mContext);
        }
    }

    public static boolean checkScreenIsInPurchaseList(String screenId) {
        InAppPurchaseModel inAppPurchaseModel = JSONStorage.getInAppPurchaseModel();
        if (inAppPurchaseModel != null && inAppPurchaseModel.products != null) {
            List<InAppPurchaseProduct> inAppPurchaseProducts = inAppPurchaseModel.products;
            for (int i = 0; i < inAppPurchaseProducts.size(); i++) {
                if (inAppPurchaseProducts.get(i).screenList != null) {
                    for (int j = 0; j < inAppPurchaseProducts.get(i).screenList.size(); j++) {
                        if (inAppPurchaseProducts.get(i).screenList.get(j).equalsIgnoreCase(screenId))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<InAppPurchaseProduct> getScreenProductList(String screenId) {
        List<InAppPurchaseProduct> productList = new ArrayList<>();
        List<InAppPurchaseProduct> inAppPurchaseProducts = JSONStorage.getInAppPurchaseModel().products;
        if (inAppPurchaseProducts != null) {
            for (int i = 0; i < inAppPurchaseProducts.size(); i++) {
                if (inAppPurchaseProducts.get(i).screenList != null) {
                    for (int j = 0; j < inAppPurchaseProducts.get(i).screenList.size(); j++) {
                        if (inAppPurchaseProducts.get(i).screenList.get(j).equalsIgnoreCase(screenId)) {
                            productList.add(inAppPurchaseProducts.get(i));
                        }
                    }
                }
            }
        }

        return productList;
    }

    public static String getInAppPurchaseLicenseKey() {
        return JSONStorage.getInAppPurchaseModel().androidLicenseKey;
    }

    public static boolean checkIabAvailable(Context context) {
        if (BillingProcessor.isIabServiceAvailable(context))
            return true;
        else
            Toast.makeText(context, context.getString(R.string.in_app_purchase_not_available), Toast.LENGTH_SHORT).show();
        return false;
    }

    @SuppressLint("StringFormatInvalid")
    public static String getPeriodString(Context context, String period) {
        if (period.equalsIgnoreCase("P1W"))
            return context.getString(R.string.in_app_purchase_period_week, "1");
        else if (period.equalsIgnoreCase("P1M"))
            return context.getString(R.string.in_app_purchase_period_month, "1");
        else if (period.equalsIgnoreCase("P3M"))
            return context.getString(R.string.in_app_purchase_period_month, "3");
        else if (period.equalsIgnoreCase("P6M"))
            return context.getString(R.string.in_app_purchase_period_month, "6");
        else if (period.equalsIgnoreCase("P1Y"))
            return context.getString(R.string.in_app_purchase_period_yearly);
        return "Unknown";
    }

    public static boolean checkIsInAppPurchaseValid() {
        return (UtilManager.sharedPrefHelper().getIsInAppPurchaseActive()) && JSONStorage.getInAppPurchaseModel() != null && JSONStorage.getInAppPurchaseModel().androidLicenseKey != null;
    }

    public static boolean isScreenPurchased(BillingProcessor mBillingProcessor, String mScreenId) {
        mBillingProcessor.loadOwnedPurchasesFromGoogleAsync(new BillingProcessor.IPurchasesResponseListener() {
            @Override
            public void onPurchasesSuccess() {

            }

            @Override
            public void onPurchasesError() {

            }
        });
        List<InAppPurchaseProduct> productList = InAppPurchaseHelper.getScreenProductList(mScreenId);
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).oneTimeProductId != null && mBillingProcessor.isPurchased(productList.get(i).oneTimeProductId)) {
                PurchaseInfo details = mBillingProcessor.getPurchaseInfo(productList.get(i).oneTimeProductId);
//                PurchaseInfo details = mBillingProcessor.getPurchaseTransactionDetails(productList.get(i).oneTimeProductId);
                if (details != null && details.purchaseData.purchaseState == PurchaseState.PurchasedSuccessfully) {
                    return true;
                } else
                    return false;
            } else if (productList.get(i).subscriptionProductId != null && productList.get(i).subscriptionProductId.length != 0 && mBillingProcessor.isSubscribed(productList.get(i).subscriptionProductId[0]))
                return true;
        }
        return false;
    }
}

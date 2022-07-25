package com.mobiroller.core.helpers;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.mobiroller.core.SharedApplication;

/**
 * Created by ealtaca on 26.05.2017.
 */

public class BannerHelper {

    private SharedPrefHelper sharedPrefHelper;
    private NetworkHelper networkHelper;
    private Context context;

    /**
     * Helps for banner placement
     *
     * @param sharedPrefHelper SharedPrefHelper
     * @param context          Context
     * @param networkHelper    NetworkHelper
     */
    public BannerHelper(SharedPrefHelper sharedPrefHelper, Context context, NetworkHelper networkHelper) {
        this.sharedPrefHelper = sharedPrefHelper;
        this.networkHelper = networkHelper;
        this.context = context;
        this.sharedPrefHelper.setBannerAd(context);
    }

    /**
     * Helps for banner placement
     */
    public BannerHelper() {
        this.sharedPrefHelper = UtilManager.sharedPrefHelper();
        this.networkHelper = UtilManager.networkHelper();
        this.context = SharedApplication.context;
        this.sharedPrefHelper.setBannerAd(context);
    }

    // Add banner ad to bottom of given layout
    public void addBannerAd(RelativeLayout layout, final View view) {
        if (networkHelper.isConnected() && sharedPrefHelper.isBannerActive()) {
            addSpaceForBanner(view);
            if (sharedPrefHelper.getIsAdmobBannerAdEnabled()) {
                addAdmobBanner(layout, view);
            } else {
                addStartAppBanner(layout);
            }
        }
    }

    public int getBannerHeight() {
        if (networkHelper.isConnected() && sharedPrefHelper.isBannerActive() && sharedPrefHelper.getBannerAd() != null) {
            sharedPrefHelper.getBannerAd().getAdSize().getHeight();
        }
        return 0;
    }

    // Add banner ad to top of given layout
    public void addBannerTop(RelativeLayout layout) {
        if (networkHelper.isConnected() && sharedPrefHelper.isBannerActive()) {
            if (sharedPrefHelper.getIsAdmobBannerAdEnabled()) {
                addAdmobBannerTop(layout);
            } else {
                addStartAppBannerTop(layout);
            }
        }
    }

    private void addAdmobBannerTop(RelativeLayout layout) {

        if (sharedPrefHelper.getBannerAd() == null)
            sharedPrefHelper.setBannerAd(context, null, layout);
        else {
            if (!sharedPrefHelper.getBannerAdUnitID().isEmpty()
                    && sharedPrefHelper.getBannerAd() != null) {
                RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

                lay.addRule(RelativeLayout.ALIGN_PARENT_TOP);

                if (sharedPrefHelper.getBannerAd().getParent() != null) {
                    ((ViewGroup) sharedPrefHelper.getBannerAd().getParent()).removeView(sharedPrefHelper.getBannerAd());
                }

                // Add the AdView to the view hierarchy.
                layout.addView(sharedPrefHelper.getBannerAd(), lay);
            }
        }
    }

    private void addStartAppBannerTop(RelativeLayout mainLayout) {
    }

    private void addStartAppBanner(RelativeLayout mainLayout) {
    }

    private void addAdmobBanner(RelativeLayout layout, View view) {
        if (sharedPrefHelper.getBannerAd() == null)
            sharedPrefHelper.setBannerAd(context, view, layout);
        else {
            if (!sharedPrefHelper.getBannerAdUnitID().isEmpty()
                    && sharedPrefHelper.getBannerAd() != null) {

                RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

                lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                if (sharedPrefHelper.getBannerAd().getParent() != null) {
                    ((ViewGroup) sharedPrefHelper.getBannerAd().getParent()).removeView(sharedPrefHelper.getBannerAd());
                }

                // Add the AdView to the view hierarchy.
                layout.addView(sharedPrefHelper.getBannerAd(), lay);
            }
        }
    }

    private void addSpaceForBanner(View view) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float px;

        if (dpHeight <= 400) {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, context.getResources().getDisplayMetrics());
        } else if (dpHeight <= 720) {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
        } else {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, context.getResources().getDisplayMetrics());
        }
        if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            linearParams.setMargins(0, 0, 0, (int) px);  // left, top, right, bottom
            view.setLayoutParams(linearParams);
        } else if (view instanceof RecyclerView) {
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            marginLayoutParams.setMargins(0, 0, 0, (int) px);
            view.setLayoutParams(marginLayoutParams);
        } else if(view.getLayoutParams() instanceof  RelativeLayout.LayoutParams){
            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            relativeParams.setMargins(0, 0, 0, (int) px);  // left, top, right, bottom
            view.setLayoutParams(relativeParams);
        } else if(view.getLayoutParams() instanceof  FrameLayout.LayoutParams){
            FrameLayout.LayoutParams relativeParams = (FrameLayout.LayoutParams) view.getLayoutParams();
            relativeParams.setMargins(0, 0, 0, (int) px);  // left, top, right, bottom
            view.setLayoutParams(relativeParams);
        }
    }

}

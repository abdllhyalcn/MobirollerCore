package com.mobiroller.core.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.activities.MobiRollerBadgeActivity;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.R;
import com.mobiroller.core.coreui.util.ScreenUtil;
import com.nightonke.boommenu.Animation.AnimationManager;
import com.nightonke.boommenu.Animation.Ease;
import com.nightonke.boommenu.Animation.EaseEnum;
import com.nightonke.boommenu.Util;

/**
 * MobiRoller Badge View
 * <p>
 * Created by ealtaca
 * 23.04.2019
 * Since v3.8.1.0
 */
public class MobiRollerBadgeView {

    private static float startPositionXBadge;
    private static float startPositionYBadge;
    private static boolean ableToStartDraggingBadge = false;
    private static boolean isDraggingBadge = false;
    private static float lastMotionXBadge = -1;
    private static float lastMotionYBadge = -1;

    private static Rect edgeInsetsInParentViewBadge;
    private final static String MOBIROLLER_BADGE_BUTTON_TAG = "mobiRollerBadgeView";

    /**
     * Add movable MobiRoller badge view to activity
     *
     * @param activity AveActivity
     */
    @SuppressLint("ClickableViewAccessibility")
    public static void addView(Activity activity) {
        if (DynamicConstants.MobiRoller_Stage)
            return;

        Bundle bundle = new Bundle();
        bundle.putString("app_name", activity.getString(R.string.app_name));
        bundle.putString("package", activity.getPackageName());
        FirebaseAnalytics.getInstance(activity).logEvent("badge_view", bundle);
        FrameLayout mRootLayout = (FrameLayout) activity.getWindow().getDecorView().getRootView();

        View viewLayout = View.inflate(activity, R.layout.mobiroller_badge_layout, null);
        FloatingActionButton mobiRollerBadgeView = viewLayout.findViewById(R.id.fab_button_badge);
        if (JSONStorage.getMobirollerBadgeModel() != null &&
                JSONStorage.getMobirollerBadgeModel().design != null &&
                JSONStorage.getMobirollerBadgeModel().design.iconUrl != null) {
            Glide.with(viewLayout).load(JSONStorage.getMobirollerBadgeModel().design.iconUrl).into(mobiRollerBadgeView);
        }
        mobiRollerBadgeView.setRippleColor(Color.TRANSPARENT);
        mobiRollerBadgeView.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        mobiRollerBadgeView.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.mobiroller_badge));
        mobiRollerBadgeView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.mobiroller_badge));
        mobiRollerBadgeView.setColorFilter(null);
        mobiRollerBadgeView.setSupportImageTintList(null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mobiRollerBadgeView.setLayoutParams(params);
        mobiRollerBadgeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        startPositionXBadge = mobiRollerBadgeView.getX() - event.getRawX();
                        startPositionYBadge = mobiRollerBadgeView.getY() - event.getRawY();
                        lastMotionXBadge = event.getRawX();
                        lastMotionYBadge = event.getRawY();
                        UtilManager.sharedPrefHelper().setMobiRollerBadgeY(mobiRollerBadgeView.getY(), activity);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(lastMotionXBadge - event.getRawX()) > 10
                                || Math.abs(lastMotionYBadge - event.getRawY()) > 10)
                            ableToStartDraggingBadge = true;
                        if (ableToStartDraggingBadge) {
                            isDraggingBadge = true;
                            mobiRollerBadgeView.setX(event.getRawX() + startPositionXBadge);
                            mobiRollerBadgeView.setY(event.getRawY() + startPositionYBadge);
                        } else {
                            ableToStartDraggingBadge = false;
                        }
                        UtilManager.sharedPrefHelper().setMobiRollerBadgeY(mobiRollerBadgeView.getY(), activity);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isDraggingBadge) {
                            ableToStartDraggingBadge = false;
                            isDraggingBadge = false;
                            mobiRollerBadgeView.requestLayout();
                            UtilManager.sharedPrefHelper().setMobiRollerBadgeY(mobiRollerBadgeView.getY(), activity);
                            preventDragOutside(mobiRollerBadgeView, activity);
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (isDraggingBadge) {
                            ableToStartDraggingBadge = false;
                            isDraggingBadge = false;
                            mobiRollerBadgeView.requestLayout();
                            UtilManager.sharedPrefHelper().setMobiRollerBadgeY(mobiRollerBadgeView.getY(), activity);
                            preventDragOutside(mobiRollerBadgeView, activity);
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

        mobiRollerBadgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("app_name", activity.getString(R.string.app_name));
                bundle.putString("package", activity.getPackageName());
                FirebaseAnalytics.getInstance(activity).logEvent("badge_click", bundle);
                activity.startActivity(new Intent(activity, MobiRollerBadgeActivity.class));
            }
        });
        View view = mRootLayout.findViewById(R.id.fab_button_badge);
        if (view != null) {
            mRootLayout.removeView(view);
        }
        mRootLayout.addView(mobiRollerBadgeView);
        mobiRollerBadgeView.setY(UtilManager.sharedPrefHelper().getMobiRollerBadgeY(activity));
        preventDragOutside(mobiRollerBadgeView, activity);

    }

    private static void preventDragOutside(View menuButton, Activity activity) {

        if (edgeInsetsInParentViewBadge == null) {
            TypedArray typedArray = activity.obtainStyledAttributes(
                    null, com.nightonke.boommenu.R.styleable.BoomMenuButton, 0, 0);
            edgeInsetsInParentViewBadge = new Rect(0, 0, 0, 0);
            edgeInsetsInParentViewBadge.left = Util.getDimenOffset(typedArray, com.nightonke.boommenu.R.styleable.BoomMenuButton_bmb_edgeInsetsLeft, com.nightonke.boommenu.R.dimen.default_bmb_edgeInsetsLeft);
            edgeInsetsInParentViewBadge.top = Util.getDimenOffset(typedArray, com.nightonke.boommenu.R.styleable.BoomMenuButton_bmb_edgeInsetsTop, com.nightonke.boommenu.R.dimen.default_bmb_edgeInsetsTop);
            edgeInsetsInParentViewBadge.right = Util.getDimenOffset(typedArray, com.nightonke.boommenu.R.styleable.BoomMenuButton_bmb_edgeInsetsRight, com.nightonke.boommenu.R.dimen.default_bmb_edgeInsetsRight);
            edgeInsetsInParentViewBadge.bottom = Util.getDimenOffset(typedArray, com.nightonke.boommenu.R.styleable.BoomMenuButton_bmb_edgeInsetsBottom, com.nightonke.boommenu.R.dimen.default_bmb_edgeInsetsBottom);
        }

        boolean needToAdjustXY = false;
        boolean saveY = false;
        float newX = menuButton.getX();
        float newY = menuButton.getY();
        ViewGroup parentView = (ViewGroup) menuButton.getParent();
        int parentWidth = ScreenUtil.getScreenWidth();
        int parentHeight = ScreenUtil.getScreenHeight();
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        if (newX == 0.0) {
            newX = width - activity.getResources().getDimension(R.dimen.default_bmb_text_inside_circle_width) / 2;
            needToAdjustXY = true;
        }
        if (newY == 0.0) {
            newY = height / 2;
            saveY = true;
            needToAdjustXY = true;
        }
        if (newX < (width - activity.getResources().getDimension(R.dimen.default_bmb_text_inside_circle_width) / 2) || newX > (width - activity.getResources().getDimension(R.dimen.default_bmb_text_inside_circle_width) / 2)) {
            newX = width - activity.getResources().getDimension(R.dimen.default_bmb_text_inside_circle_width) / 2;
            needToAdjustXY = true;
        }

        if (newY < edgeInsetsInParentViewBadge.top) {
            newY = edgeInsetsInParentViewBadge.top;
            saveY = true;
            needToAdjustXY = true;
        }

        if (!(parentWidth - edgeInsetsInParentViewBadge.right - activity.getResources().getDimension(R.dimen.default_bmb_text_inside_circle_width) < 0) && newX > parentWidth - edgeInsetsInParentViewBadge.right - activity.getResources().getDimension(R.dimen.default_bmb_text_inside_circle_width)) {
            newX = (width - activity.getResources().getDimension(R.dimen.default_bmb_text_inside_circle_width) / 2);
            needToAdjustXY = true;
        }

        if (!(parentHeight - edgeInsetsInParentViewBadge.bottom - activity.getResources().getDimension(R.dimen.default_bmb_text_inside_circle_height) < 0) && newY > parentHeight - edgeInsetsInParentViewBadge.bottom - activity.getResources().getDimension(R.dimen.default_bmb_text_inside_circle_height)) {
            newY = parentHeight - edgeInsetsInParentViewBadge.bottom - activity.getResources().getDimension(R.dimen.default_bmb_text_inside_circle_height);
            saveY = true;
            needToAdjustXY = true;
        }

        if (needToAdjustXY) {
            if (saveY)
                UtilManager.sharedPrefHelper().setMobiRollerBadgeY(newY, activity);
            AnimationManager.animate(menuButton, "x", 0, 300, Ease.getInstance(EaseEnum.EaseOutBack), menuButton.getX(), newX);
            AnimationManager.animate(menuButton, "y", 0, 300, Ease.getInstance(EaseEnum.EaseOutBack), menuButton.getY(), newY);
        }
        parentView.requestLayout();
    }

    public static void removeView(Activity activity) {
        FrameLayout mRootLayout = (FrameLayout) activity.getWindow().getDecorView().getRootView();
        View view = mRootLayout.findViewById(R.id.fab_button_badge);
        if (view != null) {
            mRootLayout.removeView(view);
        }
    }

}

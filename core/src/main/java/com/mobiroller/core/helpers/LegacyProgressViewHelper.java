package com.mobiroller.core.helpers;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.mobiroller.core.coreui.helpers.ProgressViewHelper;

/**
 * Created by ealtaca on 27.12.2016.
 */

public class LegacyProgressViewHelper {

    private SharedPrefHelper sharedPrefHelper;

    private ProgressViewHelper progressViewHelper;


    public LegacyProgressViewHelper(AppCompatActivity context) {
        init(context);
    }

    public LegacyProgressViewHelper(FragmentActivity context) {
        init(context);
    }

    public LegacyProgressViewHelper(Activity context) {
        init(context);
    }

    private void init(Activity activity) {
        sharedPrefHelper = UtilManager.sharedPrefHelper();

        progressViewHelper = new ProgressViewHelper(activity,
                sharedPrefHelper.getProgressAnimationType(),
                sharedPrefHelper.getProgressAnimationColor());
    }

    public void show() {
        progressViewHelper.show();
    }

    public void dismiss() {
        progressViewHelper.dismiss();
    }

    public void cancel() {
        progressViewHelper.cancel();
    }

    public ProgressBar getProgressDialog() {
        return progressViewHelper.getProgressDialog();
    }

    public void setNotCancelable() {
        progressViewHelper.notCancelable();
    }

    public boolean isShowing() {
        return progressViewHelper.isShowing();
    }

    public Drawable getProgressDrawable() {
        return progressViewHelper.getProgressDrawable();
    }

}

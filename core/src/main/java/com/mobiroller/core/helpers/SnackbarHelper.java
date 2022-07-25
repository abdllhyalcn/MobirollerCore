package com.mobiroller.core.helpers;

import android.app.Activity;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.mobiroller.core.R;
import com.mobiroller.core.coreui.util.ScreenUtil;

/**
 * Created by ealtaca on 9/17/18.
 */

public class SnackbarHelper {


    public static void displaySnackBarMsg(final Activity activity, View view, String msg, boolean success) {
        ScreenUtil.closeKeyboard(activity);
        Snackbar snackbar = Snackbar
                .make(view, "", Snackbar.LENGTH_LONG);
        if (success)
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.snackbar_success));
        else
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.snackbar_fail));
        snackbar.setText(msg);
        snackbar.show();
    }


    public static void displaySnackBarMsgFinishActivity(final Activity activity, View view, String msg, boolean success) {
        ScreenUtil.closeKeyboard(activity);
        Snackbar snackbar = Snackbar
                .make(view, "", Snackbar.LENGTH_LONG);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                activity.finish();
            }
        });
        if (success)
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.snackbar_success));
        else
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.snackbar_fail));
        snackbar.setText(msg);
        snackbar.show();
    }
}

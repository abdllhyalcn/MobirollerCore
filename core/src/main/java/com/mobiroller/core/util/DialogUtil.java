package com.mobiroller.core.util;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mobiroller.core.R;

public class DialogUtil {

    /**
     * Show no connection information
     * and destroy activity
     *
     * @param activity
     */
    public static void showNoConnectionError(Activity activity) {
        new MaterialDialog.Builder(activity)
                .title(R.string.connection_required_error)
                .content(R.string.please_check_your_internet_connection)
                .cancelable(false)
                .positiveText(R.string.OK)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        activity.finish();
                    }
                })
                .show();
    }

    /**
     * Just show no connection information
     *
     * @param context
     */
    public static void showNoConnectionInfo(Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.connection_required_error)
                .content(R.string.please_check_your_internet_connection)
                .positiveText(R.string.OK)
                .show();
    }


    public static void showNoConnectionInfo(Context context, MaterialDialog.SingleButtonCallback callback) {
        new MaterialDialog.Builder(context)
                .title(R.string.connection_required_error)
                .content(R.string.please_check_your_internet_connection)
                .positiveText(R.string.OK)
                .onPositive(callback)
                .show();
    }



    public static void showDialog(Context context,String message)
    {
        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText(R.string.OK)
                .show();
    }

    public static void showDialogWithCallBack(Context context,String message,DialogCallBack callBack)
    {
        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText(R.string.OK)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        callBack.onClickPositive();
                    }
                })
                .show();
    }


    public static MaterialDialog.Builder getDialogWithCallBack(Context context,String message,DialogCallBack callBack)
    {
        return new MaterialDialog.Builder(context)
                .content(message)
                .positiveText(R.string.OK)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        callBack.onClickPositive();
                    }
                });
    }



    public static void showLogoutDialog(AppCompatActivity activity,UserLogoutDialog userLogoutDialog) {
        new MaterialDialog.Builder(activity)
                .title(R.string.app_name)
                .content(R.string.logout_dialog)
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        userLogoutDialog.onUserLogout();
                    }
                })
                .show();
    }


    public static MaterialDialog showExitDialog(AppCompatActivity activity, ExitDialog exitDialog) {
        return new MaterialDialog.Builder(activity)
                .title(R.string.app_name)
                .content(R.string.action_close_app)
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        exitDialog.onExitApp();
                    }
                })
                .show();
    }



    public interface UserLogoutDialog {
        void onUserLogout();
    }

    public interface ExitDialog {
        void onExitApp();
    }

    public interface DialogCallBack {
        void onClickPositive();
    }

}

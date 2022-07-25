package com.mobiroller.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.URLUtil;

import androidx.browser.customtabs.CustomTabsIntent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mobiroller.core.activities.aveWebView;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.models.PreviewNotificationModel;
import com.mobiroller.core.models.WebContent;
import com.mobiroller.core.R;

public class PreviewUtil {

    public static void showNotSupportedDialog(Context context) {
        new MaterialDialog.Builder(context)
                .content(R.string.not_supported_on_preview)
                .cancelable(false)
                .positiveText(R.string.OK)
                .show();
    }

    public static void openWebFromNotification(Activity activity) {
        if (activity.getIntent().hasExtra(Constants.MobiRoller_Preview_Notification_Model)) {
            PreviewNotificationModel previewNotificationModel = (PreviewNotificationModel) activity.getIntent().getSerializableExtra(Constants.MobiRoller_Preview_Notification_Model);
            if (URLUtil.isValidUrl(previewNotificationModel.webUrl)) {
                if (previewNotificationModel.external) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(Theme.primaryColor);
                    builder.setShowTitle(true);
                    builder.addDefaultShareMenuItem();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(activity, Uri.parse(previewNotificationModel.webUrl));
                } else {
                    WebContent webContent = new WebContent(UtilManager.localizationHelper().getLocalizedTitle(previewNotificationModel.title)
                            ,UtilManager.localizationHelper().getLocalizedTitle(previewNotificationModel.title),true);
                    Intent i = new Intent(activity, aveWebView.class);
                    i.putExtra("webContent", webContent);
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }
            }
        }
    }
}

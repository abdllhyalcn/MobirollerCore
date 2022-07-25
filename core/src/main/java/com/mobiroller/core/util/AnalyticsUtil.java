package com.mobiroller.core.util;

import android.content.Context;

import com.applyze.ApplyzeAnalytics;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.R;

public class AnalyticsUtil {

    public static void sendStats(Context context,ScreenModel screenModel, String screenType) {
        ApplyzeAnalytics.getInstance().sendScreenEvent(UtilManager.localizationHelper().getLocalizedTitle(screenModel.getTitle()));
        //TODO Analytics
//        String result = getScreenTypeAnalyticsName(context,screenModel,screenType);
//        setScreenName(result + " - " + UtilManager.localizationHelper().getLocalizedTitle(screenModel.getTitle()))
    }

    private static String getScreenType(ScreenModel screenModel, String screenType) {
        if (screenModel.getScreenType() != null)
            return screenModel.getScreenType();
        else
            return screenType;
    }

    private static String getScreenTypeAnalyticsName(Context context, ScreenModel screenModel, String screenType) {
        String result = getScreenType(screenModel,screenType);
        String[] moduleAnalyticsName = context.getResources().getStringArray(R.array.module_analytics_name);
        String[] moduleAnalyticsValue = context.getResources().getStringArray(R.array.module_analytics_value);
        for (int i = 0; i < moduleAnalyticsName.length; i++) {
            if (result.equalsIgnoreCase(moduleAnalyticsName[i])) {
                result = moduleAnalyticsValue[i];
                break;
            }
        }
        return result;
    }
}

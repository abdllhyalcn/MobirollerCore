package com.mobiroller.core.coreui.helpers;


import android.content.Context;

import java.util.List;
import java.util.Locale;

/**
 * Created by ealtaca on 26.05.2017.
 */

public class LocalizationHelper {

    public static Context context;

    public LocalizationHelper() {
    }

    public static String getLocalizedTitle(String text) {
        if(text == null)
            return "";
        if(!text.contains("<"))
            return text;
        if(!text.startsWith("{<") && !text.endsWith(">}"))
            return text;

        String[] parts = text.split("<" +  LocaleHelper.getLocale(context).toUpperCase() + ">");

        if (parts.length <= 1) {
            return "";
        }

        int position = parts.length - 2;

        if (position > 0)
            return parts[position];
        else
            return text;
    }

    public String getLocalizedTitlePreview(String text) {
        String deviceLang = "EN";
        if(Locale.getDefault().getLanguage().toUpperCase().equalsIgnoreCase("TR"))
            deviceLang = "TR";
        if(text == null)
            return "";
        if(!text.contains("<"))
            return  text;

        List<String> localeCodes = LocaleHelper.getLocaleList(context);

        String[] parts = text.split("<" +deviceLang.toUpperCase() + ">");
        if (parts.length <= 1) {
            if (localeCodes.contains(deviceLang.toLowerCase()))
                return "";
        }
        int position = parts.length - 2;
        if (position > 0)
            return parts[position];
        else
            return text;
    }

}

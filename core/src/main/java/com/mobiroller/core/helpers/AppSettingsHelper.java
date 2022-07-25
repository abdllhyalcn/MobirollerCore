package com.mobiroller.core.helpers;

import androidx.annotation.Nullable;

import com.mobiroller.core.helpers.godmode.SquidexAppData;

import io.paperdb.Paper;

public class AppSettingsHelper {

    private static final String isECommerceActiveKey = "isECommerceActiveKey";
    private static final String isTourVisioActiveKey = "isTourVisioActiveKey";
    public static String APP_SETTINGS_BOOK = "appSettingsBook";
    private static final String GodModeSelectedApp = "GodModeSelectedApp";


    public static boolean isECommerceActive() {
        return Paper.book(APP_SETTINGS_BOOK).read(isECommerceActiveKey,false);
    }

    public static void setIsECommerceActive(boolean isActive) {
        Paper.book(APP_SETTINGS_BOOK).write(isECommerceActiveKey, isActive);
    }

    public static boolean isTourVisioActive() {
        if(Paper.book(APP_SETTINGS_BOOK).contains(isTourVisioActiveKey))
            return Paper.book(APP_SETTINGS_BOOK).read(isTourVisioActiveKey,false);
        return false;
    }

    public static void setIsTourVisioActive(boolean isActive) {
        Paper.book(APP_SETTINGS_BOOK).write(isTourVisioActiveKey, isActive);
    }

    @Nullable
    public static SquidexAppData getGodModeSelectedApp() {
        try {
            if(Paper.book(APP_SETTINGS_BOOK).contains(GodModeSelectedApp))
                return Paper.book(APP_SETTINGS_BOOK).read(GodModeSelectedApp,null);
            return null;
        } catch (Exception e) { return null; }
    }

    public static void setGodModeSelectedApp(SquidexAppData data) {
        Paper.book(APP_SETTINGS_BOOK).write(GodModeSelectedApp, data);
    }

}

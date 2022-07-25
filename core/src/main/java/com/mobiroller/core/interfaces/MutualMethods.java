package com.mobiroller.core.interfaces;

import android.content.Context;

/**
 * Define mutual methods that required by MobiRoller structure
 *
 * Created by OKN on 05.05.2017.
 */

public interface MutualMethods {



    /**
     * Use this method to get localized text for user language,
     * if user device language is not supported or no resource
     * entered for related content for device lang, than return
     * default language specified text
     *
     * @param context
     * @param text
     * @return
     */
    String getLocalizedTitle( Context context, String text );   // TODO remove unncessary context object & change this method name to "getLocalizedText" **

    /**
     * Get height with ratio of mobiroller standard height (Constants.MobiRoller_Preferences_StandardHeight)
     *
     * @param height
     * @return
     */
    int getHeightForDevice( int height );

    /**
     * Get width with ratio of mobiroller standard width (Constants.MobiRoller_Preferences_StandardWidth)
     *
     * @param width
     * @return
     */
    int getWidthForDevice( int width );



    /**
     * Send user screen related data to MobiRoller by post request,
     * User datas that will sent to mbr;
     *  - mbr account id (accountName)
     *  - user device id (udid)
     *  - related screen id
     *  - user device lang
     *  - registration id (token for push notification)
     *  - localized title of related screen
     *  - mbr object type of related screen (ex. aveWebView)
     *  - OS type (static - "1" for android)
     *  -
     *
     * @param context
     * @param screenTitle - Localized title of related screen
     * @param screenType - MobiRoller object type of related screen (ex. aveWebView)
     */
    void ScreenDisplayStats( Context context, String screenTitle, String screenType );



}

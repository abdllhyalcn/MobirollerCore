package com.mobiroller.core;

import android.content.Context;

import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.ApiRequestManager;
import com.mobiroller.core.helpers.BannerHelper;
import com.mobiroller.core.helpers.ComponentHelper;
import com.mobiroller.core.helpers.FileDownloader;
import com.mobiroller.core.helpers.JSONParser;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.helpers.NetworkHelper;
import com.mobiroller.core.helpers.ScreenHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.scopes.ApplicationLevel;
import com.mobiroller.core.util.ImageManager;

import dagger.Component;

@ApplicationLevel
@Component(modules = {AppModule.class})
public interface AppComponent {
    SharedPrefHelper getSharedPref();

    SharedApplication getApplication();

    Context getContext();

    NetworkHelper getNetworkHelper();

    ScreenHelper getScreenHelper();

    FileDownloader getFileDownloader();

    JSONParser getJsonParser();

    ImageManager getImageManager();

    ApiRequestManager getApiRequestManager();

    LegacyToolbarHelper getToolbarHelper();

    LocalizationHelper getLocalizationHelper();

    BannerHelper getBannerHelper();

    ComponentHelper getComponentHelper();

    void inject(SharedApplication app);
}

package com.mobiroller.core;

import android.app.Application;
import android.content.Context;

import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.ApiRequestManager;
import com.mobiroller.core.helpers.BannerHelper;
import com.mobiroller.core.helpers.ComponentHelper;
import com.mobiroller.core.helpers.FileDownloader;
import com.mobiroller.core.helpers.JSONParser;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.helpers.NetworkHelper;
import com.mobiroller.core.helpers.RequestHelper;
import com.mobiroller.core.helpers.ScreenHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.scopes.ApplicationLevel;
import com.mobiroller.core.util.ImageManager;

import dagger.Module;
import dagger.Provides;

/**
 * App Module provides helpers to ActivityModule and FragmentModule
 * <p>
 * Created by ealtaca on 23.05.2017.
 */

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationLevel
    Context providesApplicationContext() {
        return mApplication;
    }


    @Provides
    @ApplicationLevel
    SharedApplication providesApplication() {
        return (SharedApplication) mApplication;
    }

    @Provides
    @ApplicationLevel
    SharedPrefHelper providesSharedPrefHelper(Context mApplication) {
        return new SharedPrefHelper(mApplication);
    }

    @Provides
    @ApplicationLevel
    NetworkHelper providesNetworkHelper(Context mApplication) {
        return new NetworkHelper(mApplication);
    }

    @Provides
    @ApplicationLevel
    ScreenHelper providesScreenHelper(Context mApplication) {
        return new ScreenHelper(mApplication);
    }

    @Provides
    @ApplicationLevel
    FileDownloader providesFileDownloader() {
        return new FileDownloader();
    }

    @Provides
    @ApplicationLevel
    JSONParser providesJSONParser(FileDownloader fileDownloader, ApiRequestManager apiRequestManager, NetworkHelper networkHelper) {
        return new JSONParser(fileDownloader, apiRequestManager, networkHelper);
    }

    @Provides
    @ApplicationLevel
    ImageManager providesImageManager() {
        return new ImageManager();
    }

    @Provides
    @ApplicationLevel
    ApiRequestManager providesApiRequestManager(SharedPrefHelper sharedPrefHelper,RequestHelper requestHelper) {
        return new ApiRequestManager(sharedPrefHelper,requestHelper);
    }

    @Provides
    @ApplicationLevel
    LegacyToolbarHelper providesToolbarHelper() {
        return new LegacyToolbarHelper();
    }

    @Provides
    @ApplicationLevel
    LocalizationHelper providesLocalizationHelper() {
        return new LocalizationHelper();
    }

    @Provides
    @ApplicationLevel
    BannerHelper providesBannerHelper(SharedPrefHelper sharedPrefHelper, Context context, NetworkHelper networkHelper) {
        return new BannerHelper(sharedPrefHelper, context, networkHelper);
    }

    @Provides
    @ApplicationLevel
    ComponentHelper providesComponentHelper(SharedPrefHelper sharedPrefHelper, ScreenHelper screenHelper) {
        return new ComponentHelper(screenHelper, sharedPrefHelper);
    }

    @Provides
    @ApplicationLevel
    RequestHelper providesRequestHelper(Context context, SharedPrefHelper sharedPrefHelper) {
        return new RequestHelper(context, sharedPrefHelper);
    }

}

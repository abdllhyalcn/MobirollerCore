package com.mobiroller.core.interfaces;

import androidx.appcompat.app.AppCompatActivity;

import com.mobiroller.core.AppComponent;
import com.mobiroller.core.activities.AuthorizationActivity;
import com.mobiroller.core.activities.ConnectionRequired;
import com.mobiroller.core.activities.GenericActivity;
import com.mobiroller.core.activities.ImagePagerActivity;
import com.mobiroller.core.activities.PermissionRequiredActivity;
import com.mobiroller.core.activities.aveCallNowView;
import com.mobiroller.core.activities.aveFullScreenVideo;
import com.mobiroller.core.activities.aveRSSView;
import com.mobiroller.core.activities.aveRssContentViewPager;
import com.mobiroller.core.activities.aveShareView;
import com.mobiroller.core.activities.aveWebView;
import com.mobiroller.core.module.ActivityModule;
import com.mobiroller.core.scopes.PerActivity;

import dagger.Component;

/**
 * Created by ealtaca on 23.05.2017.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {

    void inject(PermissionRequiredActivity splashActivity);

    void inject(AuthorizationActivity splashActivity);

    void inject(aveCallNowView splashActivity);

    void inject(ConnectionRequired splashActivity);

    void inject(aveFullScreenVideo splashActivity);

    void inject(GenericActivity splashActivity);

    AppCompatActivity inject(AppCompatActivity splashActivity);

    void inject(aveRssContentViewPager splashActivity);

    void inject(aveRSSView splashActivity);

    void inject(aveShareView splashActivity);

    void inject(aveWebView splashActivity);

    void inject(ImagePagerActivity splashActivity);

}

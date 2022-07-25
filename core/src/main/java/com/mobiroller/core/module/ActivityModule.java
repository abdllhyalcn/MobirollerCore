package com.mobiroller.core.module;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.mobiroller.core.scopes.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Activity Module provides helpers to activities
 * <p>
 * Created by ealtaca on 23.05.2017.
 */

@Module
public class ActivityModule {

    private AppCompatActivity appCompatActivity;

    public ActivityModule(AppCompatActivity application) {
        appCompatActivity = application;
    }


    @Provides
    @PerActivity
    Context providesApplicationContext() {
        return appCompatActivity;
    }


    @Provides
    @PerActivity
    Activity providesAppCompatActivity() {
        return appCompatActivity;
    }

}

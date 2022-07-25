package com.mobiroller.core.module;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.mobiroller.core.scopes.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Fragment Module provides helpers to fragments
 * <p>
 * Created by ealtaca on 30.05.2017.
 */
@Module
public class FragmentModule {

    private Fragment fragment;
    private Activity activity;

    public FragmentModule(Fragment fragment, Activity activity) {
        this.fragment = fragment;
        this.activity = activity;
    }

    @Provides
    @PerFragment
    Activity providesAppCompatActivity() {
        return activity;
    }

}

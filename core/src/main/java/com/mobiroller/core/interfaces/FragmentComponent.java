package com.mobiroller.core.interfaces;


import androidx.fragment.app.Fragment;

import com.mobiroller.core.AppComponent;
import com.mobiroller.core.fragments.BaseModuleFragment;
import com.mobiroller.core.fragments.preview.NotSupportedFragment;
import com.mobiroller.core.module.FragmentModule;
import com.mobiroller.core.scopes.PerFragment;

import dagger.Component;

/**
 * Created by ealtaca on 30.05.2017.
 */

@PerFragment
@Component(dependencies = AppComponent.class, modules = {FragmentModule.class})
public interface FragmentComponent {

    Fragment inject(Fragment baseFragment);

    void inject(BaseModuleFragment baseFragment);

    void inject(NotSupportedFragment baseFragment);

}

package com.mobiroller.core;

import androidx.fragment.app.Fragment;

public class FragmentHelper {

    public Fragment getFragment(String screenType) {
        return SharedApplication.app.getFragmentHelper().getFragment(screenType);
    }
}

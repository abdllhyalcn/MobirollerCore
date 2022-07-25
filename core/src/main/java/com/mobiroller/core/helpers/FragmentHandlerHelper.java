package com.mobiroller.core.helpers;

import androidx.fragment.app.Fragment;

import com.mobiroller.core.FragmentHelper;
import com.mobiroller.core.fragments.avePDFViewFragment;

public class FragmentHandlerHelper {

    public static Fragment getFragment(String screenType,String subScreenType) {
        Fragment fragment = new FragmentHelper().getFragment(screenType);

        if(subScreenType!=null)
        {
            if ("avePDFView".equals(subScreenType)) {
                fragment = new avePDFViewFragment();
            }
        }

        return fragment;
    }
}

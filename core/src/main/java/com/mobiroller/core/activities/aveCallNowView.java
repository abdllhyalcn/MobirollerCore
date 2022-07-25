package com.mobiroller.core.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mobiroller.core.activities.base.AveActivity;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.fragments.aveCallNowViewFragment;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.interfaces.ActivityComponent;
import com.mobiroller.core.R;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class aveCallNowView extends AveActivity {

    @Inject
    LegacyToolbarHelper toolbarHelper;
    @Inject
    LocalizationHelper localizationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_with_toolbar);
        ButterKnife.bind(this);

        if (screenModel!=null) {
            toolbarHelper.setToolbarTitle(this,localizationHelper.getLocalizedTitle(screenModel.getTitle()));
        } else {
            toolbarHelper.setToolbarTitle(this,"");
        }

        Bundle bundle = new Bundle();
        JSONStorage.putScreenModel(screenId,screenModel);
        bundle.putString(Constants.KEY_SCREEN_TYPE,screenType);
        bundle.putString(Constants.KEY_SCREEN_ID, screenId);
        aveCallNowViewFragment callNowViewFragment = new aveCallNowViewFragment();
        callNowViewFragment.setArguments(bundle);
        setFragment(callNowViewFragment, "callNowViewFragment");
    }

    @Override
    public AppCompatActivity injectActivity(ActivityComponent component) {
        component.inject(this);
        return this;
    }
}

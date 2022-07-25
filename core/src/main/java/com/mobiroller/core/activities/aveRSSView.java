package com.mobiroller.core.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mobiroller.core.activities.base.AveActivity;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.fragments.aveRSSViewFragment;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.interfaces.ActivityComponent;
import com.mobiroller.core.R;

import javax.inject.Inject;

import static com.mobiroller.core.constants.Constants.KEY_SCREEN_RSS_PUSH_TITLE;

public class aveRSSView extends AveActivity {

    @Inject
    LegacyToolbarHelper toolbarHelper;
    @Inject
    LocalizationHelper localizationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_with_toolbar);
        setMobirollerToolbar(findViewById(R.id.toolbar_top));
        if (screenModel != null) {
            toolbarHelper.setToolbarTitle(this, localizationHelper.getLocalizedTitle(screenModel.getTitle()));
        } else {
            toolbarHelper.setToolbarTitle(this, "");
        }
        Bundle bundle = new Bundle();
        JSONStorage.putScreenModel(screenId,screenModel);
        bundle.putString(Constants.KEY_SCREEN_TYPE, screenType);
        bundle.putString(Constants.KEY_SCREEN_ID, screenId);
        if (getIntent().hasExtra(KEY_SCREEN_RSS_PUSH_TITLE)) {
            bundle.putString(KEY_SCREEN_RSS_PUSH_TITLE, getIntent().getStringExtra(KEY_SCREEN_RSS_PUSH_TITLE));
        }
        aveRSSViewFragment rssViewFragment = new aveRSSViewFragment();
        rssViewFragment.setArguments(bundle);
        setFragment(rssViewFragment, "rssFragment");
    }

    @Override
    public AppCompatActivity injectActivity(ActivityComponent component) {
        component.inject(this);
        return this;
    }

}
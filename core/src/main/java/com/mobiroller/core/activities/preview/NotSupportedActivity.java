package com.mobiroller.core.activities.preview;

import static com.mobiroller.core.constants.Constants.MODULE_ECOMMERCE_PRO_VIEW;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.fragments.preview.NotSupportedFragment;
import com.mobiroller.core.helpers.LegacyToolbarHelper;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.R;

import butterknife.ButterKnife;

public class NotSupportedActivity extends AppCompatActivity {
    SharedPrefHelper sharedPrefHelper;
    LegacyToolbarHelper toolbarHelper;
    LocalizationHelper localizationHelper;
    private String mScreenType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_with_toolbar);
        ButterKnife.bind(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        toolbarHelper = new LegacyToolbarHelper();
        localizationHelper = new LocalizationHelper();
        toolbarHelper.setToolbarTitle(this, localizationHelper.getLocalizedTitle(getIntent().getStringExtra("title")));
        mScreenType = getIntent().getStringExtra(Constants.KEY_SCREEN_TYPE);
        NotSupportedFragment fragment = new NotSupportedFragment();
        if(mScreenType.equalsIgnoreCase("aveChatView"))
            fragment.setModule(R.string.chat_module);
        else if(mScreenType.equalsIgnoreCase("aveECommerceView"))
            fragment.setModule(R.string.ecommerce_module);
        else if(mScreenType.equalsIgnoreCase(MODULE_ECOMMERCE_PRO_VIEW))
            fragment.setModule(R.string.ecommerce_module);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment, "notSupported");
        fragmentTransaction.commit();
    }
}

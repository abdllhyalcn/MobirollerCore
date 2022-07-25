package com.mobiroller.core.fragments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.applyze.ApplyzeAnalytics;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.appbar.AppBarLayout;
import com.mobiroller.core.constants.DynamicConstants;
import com.mobiroller.core.activities.base.AveActivity;
import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.helpers.BannerHelper;
import com.mobiroller.core.helpers.ComponentHelper;
import com.mobiroller.core.helpers.FavoriteHelper;
import com.mobiroller.core.helpers.JSONParser;
import com.mobiroller.core.helpers.JSONStorage;
import com.mobiroller.core.coreui.helpers.LocalizationHelper;
import com.mobiroller.core.helpers.NetworkHelper;
import com.mobiroller.core.helpers.ScreenHelper;
import com.mobiroller.core.helpers.SharedPrefHelper;
import com.mobiroller.core.helpers.UtilManager;
import com.mobiroller.core.interfaces.FragmentComponent;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.R;
import com.mobiroller.core.util.ImageManager;

import javax.inject.Inject;

import butterknife.Unbinder;

public class BaseModuleFragment extends BaseFragment {

    public SharedPrefHelper sharedPrefHelper;
    public NetworkHelper networkHelper;
    public LocalizationHelper localizationHelper;
    @Inject
    protected BannerHelper bannerHelper;
    @Inject
    public ScreenHelper screenHelper;
    @Inject
    SharedPrefHelper mSharedPrf;
    @Inject
    ImageManager imageManager;
    @Inject
    JSONParser jsonParser;
    @Inject
    ComponentHelper componentHelper;


    FavoriteHelper favoriteHelper;
    public ScreenModel screenModel;
    public String screenId;
    public String screenType;
    public String subScreenType;

    protected Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefHelper = UtilManager.sharedPrefHelper();
        networkHelper = UtilManager.networkHelper();
        localizationHelper = UtilManager.localizationHelper();
        checkScreenValues();
        favoriteHelper = new FavoriteHelper(getActivity());
        if (screenModel != null)
            setHasOptionsMenu(true);
        if (!DynamicConstants.MobiRoller_Stage)
            checkStats();
        else
            setPreviewButton();

        if (((AveActivity) getActivity()).getToolbar() == null)
            return;
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) ((AveActivity) getActivity()).getToolbar().getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
    }

    @Override
    public Fragment injectFragment(FragmentComponent component) {
        component.inject(this);
        return this;
    }

    protected void setPreviewButton() {
//        if (!(screenModel == null || screenModel.isRssContent()))
//            layoutHelper.setPreviewButton(getActivity().getIntent(), getActivity());
    }

    private void checkScreenValues() {
        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.KEY_SCREEN_ID)) {
                    screenId =getArguments().getString(Constants.KEY_SCREEN_ID);
            }

            if (screenId !=null && JSONStorage.containsScreen(screenId))
                screenModel = JSONStorage.getScreenModel(screenId);
            if (getArguments().containsKey(Constants.KEY_SCREEN_TYPE))
                screenType = getArguments().getString(Constants.KEY_SCREEN_TYPE);
            if (getArguments().containsKey(Constants.KEY_SUB_SCREEN_TYPE))
                subScreenType = getArguments().getString(Constants.KEY_SUB_SCREEN_TYPE);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mSharedPrf.isFavoriteActive() && favoriteHelper.isScreenAvailable(screenType) &&
                !(screenType.equalsIgnoreCase("aveHtmlView") && screenModel.getScreenType().equalsIgnoreCase("aveCatalogView"))) {
            ((AveActivity)getActivity()).getToolbar().inflateMenu(R.menu.favorite_menu);
            if (favoriteHelper.isScreenAddedToList(screenId)) {
                ((AveActivity)getActivity()).getToolbar().getMenu().findItem(R.id.action_favorite).setIcon(R.drawable.ic_bookmark_white_24dp);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            if (favoriteHelper.isScreenAddedToList(screenId)) {
                favoriteHelper.removeScreenContent(screenId);
            } else
                favoriteHelper.addScreenToList(screenModel, screenType, subScreenType, screenId);
            getActivity().invalidateOptionsMenu();
        } else {
            super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void sendStats() {
        ApplyzeAnalytics.getInstance().sendScreenEvent(localizationHelper.getLocalizedTitle(screenModel.getTitle()));
        //TODO Analytics
//        String screenType = getScreenTypeAnalyticsName();
//        setScreenName(screenType + " - " + localizationHelper.getLocalizedTitle(screenModel.getTitle()));
    }

    private String getScreenType() {
        if (screenModel.getScreenType() != null)
            return screenModel.getScreenType();
        else
            return screenType;
    }

    private String getScreenTypeAnalyticsName() {
        String screenType = getScreenType();
        String[] moduleAnalyticsName = getActivity().getResources().getStringArray(R.array.module_analytics_name);
        String[] moduleAnalyticsValue = getActivity().getResources().getStringArray(R.array.module_analytics_value);
        for (int i = 0; i < moduleAnalyticsName.length; i++) {
            if (screenType.equalsIgnoreCase(moduleAnalyticsName[i])) {
                screenType = moduleAnalyticsValue[i];
                break;
            }
        }
        return screenType;
    }

    private void checkStats() {
        try {
            if (screenModel != null) {
                sendStats();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder!=null)
            unbinder.unbind();
    }

}

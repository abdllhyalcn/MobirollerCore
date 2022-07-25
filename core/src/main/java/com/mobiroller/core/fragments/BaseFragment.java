package com.mobiroller.core.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.mobiroller.core.SharedApplication;
import com.mobiroller.core.coreui.CoreFragment;
import com.mobiroller.core.interfaces.DaggerFragmentComponent;
import com.mobiroller.core.interfaces.FragmentComponent;
import com.mobiroller.core.coreui.models.ScreenModel;
import com.mobiroller.core.module.FragmentModule;

/**
 * Created by ealtaca on 27.12.2016.
 */

public abstract class BaseFragment extends CoreFragment {

    public ScreenModel screenModel;
    public String screenId;
    public String screenType;
    private FragmentComponent fragmentComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent = fragmentComponent();
        fragmentComponent.inject(this);
        injectFragment(fragmentComponent);
    }

    public abstract Fragment injectFragment(FragmentComponent component);

    protected final FragmentComponent fragmentComponent() {
        if (fragmentComponent == null) {
            fragmentComponent = DaggerFragmentComponent.builder()
                    .appComponent(SharedApplication.getMainComponent())
                    .fragmentModule(new FragmentModule(this, getActivity()))
                    .build();
        }
        return fragmentComponent;
    }

    public void hideToolbar(Toolbar toolbar) {
        if(toolbar!=null)
            toolbar.setVisibility(View.GONE);
    }


    public int getStatusBarColor(int actionBarColor) {
        int color = actionBarColor;
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * 0.9f);
        int g = Math.round(Color.green(color) * 0.9f);
        int b = Math.round(Color.blue(color) * 0.9f);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    public void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



}

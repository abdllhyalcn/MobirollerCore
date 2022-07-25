package com.mobiroller.core.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

/**
 * Created by ealtaca on 27.02.2017.
 */

public abstract class BackHandledFragment extends BaseModuleFragment {
    protected BackHandlerInterface backHandlerInterface;

    public abstract String getTagText();

    public abstract boolean onBackPressed();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof BackHandlerInterface)) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        } else {
            backHandlerInterface = (BackHandlerInterface) getActivity();
        }
    }

    public void hideToolbar(Toolbar toolbar) {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Mark this fragment as the selected Fragment.
        backHandlerInterface.setSelectedFragment(this);
    }

    public interface BackHandlerInterface {
        void setSelectedFragment(BackHandledFragment backHandledFragment);
    }


    public int getStatusBarColor() {
        int color = sharedPrefHelper.getActionBarColor();
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * 0.9f);
        int g = Math.round(Color.green(color) * 0.9f);
        int b = Math.round(Color.blue(color) * 0.9f);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }
}
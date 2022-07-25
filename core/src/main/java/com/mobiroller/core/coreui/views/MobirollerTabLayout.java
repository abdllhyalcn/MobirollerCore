package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.tabs.TabLayout;
import com.mobiroller.core.coreui.Theme;

public class MobirollerTabLayout extends TabLayout {

    public MobirollerTabLayout(Context context) {
        super(context);
        init();
    }

    public MobirollerTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MobirollerTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setSelectedTabIndicatorColor(Theme.primaryColor);
    }

}

package com.mobiroller.core.coreui.views.legacy;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.coreui.util.ColorUtil;

public class MobirollerSwipeRefreshLayout extends SwipeRefreshLayout {

    public MobirollerSwipeRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public MobirollerSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEnabled(false);
        setTheme();
    }

    private void setTheme() {
        setColorSchemeColors(
                ColorUtil.isColorDark(Theme.primaryColor) ? Theme.primaryColor : Color.BLACK);
    }

}

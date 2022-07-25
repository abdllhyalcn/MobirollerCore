package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;
import com.mobiroller.core.coreui.Theme;

public class MPrimaryButton extends MaterialButton {

    public MPrimaryButton(Context context) {
        super(context);
        init();
    }

    public MPrimaryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MPrimaryButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTextColor(Theme.primaryColor);
    }
}

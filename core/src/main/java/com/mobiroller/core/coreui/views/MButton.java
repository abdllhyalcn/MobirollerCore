package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;
import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.coreui.helpers.SizeHelper;

public class MButton extends MaterialButton {

    public MButton(Context context) {
        super(context);
        init();
    }

    public MButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setCornerRadius(SizeHelper.convertDpToPixel(4, getContext()));
        setAllCaps(false);
        setBackgroundColor(Theme.primaryColor);
    }

}

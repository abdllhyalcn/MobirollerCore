package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputLayout;
import com.mobiroller.core.coreui.Theme;

public class MTextInputLayout extends TextInputLayout {

    public MTextInputLayout(Context context) {
        super(context);
        init();
    }

    public MTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBoxStrokeColor(Theme.primaryColor);
        setHintTextColor(ColorStateList.valueOf(Theme.primaryColor));
    }

}

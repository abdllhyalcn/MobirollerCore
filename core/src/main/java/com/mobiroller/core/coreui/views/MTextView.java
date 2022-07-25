package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.util.AttributeSet;

import com.mobiroller.core.coreui.Theme;

public class MTextView extends androidx.appcompat.widget.AppCompatTextView {

    public MTextView(Context context) {
        super(context);
        init();
    }

    public MTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTextColor(Theme.primaryColor);
    }

}

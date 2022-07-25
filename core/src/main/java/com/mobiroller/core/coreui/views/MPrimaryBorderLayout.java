package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.coreui.helpers.SizeHelper;

public class MPrimaryBorderLayout extends ConstraintLayout {


    public MPrimaryBorderLayout(Context context) {
        super(context);
        init();
    }

    public MPrimaryBorderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MPrimaryBorderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(SizeHelper.convertDpToPixel(1,getContext()), Theme.primaryColor);
        drawable.setCornerRadius(8);
        drawable.setColor(Color.TRANSPARENT);
        setBackground(drawable);
    }

}

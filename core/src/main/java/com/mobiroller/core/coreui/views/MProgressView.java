package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.github.ybq.android.spinkit.SpinKitView;

public class MProgressView extends SpinKitView {

    public MProgressView(Context context) {
        super(context);
    }

    public MProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        super.setIndeterminateDrawable(d);
    }

    @Override
    public boolean post(Runnable action) {
        return super.post(action);
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
    }
}

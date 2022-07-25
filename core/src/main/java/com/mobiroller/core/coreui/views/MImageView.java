package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;

public class MImageView extends androidx.appcompat.widget.AppCompatImageView {

    public MImageView(Context context) {
        super(context);
        init();
    }

    public MImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public void setImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .into(this);
    }
}

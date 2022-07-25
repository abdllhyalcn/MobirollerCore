package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.core.widget.ImageViewCompat;

import com.bumptech.glide.Glide;
import com.mobiroller.core.coreui.Theme;

public class MTintedImageView extends androidx.appcompat.widget.AppCompatImageView {

    public MTintedImageView(Context context) {
        super(context);
        init();
    }

    public MTintedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MTintedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(Theme.primaryColor));
    }

    public void setImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .into(this);
    }
}

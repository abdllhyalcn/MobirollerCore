package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;

import androidx.core.widget.CompoundButtonCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.mobiroller.core.coreui.Theme;

public class MSwitchView extends SwitchMaterial {

    public MSwitchView(Context context) {
        super(context);
        init();
    }

    public MSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {Color.WHITE, Theme.primaryColor, Color.WHITE};
        CompoundButtonCompat.setButtonTintList(this, new ColorStateList(states, colors));
        ColorStateList thumbStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                colors
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setThumbTintList(thumbStates);
        }
    }

}

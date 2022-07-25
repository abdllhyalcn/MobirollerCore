package com.mobiroller.core.coreui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.mobiroller.core.R;
import com.mobiroller.core.coreui.Theme;

import org.jetbrains.annotations.NotNull;

public class MRadioButton extends MaterialRadioButton {

    public MRadioButton(@NonNull @NotNull Context context) {
        super(context);
        init();
    }

    public MRadioButton(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MRadioButton(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.DKGRAY
                        , Theme.primaryColor,
                }
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setButtonTintList(colorStateList);
        }
    }

    public void setValue(String value, Boolean setLayout) {

        if(setLayout)
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextAppearance(R.style.BodyS);
        } else {
            setTextAppearance(getContext(), R.style.BodyS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setLayoutDirection(LAYOUT_DIRECTION_RTL);
            setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
        }
        setText(value);

    }

    public void setValue(String value) {
        setValue(value,true);
    }
}

package com.mobiroller.core.enums;

import com.mobiroller.core.R;

/**
 * Created by ealtaca on 9/26/17.
 */

public enum FontStyle {
    Small(R.style.FontStyle_Small, "Small"),
    Medium(R.style.FontStyle_Medium, "Medium"),
    Large(R.style.FontStyle_Large, "Large"),
    XLarge(R.style.FontStyle_XLarge, "XLarge");

    private int resId;
    private String title;

    public int getResId() {
        return resId;
    }

    public String getTitle() {
        return title;
    }

    FontStyle(int resId, String title) {
        this.resId = resId;
        this.title = title;
    }
}
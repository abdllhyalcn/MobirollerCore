package com.mobiroller.core.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.mobiroller.core.R;

public class DrawableUtil {

    private static Drawable uncheckedCheckBoxDrawable;
    private static Drawable checkedCheckBoxDrawable;

    public static Drawable getUncheckedCheckboxDrawable(Context context) {
        if (uncheckedCheckBoxDrawable == null)
            uncheckedCheckBoxDrawable = ContextCompat.getDrawable(context, R.drawable.unchecked_radio_button);
        return uncheckedCheckBoxDrawable;
    }
    public static Drawable getCheckedCheckboxDrawable(Context context) {
        if (checkedCheckBoxDrawable == null)
            checkedCheckBoxDrawable = ContextCompat.getDrawable(context, R.drawable.checked_radio_button);
        return checkedCheckBoxDrawable;
    }

}

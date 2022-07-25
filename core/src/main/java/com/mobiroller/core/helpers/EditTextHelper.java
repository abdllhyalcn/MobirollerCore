package com.mobiroller.core.helpers;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;

/**
 * Created by ealtaca on 6/14/17.
 */

public class EditTextHelper {

    public static void setCursorColor(EditText view, @ColorInt int color) {
        try {
            // Get the cursor resource id
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(view);

            // Get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(view);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }

    public static void setUnderLineColor(EditText editText,@ColorInt int color)
    {
        editText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public static void setHintTextColor(EditText editText,@ColorInt int color)
    {
        editText.setHintTextColor(color);
    }

    public static void setAllColors(EditText editText,@ColorInt int color)
    {
        setHintTextColor(editText,color);
        setUnderLineColor(editText,color);
        setCursorColor(editText,color);
        editText.setTextColor(color);
    }
}

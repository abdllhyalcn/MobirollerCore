package com.mobiroller.core.util;

import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Field;

public class ViewUtil {

    public static TextView getTitleTextView(Toolbar toolbar) {
        try {
            Class<?> toolbarClass = Toolbar.class;
            Field titleTextViewField = toolbarClass.getDeclaredField("mTitleTextView");
            titleTextViewField.setAccessible(true);
            TextView titleTextView = (TextView) titleTextViewField.get(toolbar);

            return titleTextView;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

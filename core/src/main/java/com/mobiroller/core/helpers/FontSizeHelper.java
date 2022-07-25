package com.mobiroller.core.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.mobiroller.core.enums.FontStyle;

/**
 * Created by ealtaca on 9/26/17.
 */

public class FontSizeHelper {
    private final static String FONT_STYLE = "FONT_STYLE";
    private final static String CONTENT_FONT_STYLE = "CONTENT_FONT_STYLE";

    private final Context context;

    public FontSizeHelper(Context context) {
        this.context = context;
    }

    protected SharedPreferences open() {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor edit() {
        return open().edit();
    }

    public FontStyle getFontStyle() {
        return FontStyle.valueOf(open().getString(FONT_STYLE,
                FontStyle.Medium.name()));
    }

    public FontStyle getContentFontStyle() {
        return FontStyle.valueOf(open().getString(CONTENT_FONT_STYLE,
                FontStyle.Medium.name()));
    }

    public int getFontOrder() {
        String fontStyle = open().getString(FONT_STYLE,
                FontStyle.Medium.name());
        if (fontStyle.equalsIgnoreCase("Small"))
            return 0;
        else if (fontStyle.equalsIgnoreCase("Medium"))
            return 1;
        else if (fontStyle.equalsIgnoreCase("Large"))
            return 2;
        else if (fontStyle.equalsIgnoreCase("XLarge"))
            return 3;
        return 1;
    }

    public int getContentFontOrder() {
        if(!open().contains(CONTENT_FONT_STYLE))
            return -1;
        String fontStyle = open().getString(CONTENT_FONT_STYLE,
                FontStyle.Medium.name());
        if (fontStyle.equalsIgnoreCase("Small"))
            return 0;
        else if (fontStyle.equalsIgnoreCase("Medium"))
            return 1;
        else if (fontStyle.equalsIgnoreCase("Large"))
            return 2;
        else if (fontStyle.equalsIgnoreCase("XLarge"))
            return 3;
        return 1;
    }

    public void setFontStyle(FontStyle style) {
        edit().putString(FONT_STYLE, style.name()).commit();
    }

    public void setContentFontStyle(FontStyle style) {
        edit().putString(CONTENT_FONT_STYLE, style.name()).commit();
    }
}

package com.mobiroller.core.coreui.enums;

import android.content.Context;
import android.graphics.Typeface;

import com.mobiroller.core.coreui.Theme;
import com.mobiroller.core.coreui.util.cache.FontCache;

public enum FontTypeEnum {

    HeadOne(Theme.headOneTypeface,24, 0),
    HeadTwo(Theme.headTwoTypeface, 18,1),
    HeadingOne(Theme.headingOneTypeface, 14,2),
    HeadingTwo(Theme.headingTwoTypeface, 14,3),
    Paragraph(Theme.paragraphTypeface, 14,4),
    Span(Theme.spanTypeface,12,5),
    Badge(Theme.headingOneTypeface,9,6),
    BadgeLarge(Theme.headingOneTypeface,10,7);

    private int resId;
    private float fontSize;
    private int resOrder;

    FontTypeEnum(int resId, float fontSize, int resOrder) {
        this.resId = resId;
        this.fontSize = fontSize;
        this.resOrder = resOrder;
    }

    public int getResId() {
        return resId;
    }

    public int getResOrder() {
        return resOrder;
    }

    public float getFontSize() {
        return fontSize;
    }

    public static Typeface getResIdByResOrder(int order, Context context) {
        for(FontTypeEnum e : FontTypeEnum.values()){
            if(order == e.resOrder) return FontCache.getTypeface(context, e.getResId());
        }
        return null;
    }

    public static float getFontSizeByResOrder(int order) {
        for(FontTypeEnum e : FontTypeEnum.values()){
            if(order == e.resOrder) return e.getFontSize();
        }
        return 14;
    }

}

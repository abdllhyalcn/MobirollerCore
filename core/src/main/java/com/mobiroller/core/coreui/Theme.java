package com.mobiroller.core.coreui;

import android.graphics.Color;

import com.mobiroller.core.R;
import com.mobiroller.core.coreui.helpers.ColorHelper;

public class Theme {

    public static int primaryColor = Color.parseColor("#37495f");
    //    public static int primaryColor = Color.WHITE;
    public static int secondaryColor = Color.parseColor("#ffad01");
    public static int darkColor = Color.parseColor("#0039D1");
    public static int headerColor = Color.parseColor("#0045FF");
    public static int textColor = Color.parseColor("#969fa2");
    public static int paragraphColor = Color.parseColor("#455154");

    public static int buttonPrimaryColor = Color.BLACK;
    public static int textPrimaryColor = Color.BLACK;
    public static int textSecondaryColor = Color.parseColor("#455154");
    public static int imagePrimaryColor = Color.BLACK;


    public static int headOneTypeface = R.font.poppins_bold;
    public static int headTwoTypeface = R.font.poppins_bold;
    public static int headingOneTypeface = R.font.poppins_bold;
    public static int headingTwoTypeface = R.font.poppins_semibold;
    public static int paragraphTypeface = R.font.poppins_regular;
    public static int spanTypeface = R.font.poppins_regular;


    public static int antiColor = ColorHelper.isColorDark(primaryColor) ? Color.WHITE : Color.BLACK;

    public static int progressType = 2;
    public static int progressColor = Color.BLUE;


}



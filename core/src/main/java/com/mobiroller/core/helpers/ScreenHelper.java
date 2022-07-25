package com.mobiroller.core.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;

import com.mobiroller.core.constants.Constants;
import com.mobiroller.core.coreui.models.ColorModel;

public class ScreenHelper {

    private Context context;
    private SharedPrefHelper sharedPrefHelper;
    public ScreenHelper(Context context,SharedPrefHelper sharedPrefHelper) {
        this.sharedPrefHelper = sharedPrefHelper;
        this.context = context;
    }
    public ScreenHelper(Context context) {
        this.sharedPrefHelper = UtilManager.sharedPrefHelper();
        this.context = context;


    }


    public int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    @SuppressWarnings("static-access")
    public static int setColorUnselected(ColorModel colorModel) {
        Color myColor = new Color();
        float alpha = 0, red = 0, green = 0, blue = 0;
        try {
            alpha = Float.parseFloat(String.valueOf(colorModel.getAlpha())) * 255;
            red = Float.parseFloat(String.valueOf(colorModel.getRed())) * 255;
            green = Float.parseFloat(String.valueOf(colorModel.getGreen())) * 255;
            blue = Float.parseFloat(String.valueOf(colorModel.getBlue())) * 255;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myColor.argb(Math.round(alpha), Math.round(red), Math.round(green), Math.round(blue));
    }


    @SuppressWarnings("static-access")
    public static int setColorWithNoAlpha(ColorModel colorModel) {
        Color myColor = new Color();
        float alpha = 0, red = 0, green = 0, blue = 0;
        try {
            alpha = 255;
            red = Float.parseFloat(String.valueOf(colorModel.getRed())) * 255;
            green = Float.parseFloat(String.valueOf(colorModel.getGreen())) * 255;
            blue = Float.parseFloat(String.valueOf(colorModel.getBlue())) * 255;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myColor.argb(Math.round(alpha), Math.round(red), Math.round(green), Math.round(blue));
    }


    // We're using Hover Effect color instead of this


    public int setColorSelected(ColorModel colorModel) {
        Color myColor = new Color();
        float alpha = 0, red = 0, green = 0, blue = 0;
        try {
            alpha = (Float.parseFloat(String.valueOf(colorModel.getAlpha())) * 255) / 2;
            red = Float.parseFloat(String.valueOf(colorModel.getRed())) * 255;
            green = Float.parseFloat(String.valueOf(colorModel.getGreen())) * 255;
            blue = Float.parseFloat(String.valueOf(colorModel.getBlue())) * 255;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return Color.argb(Math.round(alpha), Math.round(red), Math.round(green), Math.round(blue));
    }


    // Color for Hover Effect (Using on Tab Bar for now)
    public int setColorHover() {
        Color myColor = new Color();
        float alpha = 0.2f, red = 1, green = 1, blue = 1;
        alpha = alpha * 255;
        red = red * 255;
        green = green * 255;
        blue = blue * 255;
        return Color.argb(Math.round(alpha), Math.round(red), Math.round(green), Math.round(blue));
    }

    /**
     * Returns darker version of specified <code>color</code>.
     */
    public static int getDarkerColor(int color, float factor) {
        int a = Color.alpha( color );
        int r = Color.red( color );
        int g = Color.green( color );
        int b = Color.blue( color );

        return Color.argb( a,
                Math.max( (int)(r * factor), 0 ),
                Math.max( (int)(g * factor), 0 ),
                Math.max( (int)(b * factor), 0 ) );
    }

    /**
     * Lightens a color by a given factor.
     *
     * @param color
     *            The color to lighten
     * @param factor
     *            The factor to lighten the color. 0 will make the color unchanged. 1 will make the
     *            color white.
     * @return lighter version of the specified color.
     */
    public static int getLighterColor(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    /**
     * @param darkerFactor
     *          The factor to darken the color. 1 will make the color unchanged. 0 will make the
     *            color black.
     * @param lighterFactor
     *          The factor to lighten the color. 0 will make the color unchanged. 1 will make the
     *            color white.
     * @param color
     *          Main color to create gradient object
     * @return
     */
    public static GradientDrawable getGradientBackground(int color, float lighterFactor, float darkerFactor){
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]
                        {
                            getLighterColor(color,0),
                            getDarkerColor(color,0.5f)
                        });
        gd.setCornerRadius(0f);
        return gd;
    }

    /**
     * Get font by given name from assets folder
     * (Every font that supported by MobiRoller in assets/fonts folder)
     * Return typeface of font if it exists else return default font (HelveticaNeue)
     *
     * @param fontName
     * @return
     */
    public Typeface getFontFromAsset(String fontName) {
        Typeface face;
        try {
            face = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName + ".ttf");
        } catch (Exception e) {
            String defaultFont = "HelveticaNeue";
            face = Typeface.createFromAsset(context.getAssets(), "fonts/" + defaultFont + ".ttf");
        }
        return face;
    }

    public static int getHeightForDevice(int height, Activity appCompatActivity) {
        DisplayMetrics dm = new DisplayMetrics();
        appCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int device_height = dm.heightPixels - UtilManager.sharedPrefHelper().getStatusBarHeight();
        if (UtilManager.sharedPrefHelper().getTabActive())
            return Math.round((height * (device_height - UtilManager.sharedPrefHelper().getTabHeight()) / (Constants.MobiRoller_Preferences_StandardHeight)));
        else
            return Math.round((height * (device_height) / (Constants.MobiRoller_Preferences_StandardHeight)));
    }


    public static int getDeviceHeight(Activity appCompatActivity) {
        DisplayMetrics dm = new DisplayMetrics();
        appCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int device_height = dm.heightPixels - UtilManager.sharedPrefHelper().getStatusBarHeight();
        return device_height;
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}

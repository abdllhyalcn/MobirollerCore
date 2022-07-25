package com.mobiroller.core.coreui.models;

import android.graphics.Color;

import androidx.annotation.ColorInt;

import java.io.Serializable;

/**
 * Created by ealtaca on 07.03.2017.
 */

public class ColorModel implements Serializable {
    private String aveColorID;
    private Double red;
    private Double green;
    private Double blue;
    private Double alpha;

    public String getAveColorID() {
        return aveColorID;
    }

    public void setAveColorID(String aveColorID) {
        this.aveColorID = aveColorID;
    }

    public Double getRed() {
        return red;
    }

    public void setRed(Double red) {
        this.red = red;
    }

    public Double getGreen() {
        return green;
    }

    public void setGreen(Double green) {
        this.green = green;
    }

    public Double getBlue() {
        return blue;
    }

    public void setBlue(Double blue) {
        this.blue = blue;
    }

    public Double getAlpha() {
        return alpha;
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }

    public @ColorInt
    int getColor()
    {

        Color myColor = new Color();
        float alpha = 0, red = 0, green = 0, blue = 0;
        try {
            alpha = Float.parseFloat(String.valueOf(getAlpha())) * 255;
            red = Float.parseFloat(String.valueOf(getRed())) * 255;
            green = Float.parseFloat(String.valueOf(getGreen())) * 255;
            blue = Float.parseFloat(String.valueOf(getBlue())) * 255;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Color.argb(Math.round(alpha), Math.round(red), Math.round(green), Math.round(blue));
    }
}

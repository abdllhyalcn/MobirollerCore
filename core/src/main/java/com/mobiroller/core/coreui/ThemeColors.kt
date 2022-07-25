package com.mobiroller.core.coreui

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import java.lang.String

class ThemeColors  {

    @ColorInt
    var color = 0

    fun setTheme(context: Context) {

        color = Theme.primaryColor
        var hex = String.format("#%06X", 0xFFFFFF and color)
        if (hex.length < 6) {
            hex = "0$hex"
        }
        context.setTheme(context.resources.getIdentifier("T_$hex", "style", context.getPackageName()))
    }


    private fun isLightActionBar(): Boolean { // Checking if title text color will be black
        val rgb: Int = (Color.red(color) + Color.green(color) + Color.blue(color)) / 3
        return rgb > 210
    }
}
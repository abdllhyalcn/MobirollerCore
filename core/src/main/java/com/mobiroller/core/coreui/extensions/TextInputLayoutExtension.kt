package com.mobiroller.core.coreui.extensions

import android.content.res.ColorStateList
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout?.setColors(colors: ColorStateList) {
    this?.apply {
        hintTextColor = colors
        defaultHintTextColor = colors
        placeholderTextColor = colors
        setBoxStrokeColorStateList(colors)
    }
}
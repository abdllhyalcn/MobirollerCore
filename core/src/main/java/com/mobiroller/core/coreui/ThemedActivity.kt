package com.mobiroller.core.coreui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


open class ThemedActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeColors().setTheme(this)
        super.onCreate(savedInstanceState)

    }
}
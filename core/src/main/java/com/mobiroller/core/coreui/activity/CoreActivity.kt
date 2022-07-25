package com.mobiroller.core.coreui.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobiroller.core.coreui.helpers.LocaleHelper.setLocale

open class CoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setLocale(baseContext)
        super.onCreate(savedInstanceState)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(setLocale(base!!))
    }

}
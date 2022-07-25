package com.mobiroller.core.coreui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mobiroller.core.coreui.helpers.LocaleHelper

open class CoreFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.let { LocaleHelper.setLocale(it) }
        super.onCreate(savedInstanceState)
    }
}
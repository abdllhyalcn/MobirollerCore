package com.mobiroller.core.coreui.extensions

import android.content.Context
import androidx.core.content.ContextCompat

fun Context.getResColor(redId: Int) = ContextCompat.getColor(this, redId)
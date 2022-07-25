package com.mobiroller.core

import android.content.Context
import androidx.startup.Initializer
import com.mobiroller.core.helpers.UtilManager

class UtilManagerInitializer : Initializer<UtilManager> {

    override fun create(context: Context): UtilManager {
        UtilManager.Builder()
            .setContext(context)
            .build()
        return UtilManager()
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return emptyList()
    }
}

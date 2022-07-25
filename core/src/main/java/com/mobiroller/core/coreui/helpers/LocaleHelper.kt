package com.mobiroller.core.coreui.helpers

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.Log
import java.util.*

object LocaleHelper {

    @JvmStatic
    fun setAppLanguage(context: Context, localeCode: String): Context {
        var localeCode = localeCode
        Log.e("setAppLanguage: ", localeCode)
        if (localeCode == "us") localeCode = "en"
        localeCode = localeCode.toLowerCase()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, localeCode)
        } else updateResourcesLegacy(context, localeCode)
    }

    @JvmStatic
    fun setLocale(context: Context): Context {
        return if (DataStoreHelper(context).getLocaleCodes() != null) {
            setAppLanguage(context, getLocale(context))
        } else context
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {

        Log.e("setAppLanguage: ", "updateResources ".plus(language))
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    /**
     * get current locale
     */
    fun getLocale(res: Resources): Locale {
        val config = res.configuration
        return if (Build.VERSION.SDK_INT >= 24) config.locales[0] else config.locale
    }

    @JvmStatic
    fun getLocale(context: Context?): String {
        if (DataStoreHelper(context).getLanguageSetByUser() && getLocaleList(context).contains(
                DataStoreHelper(context).getDisplayLanguage()!!.toLowerCase())) {
            Log.e("setAppLanguage: 1", DataStoreHelper(context).getDisplayLanguage()!!.toLowerCase())
            return DataStoreHelper(context).getDisplayLanguage()!!.toLowerCase()
        }
        if (!DataStoreHelper(context).getLanguageSetByUser()) {
            if (DataStoreHelper(context).getLocaleCodes() != null) {
                val localeCodes = getLocaleList(context)
                Log.e("setAppLanguage: 2", DataStoreHelper(context).getDisplayLanguage()!!.toLowerCase())
                return if (localeCodes.contains(DataStoreHelper(context).getDisplayLanguage()!!.toLowerCase())) DataStoreHelper(context).getDisplayLanguage()!!.toLowerCase() else DataStoreHelper(context).getDefaultLanguage()!!.toLowerCase()
            }
        }

        Log.e("setAppLanguage: 3", DataStoreHelper(context).getDefaultLanguage()!!.toLowerCase())
        return DataStoreHelper(context).getDefaultLanguage()!!.toLowerCase()
    }

    @JvmStatic
    fun getLocaleList(context: Context?): List<String> {
        var localeCodes: List<String> = ArrayList()
        if (DataStoreHelper(context).getLocaleCodes() != null) {
            localeCodes = listOf(*DataStoreHelper(context).getLocaleCodes()!!.toLowerCase().split(",".toRegex()).toTypedArray())
        }
        return localeCodes
    }
}
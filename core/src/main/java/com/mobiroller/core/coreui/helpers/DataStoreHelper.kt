package com.mobiroller.core.coreui.helpers

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.mobiroller.core.coreui.constants.DataStoreConstants

class DataStoreHelper(context: Context?) {

    companion object {

        private var appSharedPrefs: SharedPreferences? = null
    }

    init {
        if (appSharedPrefs == null)
            appSharedPrefs = context?.getSharedPreferences(DataStoreConstants.PREFERENCES_KEY, Activity.MODE_PRIVATE)
    }

    fun getLanguageSetByUser(): Boolean {
        return getBoolean(DataStoreConstants.LANGUAGE_SET_BY_USER_KEY)
    }

    fun languageSetByUser() {
        put(DataStoreConstants.LANGUAGE_SET_BY_USER_KEY, true)
    }

    fun setLocaleCodes(languages: String?) {
        put(DataStoreConstants.LOCALE_CODES, languages)
    }

    fun getLocaleCodes(): String? {
        return getString(DataStoreConstants.LOCALE_CODES, null)?.toLowerCase()
    }

    fun setDefaultLanguage(lang: String?) {
        put(DataStoreConstants.DEFAULT_LANGUAGE, lang)
    }

    fun getDefaultLanguage(): String? {
        return getString(DataStoreConstants.DEFAULT_LANGUAGE, "TR")
    }

    fun setDisplayLanguage(lang: String?) {
        put(DataStoreConstants.DISPLAY_LANGUAGE, lang)
    }

    fun getDisplayLanguage(): String? {
        return getString(DataStoreConstants.DISPLAY_LANGUAGE, "")
    }

    fun setECommerceAliasKey(key: String?) {
        put(DataStoreConstants.ECOMMERCE_ALIAS_KEY, key)
    }

    fun getECommerceAliasKey(): String? {
        return getString(DataStoreConstants.ECOMMERCE_ALIAS_KEY, "")
    }

    private fun getString(key: String?, defaultValue: String? = null): String? {
        return appSharedPrefs!!.getString(key, defaultValue)
    }

    private fun getBoolean(key: String?, value: Boolean = false): Boolean {
        return appSharedPrefs?.getBoolean(key, value) ?: value
    }

    private fun put(key: String?, value: String?) {
        try {
            appSharedPrefs!!.edit().putString(key, value).apply()
        } catch (e: java.lang.Exception) {
            e.localizedMessage
        }
    }

    private fun put(key: String?, value: Boolean) {
        try {
            appSharedPrefs!!.edit().putBoolean(key, value).apply()
        } catch (e: java.lang.Exception) {
            e.localizedMessage
        }
    }

}
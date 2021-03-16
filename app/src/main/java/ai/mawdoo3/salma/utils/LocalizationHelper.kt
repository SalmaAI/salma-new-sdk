package com.banking.common.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.preference.PreferenceManager

import java.util.*

/**
 * Created by Omar Qadomi
 *
 * 16/3/2021
 */
object LocalizationHelper {

    const val LANG_ARABIC = "ar"
    const val LANG_ENGLISH = "en"

    private const val PREF_LOCALE = "PREF_LOCALE"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setLocale(context: Context, langCode: String): Context {
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    fun onAttach(context: Context): Context {
        val lang = getCurrentLocale(context)
        return setLocale(context, lang)
    }

    fun switchLocale(context: Context) {
        val sp = getSharedPreferences(context)
        val currentLanguage = getCurrentLocale(context)
        var newLanguage = ""
        if (currentLanguage == LANG_ENGLISH) {
            newLanguage = LANG_ARABIC
        } else {
            newLanguage = LANG_ENGLISH
        }
        sp.edit().putString(PREF_LOCALE, newLanguage).apply()
        setLocale(context, newLanguage)

    }

    fun setLanguage(context: Context, languageCode: String) {
        val sp = getSharedPreferences(context)
        sp.edit().putString(PREF_LOCALE, languageCode).apply()
        setLocale(context, languageCode)

    }

    public fun getCurrentLocale(context: Context): String {
        val sp = getSharedPreferences(context)
        return sp.getString(PREF_LOCALE, /*Default Localization*/ LANG_ARABIC)!!
    }
}
package com.banking.common.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.banking.common.utils.LocalizationHelper
import org.koin.android.ext.android.inject


open class BaseActivity : AppCompatActivity() {
    private val preferencesHelper: SharedPreferences by inject()
    var currentLanguage: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        currentLanguage = LocalizationHelper.getCurrentLocale(this)
        if (currentLanguage == LocalizationHelper.LANG_ARABIC) {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL;
        } else {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR;
        }
        super.onCreate(savedInstanceState)
    }


    override fun onResume() {
        super.onResume()
        val language = LocalizationHelper.getCurrentLocale(this)
        if (currentLanguage != language) {
            recreate()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalizationHelper.onAttach(newBase))
    }


}


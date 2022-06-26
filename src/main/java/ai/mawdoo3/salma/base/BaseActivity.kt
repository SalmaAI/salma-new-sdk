package ai.mawdoo3.salma.base

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.utils.LocalizationHelper
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject


open class BaseActivity : AppCompatActivity() {
    private val preferencesHelper: SharedPreferences by inject()
    var currentLanguage: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.Theme_Banking_Masa, true)
        //enable this if we want to support arabic/english localization in salma library
//        currentLanguage = LocalizationHelper.getCurrentLocale(this)
        currentLanguage = LocalizationHelper.LANG_ARABIC
        if (currentLanguage == LocalizationHelper.LANG_ARABIC) {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL;
        } else {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR;
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );

        super.onCreate(savedInstanceState)
    }


    override fun onResume() {
        super.onResume()
//        val language = LocalizationHelper.getCurrentLocale(this)
//        if (currentLanguage != language) {
//            recreate()
//        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalizationHelper.onAttach(newBase))
    }


}


package ai.mawdoo3.salma.base

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseActivity.Constants.BROADCAST_SESSION_ENDED
import ai.mawdoo3.salma.utils.AppUtils
import ai.mawdoo3.salma.utils.LocalizationHelper
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

open class BaseActivity : AppCompatActivity(), AppUtils.SessionListener {

    var currentLanguage: String = ""

    object Constants {
        const val BROADCAST_SESSION_ENDED = "BROADCAST_SESSION_ENDED"
    }

    private fun registerBroadcast() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastReceiver, IntentFilter(BROADCAST_SESSION_ENDED))
    }

    private fun unregisterBroadcast() {
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadCastReceiver)
    }

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                BROADCAST_SESSION_ENDED -> {
                    AppUtils.sessionEndedDialog(
                        this@BaseActivity,
                        R.string.general_error_message,
                        R.string.session_expired,
                        this@BaseActivity
                    )
                }
            }
        }
    }

    override fun onSessionEnded() {
        this.finish()
    }

    override fun onDestroy() {
        unregisterBroadcast()
        super.onDestroy()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        val intent = Intent("data_from_masa")
        intent.putExtra("onUserInteractionTriggered", true)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.Theme_Banking_Masa, true)
        currentLanguage = LocalizationHelper.LANG_ARABIC
        if (currentLanguage == LocalizationHelper.LANG_ARABIC) {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL;
        } else {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR;
        }
        super.onCreate(savedInstanceState)
        registerBroadcast()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalizationHelper.onAttach(newBase))
    }
}


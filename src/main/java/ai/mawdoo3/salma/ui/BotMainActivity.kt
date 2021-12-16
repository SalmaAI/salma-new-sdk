package ai.mawdoo3.salma.ui

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseActivity
import ai.mawdoo3.salma.databinding.ActivityMainBotBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotFragment
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem


class BotMainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        this.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_chatbot_close);
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true);
        this.supportActionBar?.title = getString(R.string.app_title)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GpsUtils.GPS_REQUEST && resultCode == Activity.RESULT_OK) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHostBotMain)
            val currentFragment = navHostFragment!!.childFragmentManager.fragments[0]
            if (currentFragment is ChatBotFragment) {
                currentFragment.requestCurrentLocation()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
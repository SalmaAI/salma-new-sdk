package ai.mawdoo3.salma.ui

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseActivity
import ai.mawdoo3.salma.databinding.ActivityMainBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotFragment
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem


class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        this.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_close);
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_analytics -> {

            }
            R.id.action_help -> {

            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
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
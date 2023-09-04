package ai.mawdoo3.salma.ui

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseActivity
import ai.mawdoo3.salma.databinding.ActivityMainBotBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotFragment
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

class BotMainActivity() : BaseActivity()
//    , KoinScopeComponent
{
    private lateinit var navController: NavController
    //lateinit var binding: ActivityMainBotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBotBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main_bot)
        //setSupportActionBar(binding.toolbar)
        this.supportActionBar?.title = getString(R.string.assistant_name)
        //navController = findNavController(R.id.navHostBotMain)

        /*
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )

         */
        /*
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            this.supportActionBar?.setDisplayHomeAsUpEnabled(true);
            if (destination.id == R.id.chatBotFragment) {
                this.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_chatbot_close);
            }
        }

         */
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

    override fun onSupportNavigateUp(): Boolean {
        if (!navController.navigateUp()) {
            //navigateUp() returns false if there are no more fragments to pop
            onBackPressed()
        }
        return navController.navigateUp()
    }


//    private val myKoin: Koin by lazy {
//        koinApplication {
//            androidContext(this@BotMainActivity)
//            modules(
//                listOf(
//                    appModule,
//                    ChatModule,
//                    remoteModule
//                )
//            )
//        }.koin
//    }
//
//    private val scopeID: ScopeID by lazy { getScopeId() }
//
//    override val scope : Scope by lazy {
//        activityScope()
//    }
//
//    override fun getKoin() = myKoin
}
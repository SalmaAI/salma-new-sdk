package ai.mawdoo3.salma

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class SalmaApplication : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }




}
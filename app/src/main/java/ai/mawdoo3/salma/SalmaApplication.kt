package ai.mawdoo3.salma

import ai.mawdoo3.salma.module.ChatModule
import ai.mawdoo3.salma.module.appModule
import ai.mawdoo3.salma.remote.remoteModule
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SalmaApplication : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SalmaApplication)
            val modules = modules(
                listOf(
                    appModule,
                    ChatModule,
                    remoteModule
                )
            )
        }
//        MasaSDK.initialize(this, "", "", "")
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


}
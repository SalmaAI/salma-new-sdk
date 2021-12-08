package ai.mawdoo3.salma

import ai.mawdoo3.salma.module.ChatModule
import ai.mawdoo3.salma.module.appModule
import ai.mawdoo3.salma.module.remoteModule
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
        MasaSdkInstance.initialize(
            "1",
            "LlfjJwVrMPJKeA",
            "72K5WY2T7rkNX55wcjkruIOPc5xydUmbjneS4HDuGiPQDMAuZd4izLWQxUCX1I84",
            "عمر"
        )
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


}
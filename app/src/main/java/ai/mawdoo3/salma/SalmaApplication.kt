package ai.mawdoo3.salma

import ai.mawdoo3.salma.module.ChatModule
import ai.mawdoo3.salma.module.appModule
import android.content.Context
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.banking.common.binding.BindingAdapters
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SalmaApplication : MultiDexApplication(), DataBindingComponent {

    private val appBindingAdapters: BindingAdapters by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SalmaApplication)
            val modules = modules(
                listOf(
                    appModule,
                    ChatModule
                )
            )
        }
        DataBindingUtil.setDefaultComponent(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun getBindingAdapters(): BindingAdapters {
        return appBindingAdapters
    }


}
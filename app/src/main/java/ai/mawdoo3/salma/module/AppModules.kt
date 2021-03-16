package ai.mawdoo3.salma.module

import android.content.SharedPreferences
import com.banking.common.binding.BindingAdapters
import com.banking.common.binding.BindingAdaptersImpl
import com.banking.common.utils.PreferenceHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val KOIN_NAME_DISPATCHER_MAIN = "DispatcherMain"
const val KOIN_NAME_DISPATCHER_IO = "DispatcherIO"

val appModule = module {
    single<SharedPreferences> {
        PreferenceHelper.customPrefs(get(), "DATA")
    }

    single<CoroutineDispatcher>(named(KOIN_NAME_DISPATCHER_MAIN)) {
        Dispatchers.Main
    }

    single<CoroutineDispatcher>(named(KOIN_NAME_DISPATCHER_IO)) {
        Dispatchers.IO
    }

    single<BindingAdapters> {
        BindingAdaptersImpl()
    }
}


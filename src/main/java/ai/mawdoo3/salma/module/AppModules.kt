package ai.mawdoo3.salma.module

import ai.mawdoo3.salma.utils.PreferenceHelper
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/*
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

}

 */


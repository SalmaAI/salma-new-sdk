package ai.mawdoo3.salma.module

import ai.mawdoo3.salma.data.dataSource.ChatRemoteDataSource
import ai.mawdoo3.salma.data.dataSource.ChatRepository
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.ui.chatBot.MessagesAdapter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val ChatModule = module {
    viewModel { ChatBotViewModel(get(), get()) }
    factory { params -> MessagesAdapter(viewModel = params.get()) }
    single { ChatRemoteDataSource(get(named("masaApiEndpoints"))) }
    single { ChatRepository(get()) }
}


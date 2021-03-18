package ai.mawdoo3.salma.module

import ai.mawdoo3.salma.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.chatBot.MessagesAdapter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val ChatModule = module {
    viewModel { ChatBotViewModel(get()) }
    factory { MessagesAdapter() }
}


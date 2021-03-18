package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.data.dataModel.SendMessageRequest
import ai.mawdoo3.salma.data.dataSource.ChatRepository
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.banking.common.base.BaseViewModel
import com.banking.core.remote.RepoErrorResponse
import com.banking.core.remote.RepoSuccessResponse
import kotlinx.coroutines.launch

class ChatBotViewModel(application: Application, val chatRepository: ChatRepository) :
    BaseViewModel(application) {

    fun sendMessage(text: String) {
        viewModelScope.launch {
            val result = chatRepository.sendMessage(SendMessageRequest("", message = text, ""))
            when (result) {
                is RepoSuccessResponse -> {

                }
                is RepoErrorResponse -> {

                }
                else -> {

                }
            }
        }
    }
}
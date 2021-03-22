package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.BuildConfig
import ai.mawdoo3.salma.data.dataModel.*
import ai.mawdoo3.salma.data.dataSource.ChatRepository
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.banking.common.base.BaseViewModel
import com.banking.core.remote.RepoErrorResponse
import com.banking.core.remote.RepoSuccessResponse
import kotlinx.coroutines.launch

class ChatBotViewModel(application: Application, val chatRepository: ChatRepository) :
    BaseViewModel(application) {

    val messageResponseList = MutableLiveData<MutableList<MessageUiModel>>()
    val showLoader = MutableLiveData<Boolean>()

    fun sendMessage(text: String) {
        showLoader.postValue(false)
        messageResponseList.value = ArrayList()
        messageResponseList.value?.add(TextMessageUiModel(text, MessageSender.User))
        viewModelScope.launch {
            showLoader.postValue(true)
            val result = chatRepository.sendMessage(
                SendMessageRequest(
                    "",
                    message = text,
                    BuildConfig.SECRET_KEY
                )
            )
            when (result) {
                is RepoSuccessResponse -> {
                    val responseMessages = ArrayList<MessageUiModel>()
                    val messagesResponse = result.body.messages
                    for (message in messagesResponse) {
                        message.Factory().create()?.let { responseMessages.add(it) }
                    }
                    messageResponseList.postValue(responseMessages)
                    showLoader.postValue(false)
                }
                is RepoErrorResponse -> {
                    val responseMessages = ArrayList<MessageUiModel>()
                    val items =
                        ArrayList<MessageResponse.MessageContentResponse.QuickReplyElement>()
                    items.add(
                        MessageResponse.MessageContentResponse.QuickReplyElement(
                            "ما هي آخر عمليات شراء قمت بها؟",
                            "ما هي آخر عمليات شراء قمت بها؟",
                            null
                        )
                    )
                    items.add(
                        MessageResponse.MessageContentResponse.QuickReplyElement(
                            "ما مزايا القرض السكني؟",
                            "ما مزايا القرض السكني؟",
                            null
                        )
                    )
                    items.add(
                        MessageResponse.MessageContentResponse.QuickReplyElement(
                            "أوقات عمل مركز خدمة العملاء",
                            "أوقات عمل مركز خدمة العملاء",
                            null
                        )
                    )
                    items.add(
                        MessageResponse.MessageContentResponse.QuickReplyElement(
                            "القروض",
                            "القروض",
                            null
                        )
                    )
                    items.add(
                        MessageResponse.MessageContentResponse.QuickReplyElement(
                            "البطاقات",
                            "البطاقات",
                            null
                        )
                    )
                    items.add(
                        MessageResponse.MessageContentResponse.QuickReplyElement(
                            "التأمين",
                            "التأمين",
                            null
                        )
                    )
                    val quickReplies = QuickReplyMessageUiModel(
                        "بعض ما يمكنني مساعدتك به",
                        items,
                        MessageSender.Masa
                    )
                    responseMessages.add(quickReplies)
                    messageResponseList.postValue(responseMessages)
                    showLoader.postValue(false)

//                    showErrorMessage.postValue("Something went wrong, please try again")
                }
                else -> {

                }
            }
        }
    }
}
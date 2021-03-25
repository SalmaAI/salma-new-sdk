package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.MasaSdkInstance
import ai.mawdoo3.salma.base.BaseViewModel
import ai.mawdoo3.salma.data.dataModel.*
import ai.mawdoo3.salma.data.dataSource.ChatRepository
import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.remote.RepoErrorResponse
import ai.mawdoo3.salma.remote.RepoSuccessResponse
import ai.mawdoo3.salma.utils.PhoneUtils
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.afollestad.assent.Permission
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch

class ChatBotViewModel(application: Application, val chatRepository: ChatRepository) :
    BaseViewModel(application) {

    val makeCall = LiveEvent<String>()
    val goToLocation = LiveEvent<String>()
    val messageResponseList = MutableLiveData<MutableList<MessageUiModel>>()
    val showLoader = MutableLiveData<Boolean>()
    val rateAnswer = LiveEvent<String>()
    val getUserLocation = LiveEvent<Boolean>()
    val ttsAudioList = LiveEvent<List<String>>()
    val requestPermission = LiveEvent<Permission>()

    fun sendMessage(text: String, showMessage: Boolean = true) {
        showLoader.postValue(false)
        messageResponseList.value = ArrayList()
        if (showMessage) {
            messageResponseList.value?.add(TextMessageUiModel(text, MessageSender.User))
        }
        viewModelScope.launch {
            showLoader.postValue(true)
            val result = chatRepository.sendMessage(
                SendMessageRequest(
                    PhoneUtils.getDeviceId(applicationContext),
                    message = text,
                    MasaSdkInstance.key
                )
            )

            when (result) {
                is RepoSuccessResponse -> {
                    val responseMessages = ArrayList<MessageUiModel>()
                    val locationMessages = ArrayList<LocationMessageUiModel>()
                    val messageAudiolist = ArrayList<String>()
                    val messagesResponse = result.body
                    Log.d("resultoo", messagesResponse.toString())
                    for (message in messagesResponse.messages) {
                        message.Factory().create()?.let {
                            if (!message.ttsId.isNullOrEmpty()) {
                                messageAudiolist.add(message.ttsId)
                            }
                            if (it is LocationMessageUiModel)
                                locationMessages.add(it)
                            else {
                                responseMessages.add(it)
                            }
                        }
                    }
                    if (locationMessages.isNotEmpty()) {
                        val locationsListUiModel = LocationsListUiModel(locationMessages)
                        responseMessages.add(locationsListUiModel)
                    }
                    messageResponseList.postValue(responseMessages)
                    if (messageAudiolist.size > 0) {
                        ttsAudioList.postValue(messageAudiolist)
                    }
                    showLoader.postValue(false)

                }
                is RepoErrorResponse -> {

                    Log.d("resultoo", result.error.toString())

//                    val responseMessages = ArrayList<MessageUiModel>()
//                    val items =
//                        ArrayList<MessageResponse.MessageContentResponse.QuickReplyElement>()
//                    items.add(
//                        MessageResponse.MessageContentResponse.QuickReplyElement(
//                            "ما هي آخر عمليات شراء قمت بها؟",
//                            "ما هي آخر عمليات شراء قمت بها؟",
//                            null
//                        )
//                    )
//                    items.add(
//                        MessageResponse.MessageContentResponse.QuickReplyElement(
//                            "ما مزايا القرض السكني؟",
//                            "ما مزايا القرض السكني؟",
//                            null
//                        )
//                    )
//                    items.add(
//                        MessageResponse.MessageContentResponse.QuickReplyElement(
//                            "أوقات عمل مركز خدمة العملاء",
//                            "أوقات عمل مركز خدمة العملاء",
//                            null
//                        )
//                    )
//                    items.add(
//                        MessageResponse.MessageContentResponse.QuickReplyElement(
//                            "القروض",
//                            "القروض",
//                            null
//                        )
//                    )
//                    items.add(
//                        MessageResponse.MessageContentResponse.QuickReplyElement(
//                            "البطاقات",
//                            "البطاقات",
//                            null
//                        )
//                    )
//                    items.add(
//                        MessageResponse.MessageContentResponse.QuickReplyElement(
//                            "التأمين",
//                            "التأمين",
//                            null
//                        )
//                    )
//                    val quickReplies = QuickReplyMessageUiModel(
//                        "بعض ما يمكنني مساعدتك به",
//                        items,
//                        MessageSender.Masa
//                    )
//                    val audioList = ArrayList<String>()
//                    audioList.add("20210323115331.2e76069c-0bb1-4171-9127-515dc196f759")
//                    audioList.add("20210323115332.88635623-b883-4d11-8054-65ac5354ea2c")
//                    audioList.add("20210323115332.1d3364f3-9f89-4da6-83d6-3842be25e2f4")
//                    audioList.add("20210323115333.75232e8b-a89e-49af-afc8-fa92660ed3f3")
//                    audioList.add("20210323115333.5c7ee0df-94ab-468f-83a3-43e3c7faa3a8")
//                    ttsAudioList.postValue(audioList)
//                    responseMessages.add(quickReplies)
//                    messageResponseList.postValue(responseMessages)
                    showLoader.postValue(false)
                    showErrorMessage.postValue("Something went wrong, please try again")
                }
                else -> {

                }
            }
        }
    }

}
package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.MasaSdkInstance
import ai.mawdoo3.salma.base.BaseViewModel
import ai.mawdoo3.salma.data.TtsItem
import ai.mawdoo3.salma.data.dataModel.*
import ai.mawdoo3.salma.data.dataSource.ChatRepository
import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.remote.RepoErrorResponse
import ai.mawdoo3.salma.remote.RepoSuccessResponse
import ai.mawdoo3.salma.utils.AppUtils
import ai.mawdoo3.salma.utils.PhoneUtils
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.afollestad.assent.Permission
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatBotViewModel(application: Application, val chatRepository: ChatRepository) :
    BaseViewModel(application) {

    val makeCall = LiveEvent<String>()
    val goToLocation = LiveEvent<String>()
    val messageResponseList = MutableLiveData<List<MessageUiModel>>()
    val messageSent = MutableLiveData<MessageUiModel>()
    val showLoader = MutableLiveData<Boolean>()
    val rateAnswer = LiveEvent<String>()
    val getUserLocation = LiveEvent<Boolean>()
    val openLink = LiveEvent<String>()
    val openNumberKeyPad = LiveEvent<Boolean>()
    val openDialUp = LiveEvent<String>()
    val ttsAudioList = LiveEvent<List<TtsItem>>()
    val stopTTS = LiveEvent<Boolean>()
    val requestPermission = LiveEvent<Permission>()

    /**
     * text -> this value will be shown to user as message (required when showMessage=true)
     * payload -> this value will be sent to server (won't show to user)
     * showMessage -> this boolean determine whether to show sent message in list or just send it to server without show it to user
     */
    fun sendMessage(text: String?, payload: String, showMessage: Boolean = true) {
        if (showMessage) {
            messageSent.value =
                TextMessageUiModel(
                    text, MessageSender.User,
                    time = AppUtils.getCurrentTime()
                )

        }
        stopTTS.value = true
        viewModelScope.launch {
            showLoader.value = true
            Log.d("SendMessage", "delay request 1000 millisecond")
            delay(1000)
            val result = chatRepository.sendMessage(
                SendMessageRequest(
                    PhoneUtils.getDeviceId(applicationContext),
                    message = payload,
                    MasaSdkInstance.key,
                    MasaSdkInstance.jwtToken
                )
            )

            when (result) {
                is RepoSuccessResponse -> {
                    Log.d("SendMessage", "Response success")
                    showLoader.postValue(false)

                    val responseMessages = ArrayList<MessageUiModel>()
                    val locationMessages = ArrayList<LocationMessageUiModel>()
                    val messageAudiolist = ArrayList<TtsItem>()
                    val messagesResponse = result.body
                    for (message in messagesResponse.messages) {
                        message.Factory().create()?.let {
                            if (!message.ttsId.isNullOrEmpty()) {
                                messageAudiolist.add(TtsItem(message.ttsId, message.ttsDynamic))
                            }
                            it.forEach { messageUiModel ->
                                //Aggregation all messages of LocationMessageUiModel in one list
                                if (messageUiModel is LocationMessageUiModel) {
                                    locationMessages.add(messageUiModel)
                                } else if (messageUiModel is DeeplinkMessageUiModel) {
                                    openLink.value = "http://www.masabanking.com/bills?action=add"
                                } else if (messageUiModel is KeyPadUiModel) {
                                    openNumberKeyPad.value = true
                                } else {
                                    responseMessages.add(messageUiModel)
                                }
                            }

                        }
                    }
                    if (locationMessages.isNotEmpty()) {//add locations messages to messages list
                        val locationsListUiModel = LocationsListUiModel(locationMessages)
                        responseMessages.add(locationsListUiModel)
                    }
                    //send response to fragment
                    messageResponseList.value = responseMessages
                    if (messageAudiolist.size > 0) {
                        ttsAudioList.value = messageAudiolist
                    }

                }
                is RepoErrorResponse -> {
                    Log.d("SendMessage", "Response error")
                    showLoader.value = false
                    onLoadFailure(result.error, true)
                }
                else -> {

                }
            }
        }
    }

}
package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.MasaSdkInstance
import ai.mawdoo3.salma.base.BaseViewModel
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
    val ttsAudioList = LiveEvent<List<String>>()
    val requestPermission = LiveEvent<Permission>()

    fun sendMessage(text: String, showMessage: Boolean = true) {
        if (showMessage) {
            messageSent.postValue(
                TextMessageUiModel(
                    text, MessageSender.User,
                    time = AppUtils.getCurrentTime()
                )
            )
        }
        viewModelScope.launch {
            showLoader.postValue(true)
            Log.d("SendMessage", "delay request 1000 millisecond")
            delay(1000)
            val result = chatRepository.sendMessage(
                SendMessageRequest(
                    PhoneUtils.getDeviceId(applicationContext),
                    message = text,
                    MasaSdkInstance.key
                )
            )

            when (result) {
                is RepoSuccessResponse -> {
                    Log.d("SendMessage", "Response success")
                    showLoader.postValue(false)

                    val responseMessages = ArrayList<MessageUiModel>()
                    val locationMessages = ArrayList<LocationMessageUiModel>()
                    val messageAudiolist = ArrayList<String>()
                    val messagesResponse = result.body
                    for (message in messagesResponse.messages) {
                        message.Factory().create()?.let {
                            if (!message.ttsId.isNullOrEmpty()) {
                                messageAudiolist.add(message.ttsId)
                            }
                            it.forEach { messageUiModel ->
                                //Aggregation all messages of LocationMessageUiModel in one list
                                if (messageUiModel is LocationMessageUiModel)
                                    locationMessages.add(messageUiModel)
                                else {
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
                    messageResponseList.postValue(responseMessages)
                    if (messageAudiolist.size > 0) {
//                        ttsAudioList.postValue(messageAudiolist)
                    }

                }
                is RepoErrorResponse -> {
                    Log.d("SendMessage", "Response error")
                    showLoader.postValue(false)
                    showErrorMessage.postValue("Something went wrong, please try again")
                }
                else -> {

                }
            }
        }
    }

}
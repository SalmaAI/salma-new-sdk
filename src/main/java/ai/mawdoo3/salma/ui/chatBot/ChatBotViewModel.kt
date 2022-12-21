package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.MasaSdkInstance
import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseViewModel
import ai.mawdoo3.salma.data.TtsItem
import ai.mawdoo3.salma.data.dataModel.*
import ai.mawdoo3.salma.data.dataSource.ChatRepository
import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.data.enums.MessageType
import ai.mawdoo3.salma.remote.RepoErrorResponse
import ai.mawdoo3.salma.remote.RepoSuccessResponse
import ai.mawdoo3.salma.utils.AppUtils
import ai.mawdoo3.salma.utils.PhoneUtils
import android.app.Application
import android.text.InputType
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.afollestad.assent.Permission
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class ChatBotViewModel(application: Application, val chatRepository: ChatRepository) :
    BaseViewModel(application) {

    var historyStartIndex = 0
    val makeCall = LiveEvent<String>()
    val goToLocation = LiveEvent<String>()
    val historyList = ArrayList<MessageUiModel>()
    val messageResponseList = LiveEvent<List<MessageUiModel>>()
    val messageSent = LiveEvent<MessageUiModel>()
    val showLoader = MutableLiveData<Boolean>()
    val rateAnswer = LiveEvent<String>()
    val getUserLocation = LiveEvent<Boolean>()
    val openLink = LiveEvent<String>()
    val openNumberKeyPad = LiveEvent<Boolean>()
    val openTextKeyPad = LiveEvent<Boolean>()
    val openDialUp = LiveEvent<String>()
    val ttsAudioList = LiveEvent<List<TtsItem>>()
    val stopTTS = LiveEvent<Boolean>()
    val requestPermission = LiveEvent<Permission>()
    var historyApiKey: String = ""
    var asrEnabled: Boolean = true
    var asrDisabledMessage: String = ""

    /**
     * text -> this value will be shown to user as message (required when showMessage=true)
     * payload -> this value will be sent to server (won't show to user)
     * showMessage -> this boolean determine whether to show sent message in list or just send it to server without show it to user
     */
    fun sendMessage(
        text: String?,
        payload: String,
        showMessage: Boolean = true,
        newSession: Boolean = false
    ) {
        historyStartIndex = 0
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
                    userId = PhoneUtils.getDeviceId(applicationContext),
                    message = payload,
                    secretKey = MasaSdkInstance.key,
                    JWT = MasaSdkInstance.jwtToken,
                    newSession = newSession
                )
            )

            when (result) {
                is RepoSuccessResponse -> {
                    Log.d("SendMessage", "Response success")
                    showLoader.postValue(false)

                    val responseMessages = ArrayList<MessageUiModel>()
                    val locationMessages = ArrayList<LocationMessageUiModel>()
                    val cardsMessages = ArrayList<CardUiModel>()
                    val messageAudiolist = ArrayList<TtsItem>()
                    val messagesResponse = result.body
                    var locationsPos = 0
                    historyApiKey = result.body.historyApiKey
                    asrEnabled = result.body.asrEnabled
                    asrDisabledMessage = result.body.asrDisabledMessage

                    for (message in messagesResponse.messages) {

                        if (message.type == MessageType.TextLocation.value && locationsPos == 0) {
                            locationsPos = messagesResponse.messages.indexOf(message)
                        }

                        message.Factory().create().let {
                            if (!message.ttsId.isNullOrEmpty()) {
                                messageAudiolist.add(
                                    TtsItem(
                                        message.ttsId,
                                        message.ttsDynamic,
                                        message.ttsText
                                    )
                                )
                            }
                            it.forEach { messageUiModel ->
                                //Aggregation all messages of LocationMessageUiModel in one list
                                when (messageUiModel) {
                                    is LocationMessageUiModel -> {
                                        locationMessages.add(messageUiModel)
                                    }
                                    is CardUiModel -> {
                                        cardsMessages.add(messageUiModel)
                                    }
                                    is DeeplinkMessageUiModel -> {
                                        openLink.value = messageUiModel.url
                                    }
                                    is KeyPadUiModel -> {
                                        if (messageUiModel.inputType == InputType.TYPE_CLASS_NUMBER) {
                                            openNumberKeyPad.value = true
                                        } else if (messageUiModel.inputType == InputType.TYPE_CLASS_TEXT) {
                                            openTextKeyPad.value = true
                                        }
                                    }
                                    else -> {
                                        responseMessages.add(messageUiModel)
                                    }
                                }
                            }

                        }
                    }
                    if (locationMessages.isNotEmpty()) {//add locations messages to messages list
                        val locationsListUiModel = LocationsListUiModel(locationMessages)
                        if (responseMessages.size > locationsPos) {
                            responseMessages.add(locationsPos, locationsListUiModel)
                        } else {
                            responseMessages.add(locationsListUiModel)
                        }
                    }
                    if (cardsMessages.isNotEmpty()) {//add cards list messages to messages list
                        val cardsListUiModel = CardsListMessageUiModel(cardsMessages)
                        responseMessages.add(cardsListUiModel)
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
                    if (result.error is IOException) {
                        val responseMessages = ArrayList<MessageUiModel>()
                        responseMessages.add(
                            TextMessageUiModel(
                                applicationContext.getString(R.string.check_internet_connection),
                                MessageSender.Masa,
                                time = AppUtils.getCurrentTime()
                            )
                        )
                        messageResponseList.value = responseMessages


                    } else {
                        onLoadFailure(result.error, true)
                    }
                }
                else -> {

                }
            }
        }
    }


//    fun getMessagesHistory(
//    ) {
//        viewModelScope.launch {
//            showLoader.value = true
//            val result = chatRepository.getHistory(
//                MessagesHistoryRequest(
//                    secretKey = MasaSdkInstance.key,
//                    historyApiKey = historyApiKey,
//                    start = historyStartIndex,
//                    size = 10
//                ), PhoneUtils.getDeviceId(applicationContext)
//            )
//
//            when (result) {
//                is RepoSuccessResponse -> {
//                    Log.d("SendMessage", "Response success")
//                    showLoader.postValue(false)
//                    val isFirstPage = historyStartIndex == 0
//                    val responseMessages = ArrayList<MessageUiModel>()
//                    val historyResponse = result.body
//                    historyStartIndex += historyResponse.size
//                    for (historyItem in historyResponse) {
//                        if (isFirstPage && historyItem == historyResponse[historyResponse.lastIndex])
//                        {
//                            continue
//                        }
//                        val locationMessages = ArrayList<LocationMessageUiModel>()
//                        val cardsMessages = ArrayList<CardUiModel>()
//                        responseMessages.add(
//                            TextMessageUiModel(
//                                historyItem.userRequest.value,
//                                MessageSender.User,
//                                time = historyItem.requestDate
//                            )
//                        )
//                        for (message in historyItem.botResponses) {
//
//                            message.Factory().create().let {
//
//                                it.forEach { messageUiModel ->
//                                    //Aggregation all messages of LocationMessageUiModel in one list
//                                    when (messageUiModel) {
//                                        is LocationMessageUiModel -> {
//                                            locationMessages.add(messageUiModel)
//                                        }
//                                        is CardUiModel -> {
//                                            cardsMessages.add(messageUiModel)
//                                        }
//                                        is QuickReplyMessageUiModel -> {
//                                            messageUiModel.isHistory = true
//                                            responseMessages.add(messageUiModel)
//                                        }
//                                        is BillsMessageUiModel -> {
//                                            messageUiModel.isHistory = true
//                                            responseMessages.add(messageUiModel)
//                                        }
//                                        is InformationalMessageUiModel -> {
//                                            messageUiModel.isHistory = true
//                                            responseMessages.add(messageUiModel)
//                                        }
//                                        else -> {
//                                            responseMessages.add(messageUiModel)
//                                        }
//                                    }
//                                }
//                            }
//
//                        }
//                        if (locationMessages.isNotEmpty()) {//add locations messages to messages list
//                            val locationsListUiModel = LocationsListUiModel(locationMessages)
//                            responseMessages.add(locationsListUiModel)
//                        }
//                        if (cardsMessages.isNotEmpty()) {//add cards list messages to messages list
//                            val cardsListUiModel = CardsListMessageUiModel(cardsMessages)
//                            responseMessages.add(cardsListUiModel)
//                        }
//                    }
//
//                    //send response to fragment
//                    historyResponseList.value = responseMessages
//                }
//                is RepoErrorResponse -> {
//                    Log.d("SendMessage", "Response error")
//                    showLoader.value = false
//                    onLoadFailure(result.error, true)
//                }
//                else -> {
//
//                }
//            }
//        }
//    }

}
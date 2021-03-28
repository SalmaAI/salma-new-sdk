package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender

/**
 * created by Omar Qadomi on 3/17/21
 */
data class TextMessageUiModel(val text: String?, val messageSender: MessageSender) :
    MessageUiModel {
    override var sender = messageSender

}
package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender

/**
 * created by Omar Qadomi on 16/9/21
 */
data class InformationalMessageUiModel(
    val title: String?,
    val subTitle: String?,
    val optionalInfo: String?,
    val globalButton: ButtonUiModel?,
    val buttons: List<ButtonUiModel>?,
    val messageSender: MessageSender
) :
    MessageUiModel {
    override var sender = messageSender

}
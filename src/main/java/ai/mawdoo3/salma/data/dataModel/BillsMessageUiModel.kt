package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender

/**
 * created by Omar Qadomi on 3/17/21
 */
data class BillsMessageUiModel(
    val title: String?,
    val date: String?,
    val image: String?,
    val amount: String?,
    val buttons: List<ButtonUiModel>?,
    val messageSender: MessageSender
) :
    MessageUiModel {
    override var sender = messageSender
    var isHistory: Boolean = false

}
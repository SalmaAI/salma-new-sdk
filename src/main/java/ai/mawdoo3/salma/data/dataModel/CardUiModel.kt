package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender

/**
 * created by Omar Qadomi on 27/12/21
 */
data class CardUiModel(
    val cardId: String?,
    val name: String?,
    val cardNumber: String?,
    val holderName: String?,
    val expiryDate: String?,
    val image: Int?,
    val messageSender: MessageSender
) :
    MessageUiModel {
    override var sender = messageSender

}
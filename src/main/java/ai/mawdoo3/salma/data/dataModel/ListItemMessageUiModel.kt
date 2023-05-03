package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender

/**
 * created by Omar Qadomi on 21/12/21
 */
data class ListItemMessageUiModel(
    val title: String?,
    val subTitle: String?,
    val optionalInfo: String?,
    val payload: String?,
) :
    MessageUiModel {
    override var sender = MessageSender.Masa

}
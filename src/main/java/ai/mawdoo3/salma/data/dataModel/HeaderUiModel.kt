package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender

/**
 * created by Omar Qadomi on 3/17/21
 */
data class HeaderUiModel(
    override var sender: MessageSender = MessageSender.Masa,
    val name: String?
) :
    MessageUiModel {

}
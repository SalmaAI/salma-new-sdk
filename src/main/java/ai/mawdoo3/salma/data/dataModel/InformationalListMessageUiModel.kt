package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender

/**
 * created by Omar Qadomi on 13/6/22
 */
data class InformationalListMessageUiModel(
    val items: List<InformationalMessageUiModel>,
    val messageSender: MessageSender
    ) :
    MessageUiModel {
    override var sender = messageSender
}
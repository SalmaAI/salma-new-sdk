package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender

/**
 * created by Omar Qadomi on 3/17/21
 */
data class DropdownListUiModel(
    val title: String,
    val items: List<DropdownListItem>
) :
    MessageUiModel {
    override var sender = MessageSender.Masa

}
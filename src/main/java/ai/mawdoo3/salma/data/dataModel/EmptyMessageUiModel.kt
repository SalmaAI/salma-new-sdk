package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender

/**
 * created by Omar Qadomi on 25/6/23
 */
data class EmptyMessageUiModel(val id: Int = 1, val isLoading: Boolean = false) :
    MessageUiModel {
    override var sender = MessageSender.Masa

    override fun equals(other: Any?): Boolean {
        if (other !is EmptyMessageUiModel)
            return false
        return id == other.id
    }

}
package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender
import android.text.InputType

/**
 * created by Omar Qadomi on 3/17/21
 */
data class KeyPadUiModel(override var sender: MessageSender = MessageSender.Masa, val inputType: Int) :
    MessageUiModel {

}
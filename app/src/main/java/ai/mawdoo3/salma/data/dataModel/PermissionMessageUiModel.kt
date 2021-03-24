package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender
import com.afollestad.assent.Permission

/**
 * created by Omar Qadomi on 3/17/21
 */
data class PermissionMessageUiModel(
    val text: String?,
    val buttonTitle: String?,
    val permission: Permission,
) :
    MessageUiModel {
    override var sender = MessageSender.Masa

}
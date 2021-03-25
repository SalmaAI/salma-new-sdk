package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.data.enums.MessageType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendMessageResponse(
    @Json(name = "date") val date: String,
    @Json(name = "requestText") val requestText: String,
    @Json(name = "messages") val messages: List<MessageResponse>
)

@JsonClass(generateAdapter = true)
data class MessageResponse(
    @Json(name = "messageType") val type: String,
    @Json(name = "ttsId") val ttsId: String?,
    @Json(name = "content") val messageContent: MessageContentResponse
) {
    @JsonClass(generateAdapter = true)
    data class MessageContentResponse(
        @Json(name = "text") val text: String?,
        @Json(name = "elements") val quickReplyElements: List<QuickReplyElement>?,
        @Json(name = "attachmentId") val attachmentId: String?
    ) {
        data class QuickReplyElement(
            @Json(name = "title") val title: String,
            @Json(name = "quickReplyPayload") val quickReplyPayload: String,
            @Json(name = "quickReplyType") val quickReplyType: String?
        )
    }

    inner class Factory {
        fun create(): MessageUiModel? {
            val messageType = MessageType.from(type)
            return if (messageType == MessageType.Text || messageType == MessageType.UnansweredText) {
                TextMessageUiModel(messageContent.text, MessageSender.Masa)
            } else if (messageType == MessageType.QuickReply || messageType == MessageType.UnansweredQuickReply) {
                QuickReplyMessageUiModel(
                    messageContent.text,
                    messageContent.quickReplyElements, MessageSender.Masa
                )
            } else if (messageType == MessageType.TextLocation || messageType == MessageType.UnansweredTextLocation) {
                val text = messageContent.text?.replace("الاحداثيات :", "")
                val data = text?.split('،')
                val name = data?.get(0)?.replace("الفرع :", "")
                val address = data?.get(1)?.replace("العنوان :", "")
                val phone = data?.get(2)?.replace("هاتف :", "")
                val workingHours = data?.get(3)?.replace("ساعات العمل :", "")
                LocationMessageUiModel(
                    name = name,
                    address = address,
                    workingHours = workingHours,
                    geoFence = messageContent.attachmentId,
                    phone = phone,
                    messageSender = MessageSender.Masa
                )
            } else {
                null
            }
        }
    }
}



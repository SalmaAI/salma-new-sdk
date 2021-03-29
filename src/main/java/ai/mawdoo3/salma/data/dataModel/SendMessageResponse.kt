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
                var name: String? = null
                var address: String? = null
                var workingHours: String? = null
                var phone: String? = null
                var locationType: String? = null
                if (!data.isNullOrEmpty()) {
                    name = data[0].trim()
                    locationType = if (name.contains("الفرع")) {
                        "branch"
                    } else {
                        "ATM"
                    }
                    name = name.replace("الفرع :", "").replace("الصراف الالي :", "")
                        .replace("الصراف الالي التفاعلي :", "").trim()
                }
                if (!data.isNullOrEmpty() && data.size > 1) {
                    address = data[1].replace("العنوان :", "").trim()
                }
                if (!data.isNullOrEmpty() && data.size > 2) {
                    phone = data[2].replace("هاتف :", "").trim()
                }
                if (!data.isNullOrEmpty() && data.size > 3) {
                    workingHours = data[3].replace("ساعات العمل :", "").trim()
                }
                LocationMessageUiModel(
                    name = name,
                    address = address,
                    workingHours = workingHours,
                    geoFence = messageContent.attachmentId,
                    phone = phone,
                    type = locationType,
                    messageSender = MessageSender.Masa
                )
            } else {
                null
            }
        }
    }
}



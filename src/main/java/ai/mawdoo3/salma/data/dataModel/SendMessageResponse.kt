package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.data.enums.MessageType
import ai.mawdoo3.salma.utils.AppUtils
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
        @Json(name = "url") val url: String?,
        @Json(name = "elements") val elements: List<Element>?,
        @Json(name = "attachmentId") val attachmentId: String?
    ) {
        data class Element(
            @Json(name = "title") val title: String?,
            @Json(name = "image") val image: String?,
            @Json(name = "subTitle") val subTitle: String?,
            @Json(name = "buttons") val buttons: List<ActionButton>?,
            @Json(name = "quickReplyPayload") val quickReplyPayload: String?,
            @Json(name = "quickReplyType") val quickReplyType: String?
        ) {
            data class ActionButton(
                @Json(name = "type") val type: String,
                @Json(name = "title") val title: String,
                @Json(name = "value") val value: String
            )
        }
    }

    inner class Factory {
        fun create(): List<MessageUiModel>? {
            val messageType = MessageType.from(type)
            val messages = ArrayList<MessageUiModel>()
            if (messageType == MessageType.Text || messageType == MessageType.UnansweredText) {
                messages.add(
                    TextMessageUiModel(
                        messageContent.text, MessageSender.Masa,
                        time = AppUtils.getCurrentTime()
                    )
                )
            } else if (messageType == MessageType.QuickReply || messageType == MessageType.UnansweredQuickReply) {
                messages.add(
                    QuickReplyMessageUiModel(
                        messageContent.text,
                        messageContent.elements, MessageSender.Masa
                    )
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
                messages.add(
                    LocationMessageUiModel(
                        name = name,
                        address = address,
                        workingHours = workingHours,
                        geoFence = messageContent.attachmentId,
                        phone = phone,
                        type = locationType,
                        messageSender = MessageSender.Masa
                    )
                )
            } else if (messageType == MessageType.Carousel || messageType == MessageType.UnansweredCarousel) {
                //if there is text for content add text message before cards
                messageContent.text?.let {
                    messages.add(
                        TextMessageUiModel(
                            messageContent.text,
                            MessageSender.Masa,
                            time = AppUtils.getCurrentTime()
                        )
                    )
                }
                //add bills cards UI model to messages list
                messageContent.elements?.forEach { element ->
                    val buttons = ArrayList<ButtonUiModel>()
                    element.buttons?.forEach {
                        buttons.add(ButtonUiModel(it.type, it.title, it.value))
                    }
                    val date = element.subTitle?.split("\\n\\n")?.get(0)
                    val amount = element.subTitle?.split("\\n\\n")?.get(1)
                    messages.add(
                        BillsMessageUiModel(
                            title = element.title,
                            image = element.image,
                            date = date,
                            amount = amount,
                            buttons = buttons,
                            messageSender = MessageSender.Masa
                        )
                    )
                }

            } else if (messageType == MessageType.Image || messageType == MessageType.UnansweredImage) {
                messages.add(ImageMessageUiModel(url = messageContent.url, MessageSender.Masa))
            } else if (messageType == MessageType.DeepLink || messageType == MessageType.UnansweredDeepLink) {
                messageContent.url?.let {
                    messages.add(
                        DeeplinkMessageUiModel(
                            url = messageContent.url
                        )
                    )
                }
            }

            return messages
        }
    }
}



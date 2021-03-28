package ai.mawdoo3.salma.data.enums

enum class MessageType(val value: String) {
    Text("TEXT"),
    UnansweredText("UNANSWERED_TEXT"),
    TextLocation("TEXT_LOCATION"),
    UnansweredTextLocation("UNANSWERED_TEXT_LOCATION"),
    QuickReply("QUICK_REPLY"),
    UnansweredQuickReply("UNANSWERED_QUICK_REPLY");

    companion object {
        fun from(type: String?): MessageType = values().find { it.value == type } ?: Text
    }

}

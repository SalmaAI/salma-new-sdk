package ai.mawdoo3.salma.data.enums

enum class MessageType(val value: String) {
    Text("TEXT"),
    UnansweredText("UNANSWERED_TEXT"),
    Location("LOCATION"),
    UnansweredLocation("UNANSWERED_LOCATION"),
    TextLocation("TEXT_LOCATION"),
    UnansweredTextLocation("UNANSWERED_TEXT_LOCATION"),
    QuickReply("QUICK_REPLY"),
    UnansweredQuickReply("UNANSWERED_QUICK_REPLY"),
    UnansweredDeepLink("UNANSWERED_DEEP_LINK"),
    DeepLink("DEEP_LINK"),
    Image("IMAGE"),
    UnansweredImage("UNANSWERED_IMAGE"),
    Carousel("CAROUSEL"),
    UnansweredCarousel("UNANSWERED_CAROUSEL"),
    NumberKeyPad("NUMBER_KEY_PAD"),
    UnansweredNumberKeyPad("UNANSWERED_NUMBER_KEY_PAD"),
    TextKeyPad("TEXT_KEY_PAD"),
    UnansweredTextKeyPad("UNANSWERED_TEXT_KEY_PAD"),
    InformationalCard("INFORMATIONAL_CARD"),
    UnansweredInformationalCard("UNANSWERED_INFORMATIONAL_CARD"),
    PropertiesCard("PROPERTIES_CARD"),
    UnansweredPropertiesCard("UNANSWERED_PROPERTIES_CARD"),
    KeyValueList("KEY_VALUE_LIST"),
    UnansweredKeyValueList("UNANSWERED_KEY_VALUE_LIST"),
    CardsList("CARDS_LIST"),
    UnansweredCardsList("UNANSWERED_CARDS_LIST");

    companion object {
        fun from(type: String?): MessageType = values().find { it.value == type } ?: Text
    }

}

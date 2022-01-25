package ai.mawdoo3.salma.data.enums

enum class ButtonType(val value: String) {
    WebUrl("web_url"),
    PostBack("postback"),
    PhoneNumber("phone_number"),
    PerformFunction("perform_function");

    companion object {
        fun from(type: String?): ButtonType = values().find { it.value == type } ?: WebUrl
    }

}

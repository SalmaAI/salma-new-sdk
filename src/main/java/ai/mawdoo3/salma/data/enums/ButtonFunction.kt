package ai.mawdoo3.salma.data.enums

enum class ButtonFunction(val value: String) {
    Copy("copy"),
    Share("share"),
    CopyAndShare("copy_share"),
    Deeplink("deep_link");

    companion object {
        fun from(type: String?): ButtonFunction = values().find { it.value == type } ?: Share
    }

}

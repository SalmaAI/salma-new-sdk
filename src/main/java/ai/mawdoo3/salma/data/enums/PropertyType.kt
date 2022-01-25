package ai.mawdoo3.salma.data.enums

enum class PropertyType(val value: String) {
    CurrencyFrom("CURRENCY_FROM"),
    CurrencyTo("CURRENCY_TO"),
    CurrencyFromValue("CURRENCY_FROM_VALUE"),
    CurrencyToValue("CURRENCY_TO_VALUE"),
    CurrencyExchangeRate("CURRENCY_EXCHANGE_RATE_VALUE");

    companion object {
        fun from(type: String?): PropertyType? = values().find { it.value == type } ?: null
    }

}

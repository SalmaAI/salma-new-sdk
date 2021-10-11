package ai.mawdoo3.salma.data.dataModel

import ai.mawdoo3.salma.data.enums.MessageSender

/**
 * created by Omar Qadomi on 3/17/21
 */
data class CurrencyMessageUiModel(
    val messageSender: MessageSender
) :
    MessageUiModel {
    override var sender = messageSender
    var fromCurrency: Currency? = null
    var toCurrency: Currency? = null
    lateinit var fromValue: String
    lateinit var toValue: String
    lateinit var exchangeRate: String

}

data class Currency(val code: String, val description: String, val flag: String)

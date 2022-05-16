package ai.mawdoo3.salma.data.dataModel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * created by Omar Qadomi on 3/17/21
 */
@JsonClass(generateAdapter = true)
data class MessagesHistoryRequest(
    @Json(name = "secretKey")
    var secretKey: String,
    @Json(name = "start")
    var start: Int,
    @Json(name = "size")
    var size: Int,
    @Json(name = "historyApiKey")
    var historyApiKey: String
)
package ai.mawdoo3.salma.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * created by Omar Qadomi on 3/17/21
 */
@JsonClass(generateAdapter = true)
data class SendMessageRequest(
    @Json(name = "userId")
    var userId: String,
    @Json(name = "message")
    var message: String,
    @Json(name = "secretKey")
    var secretKey: String
)
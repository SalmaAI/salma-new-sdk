package ai.mawdoo3.salma.data.dataModel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendMessageResponse(
    @Json(name = "date") val date: String,
    @Json(name = "ttsId") val ttsId: String,
    @Json(name = "requestText") val requestText: String
)

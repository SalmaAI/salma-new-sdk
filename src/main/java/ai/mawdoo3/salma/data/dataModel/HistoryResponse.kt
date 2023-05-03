package ai.mawdoo3.salma.data.dataModel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HistoryResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "userId")
    val userId: String,
    @Json(name = "date")
    val date: String,
    @Json(name = "requestDate")
    val requestDate: String,
    @Json(name = "userRequest")
    val userRequest: UserRequest,
    @Json(name = "botResponses")
    val botResponses: List<MessageResponse>
)

@JsonClass(generateAdapter = true)
data class UserRequest(
    @Json(name = "value") val value: String,
    @Json(name = "messageType") val messageType: String,
    @Json(name = "messageId") val messageId: String
)

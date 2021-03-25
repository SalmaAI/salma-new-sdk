package ai.mawdoo3.salma.remote


import ai.mawdoo3.salma.data.dataModel.SendMessageRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageResponse
import com.google.gson.JsonObject
import com.squareup.okhttp.RequestBody
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface ApiEndpoints {

    @POST("{botId}/{botChannelId}/message")
    suspend fun sendMessage(
        @Body sendMessageRequest: SendMessageRequest,
        @Path("botId") botId: String,
        @Path("botChannelId") botChannelId: String
    ): Response<SendMessageResponse>

    @Multipart
    @POST("{botId}/{botChannelId}/message")
    suspend fun sendMessageNew(
        @Path("botId") botId: String,
        @Path("botChannelId") botChannelId: String,
        @Part userId: MultipartBody.Part,
        @Part message: MultipartBody.Part,
        @Part secretKey: MultipartBody.Part
        ): Response<SendMessageResponse>

}
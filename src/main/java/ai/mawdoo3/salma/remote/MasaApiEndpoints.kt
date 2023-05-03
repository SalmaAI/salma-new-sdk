package ai.mawdoo3.salma.remote


import ai.mawdoo3.salma.data.dataModel.HistoryResponse
import ai.mawdoo3.salma.data.dataModel.MessagesHistoryRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MasaApiEndpoints {

    @Multipart
    @POST("{botId}/{botChannelId}/message")
    suspend fun sendMessage(
        @Path("botId") botId: String,
        @Path("botChannelId") botChannelId: String,
        @Part userId: MultipartBody.Part,
        @Part message: MultipartBody.Part,
        @Part secretKey: MultipartBody.Part,
        @Part mobileJWT: MultipartBody.Part,
        @Part("newSession") newSession: Boolean,
    ): Response<SendMessageResponse>

    @POST("{botId}/{botChannelId}/history/{userId}")
    suspend fun getHistory(
        @Path("botId") botId: String,
        @Path("botChannelId") botChannelId: String,
        @Path("userId") userId: String,
        @Body body: MessagesHistoryRequest,
        @Header("Content-Type") contentType: String = "application/json"
    ): Response<List<HistoryResponse>>

}
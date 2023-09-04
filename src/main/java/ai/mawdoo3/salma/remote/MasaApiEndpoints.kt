package ai.mawdoo3.salma.remote


import ai.mawdoo3.salma.data.dataModel.HistoryResponse
import ai.mawdoo3.salma.data.dataModel.MessagesHistoryRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageResponse
import ai.mawdoo3.salma.utils.security.FulfilmentEncryptedRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MasaApiEndpoints {

    @POST("{botId}/{botChannelId}/message")
    suspend fun sendMessage(
        @Path("botId") botId: String,
        @Path("botChannelId") botChannelId: String,
        @Body body: FulfilmentEncryptedRequest,
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
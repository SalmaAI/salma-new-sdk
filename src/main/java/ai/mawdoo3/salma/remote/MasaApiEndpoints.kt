package ai.mawdoo3.salma.remote


import ai.mawdoo3.salma.data.dataModel.SendMessageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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
        @Part("newSession") newSession: Boolean
    ): Response<SendMessageResponse>

}
package ai.mawdoo3.salma.data.dataSource

import ai.mawdoo3.salma.MasaSdkInstance
import ai.mawdoo3.salma.data.dataModel.SendMessageRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageResponse
import ai.mawdoo3.salma.remote.MasaApiEndpoints
import ai.mawdoo3.salma.remote.RepoResponse
import okhttp3.MultipartBody

/**
 * created by Omar Qadomi on 3/18/21
 */
class ChatRemoteDataSource(private val endpoints: MasaApiEndpoints) {

    suspend fun sendMessage(sendMessageRequest: SendMessageRequest): RepoResponse<SendMessageResponse>? {
        return try {

            val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("secretKey", sendMessageRequest.secretKey)
                .addFormDataPart("userId", sendMessageRequest.userId)
                .addFormDataPart("message", sendMessageRequest.message)
                .addFormDataPart("mobileJWT", sendMessageRequest.JWT)
                .build()

            val result = endpoints.sendMessage(
                MasaSdkInstance.botId,
                MasaSdkInstance.botChannelId,
                body.part(0),
                body.part(1),
                body.part(2),
                body.part(3)
            )
            RepoResponse.create(result)
        } catch (e: Exception) {
            RepoResponse.create(e)
        }
    }

}
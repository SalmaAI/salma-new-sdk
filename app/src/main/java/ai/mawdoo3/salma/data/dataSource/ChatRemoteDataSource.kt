package ai.mawdoo3.salma.data.dataSource

import ai.mawdoo3.salma.MasaSDK
import ai.mawdoo3.salma.data.dataModel.SendMessageRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageResponse
import ai.mawdoo3.salma.remote.ApiEndpoints
import ai.mawdoo3.salma.remote.RepoResponse

/**
 * created by Omar Qadomi on 3/18/21
 */
class ChatRemoteDataSource(private val endpoints: ApiEndpoints) {

    suspend fun sendMessage(sendMessageRequest: SendMessageRequest): RepoResponse<SendMessageResponse>? {
        return try {
            val result = endpoints.sendMessage(
                sendMessageRequest,
                MasaSDK.SdkInstance.botId,
                MasaSDK.SdkInstance.botChannelId
            )
            RepoResponse.create(result)
        } catch (e: Exception) {
            RepoResponse.create(e)
        }
    }

}
package ai.mawdoo3.salma.data.dataSource

import ai.mawdoo3.salma.MasaSdkInstance
import ai.mawdoo3.salma.data.dataModel.SendMessageRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageResponse
import ai.mawdoo3.salma.remote.MasaApiEndpoints
import ai.mawdoo3.salma.remote.RepoResponse

/**
 * created by Omar Qadomi on 3/18/21
 */
class ChatRemoteDataSource(private val endpointsMasa: MasaApiEndpoints) {

    suspend fun sendMessage(sendMessageRequest: SendMessageRequest): RepoResponse<SendMessageResponse>? {
        return try {
            val result = endpointsMasa.sendMessage(
                sendMessageRequest,
                MasaSdkInstance.botId,
                MasaSdkInstance.botChannelId
            )
            RepoResponse.create(result)
        } catch (e: Exception) {
            RepoResponse.create(e)
        }
    }

}
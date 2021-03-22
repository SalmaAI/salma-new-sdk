package ai.mawdoo3.salma.data.dataSource

import ai.mawdoo3.salma.data.dataModel.SendMessageRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageResponse
import com.banking.core.remote.ApiEndpoints
import com.banking.core.remote.RepoResponse

/**
 * created by Omar Qadomi on 3/18/21
 */
class ChatRemoteDataSource(private val endpoints: ApiEndpoints) {

    suspend fun sendMessage(sendMessageRequest: SendMessageRequest): RepoResponse<SendMessageResponse>? {
        return try {
            val result = endpoints.sendMessage(sendMessageRequest, "1", "500")
            RepoResponse.create(result)
        } catch (e: Exception) {
            RepoResponse.create(e)
        }
    }

}
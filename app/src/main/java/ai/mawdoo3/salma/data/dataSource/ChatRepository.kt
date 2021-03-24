package ai.mawdoo3.salma.data.dataSource

import ai.mawdoo3.salma.data.dataModel.SendMessageRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageResponse
import ai.mawdoo3.salma.remote.RepoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository(
    private val chatRemoteDataSource: ChatRemoteDataSource,
) {

    suspend fun sendMessage(sendMessageRequest: SendMessageRequest): RepoResponse<SendMessageResponse>? {
        return withContext(Dispatchers.IO)
        {
            val result = chatRemoteDataSource.sendMessage(sendMessageRequest)
            return@withContext result
        }
    }


}
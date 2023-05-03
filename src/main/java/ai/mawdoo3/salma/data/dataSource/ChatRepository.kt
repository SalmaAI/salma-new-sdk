package ai.mawdoo3.salma.data.dataSource

import ai.mawdoo3.salma.data.dataModel.HistoryResponse
import ai.mawdoo3.salma.data.dataModel.MessagesHistoryRequest
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
            return@withContext chatRemoteDataSource.sendMessage(sendMessageRequest)
        }
    }

    suspend fun getHistory(
        historyRequest: MessagesHistoryRequest,
        userId: String
    ): RepoResponse<List<HistoryResponse>>? {
        return withContext(Dispatchers.IO)
        {
            return@withContext chatRemoteDataSource.getHistory(historyRequest, userId)
        }
    }


}
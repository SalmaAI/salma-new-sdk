package ai.mawdoo3.salma.data.dataSource

import ai.mawdoo3.salma.BuildConfig
import ai.mawdoo3.salma.data.dataModel.SendMessageRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageResponse
import ai.mawdoo3.salma.remote.ApiEndpoints
import ai.mawdoo3.salma.remote.RepoResponse
import com.squareup.okhttp.MediaType
import okhttp3.MultipartBody

/**
 * created by Omar Qadomi on 3/18/21
 */
class ChatRemoteDataSource(private val endpoints: ApiEndpoints) {

    suspend fun sendMessage(sendMessageRequest: SendMessageRequest): RepoResponse<SendMessageResponse>? {
        return try {

            val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("secretKey", BuildConfig.SECRET_KEY)
                    .addFormDataPart("userId", sendMessageRequest.userId)
                    .addFormDataPart("message", sendMessageRequest.message)
                    .build()

            val result = endpoints.sendMessageNew(
                    "1",
                    "LlfjJwVrMPJKeA",
                    body.part(0),
                    body.part(1),
                    body.part(2)
            )
            RepoResponse.create(result)
        } catch (e: Exception) {
            RepoResponse.create(e)
        }
    }

}
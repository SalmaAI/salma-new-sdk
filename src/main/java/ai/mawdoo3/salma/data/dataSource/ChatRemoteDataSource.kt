package ai.mawdoo3.salma.data.dataSource

import ai.mawdoo3.salma.SalmaSdkInstance
import ai.mawdoo3.salma.data.dataModel.HistoryResponse
import ai.mawdoo3.salma.data.dataModel.MessagesHistoryRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageRequest
import ai.mawdoo3.salma.data.dataModel.SendMessageResponse
import ai.mawdoo3.salma.remote.MasaApiEndpoints
import ai.mawdoo3.salma.remote.RepoResponse
import ai.mawdoo3.salma.utils.security.FulfilmentEncryptedRequest
import ai.mawdoo3.salma.utils.security.RSASecurityWithSymmetric
import android.content.Context
import android.util.Log
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException

/**
 * created by Omar Qadomi on 3/18/21
 */
class ChatRemoteDataSource(private val endpoints: MasaApiEndpoints,context: Context) {
    private var security: RSASecurityWithSymmetric = RSASecurityWithSymmetric(context)
    suspend fun sendMessage(sendMessageRequest: SendMessageRequest): RepoResponse<SendMessageResponse>? {
        return try {

            val request = "clientSecretKey." + sendMessageRequest.secretKey + ".userUniqueKey." + sendMessageRequest.userId + ".startNewSession." + sendMessageRequest.newSession + ".messagePayload." + sendMessageRequest.message
            val encryptedRequest : FulfilmentEncryptedRequest? = encryptRequestBody(request)

            encryptedRequest?.let {
                val result = endpoints.sendMessage(
                    botId = SalmaSdkInstance.botId,
                    botChannelId = SalmaSdkInstance.botChannelId,
                    it
                )
                RepoResponse.create(result)
            }

        } catch (e: Exception) {
            RepoResponse.create(e)
        }
    }

    private fun encryptRequestBody(body: String): FulfilmentEncryptedRequest? {
        try {
            val encodedKey: ByteArray = security.encodedKey
            val encryptedKey: String = security.encryptKey(encodedKey)
            val encryptedBody: String = security.encrypt(body, encodedKey)
            return FulfilmentEncryptedRequest(encryptedBody, encryptedKey)
        } catch (e: NoSuchAlgorithmException) {
            Log.d("securityA",e.message.toString())
            //logger.error("Error in encryption", e)
        } catch (e: IllegalBlockSizeException) {
            //logger.error("Error in encryption", e)
            Log.d("securityA",e.message.toString())
        } catch (e: BadPaddingException) {
            //logger.error("Error in encryption", e)
            Log.d("securityA",e.message.toString())
        } catch (e: IllegalAccessException) {
            //logger.error("Error in encryption", e)
            Log.d("securityA",e.message.toString())
        } catch (e: InvalidKeyException) {
            //logger.error("Error in encryption", e)
            Log.d("securityA",e.message.toString())
        }
        catch (e : Exception){
            Log.d("securityA",e.message.toString())
        }
        return null
    }

    suspend fun getHistory(
        request: MessagesHistoryRequest,
        userId: String
    ): RepoResponse<List<HistoryResponse>>? {
        return try {
            val result = endpoints.getHistory(
                botId = SalmaSdkInstance.botId,
                botChannelId = SalmaSdkInstance.botChannelId,
                userId = userId,
                body = request
            )
            RepoResponse.create(result)
        } catch (e: Exception) {
            RepoResponse.create(e)
        }
    }



}
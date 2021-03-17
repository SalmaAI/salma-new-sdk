package com.banking.core.remote


import ai.mawdoo3.salma.data.SendMessageRequest
import ai.mawdoo3.salma.data.SendMessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiEndpoints {

    @POST("{botId}/{botChannelId}/message")
    suspend fun sendMessage(
        @Body sendMessageRequest: SendMessageRequest,
        @Path("botId") botId: String,
        @Path("botChannelId") botChannelId: String
    ): Response<SendMessageResponse>


}
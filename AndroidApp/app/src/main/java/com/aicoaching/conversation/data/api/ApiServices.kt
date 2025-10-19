package com.aicoaching.conversation.data.api

import com.aicoaching.conversation.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface OpenAIService {
    @POST("v1/chat/completions")
    suspend fun sendMessage(
        @Header("Authorization") auth: String,
        @Body request: OpenAIRequest
    ): Response<OpenAIResponse>
}

interface ElevenLabsService {
    @POST("v1/speech-to-text")
    suspend fun transcribeAudio(
        @Header("xi-api-key") apiKey: String,
        @Body request: ElevenLabsTranscriptionRequest
    ): Response<ElevenLabsTranscriptionResponse>
}

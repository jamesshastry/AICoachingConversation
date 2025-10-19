package com.aicoaching.conversation.data.repository

import com.aicoaching.conversation.data.api.ElevenLabsService
import com.aicoaching.conversation.data.api.OpenAIService
import com.aicoaching.conversation.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ConversationRepository {
    
    private val openAIRetrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val elevenLabsRetrofit = Retrofit.Builder()
        .baseUrl("https://api.elevenlabs.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val openAIService = openAIRetrofit.create(OpenAIService::class.java)
    private val elevenLabsService = elevenLabsRetrofit.create(ElevenLabsService::class.java)
    
    suspend fun sendMessageToOpenAI(
        messages: List<ConversationMessage>,
        apiKey: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val openAIMessages = messages.map { message ->
                OpenAIMessage(
                    role = if (message.isFromUser) "user" else "assistant",
                    content = message.content
                )
            }
            
            val request = OpenAIRequest(messages = openAIMessages)
            val response = openAIService.sendMessage("Bearer $apiKey", request)
            
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.choices?.isNotEmpty() == true) {
                    Result.success(responseBody.choices[0].message.content)
                } else {
                    Result.failure(Exception("Empty response from OpenAI"))
                }
            } else {
                Result.failure(Exception("OpenAI API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun transcribeAudioWithElevenLabs(
        audioBase64: String,
        apiKey: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val request = ElevenLabsTranscriptionRequest(audio = audioBase64)
            val response = elevenLabsService.transcribeAudio(apiKey, request)
            
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.text != null) {
                    Result.success(responseBody.text)
                } else {
                    Result.failure(Exception("Empty transcription from ElevenLabs"))
                }
            } else {
                Result.failure(Exception("ElevenLabs API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

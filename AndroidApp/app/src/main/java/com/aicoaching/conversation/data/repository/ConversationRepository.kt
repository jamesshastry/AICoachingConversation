package com.aicoaching.conversation.data.repository

import com.aicoaching.conversation.data.api.ElevenLabsService
import com.aicoaching.conversation.data.api.OpenAIService
import com.aicoaching.conversation.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ConversationRepository {
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    
    private val openAIRetrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val elevenLabsRetrofit = Retrofit.Builder()
        .baseUrl("https://api.elevenlabs.io/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val openAIService = openAIRetrofit.create(OpenAIService::class.java)
    private val elevenLabsService = elevenLabsRetrofit.create(ElevenLabsService::class.java)
    
    suspend fun sendMessageToOpenAI(
        messages: List<ConversationMessage>,
        apiKey: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ConversationRepository", "Sending message to OpenAI with key: ${apiKey.take(10)}...")
            
            val openAIMessages = messages.map { message ->
                OpenAIMessage(
                    role = if (message.isFromUser) "user" else "assistant",
                    content = message.content
                )
            }
            
            val request = OpenAIRequest(messages = openAIMessages, maxTokens = 1000)
            android.util.Log.d("ConversationRepository", "Request: $request")
            
            val response = openAIService.sendMessage("Bearer $apiKey", request)
            android.util.Log.d("ConversationRepository", "Response code: ${response.code()}, message: ${response.message()}")
            
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.choices?.isNotEmpty() == true) {
                    Result.success(responseBody.choices[0].message.content)
                } else {
                    Result.failure(Exception("Empty response from OpenAI"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                android.util.Log.e("ConversationRepository", "OpenAI API error: ${response.code()} ${response.message()}, body: $errorBody")
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

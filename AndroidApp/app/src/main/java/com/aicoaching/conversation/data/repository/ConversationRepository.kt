package com.aicoaching.conversation.data.repository

import com.aicoaching.conversation.data.api.OpenAIService
import com.aicoaching.conversation.data.api.VoiceAgentService
import com.aicoaching.conversation.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Base64
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
    
    private val openAIService = openAIRetrofit.create(OpenAIService::class.java)
    private val voiceAgentService = openAIRetrofit.create(VoiceAgentService::class.java)
    
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
    
    suspend fun transcribeAudioWithVoiceAgent(
        audioBase64: String,
        apiKey: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ConversationRepository", "Transcribing audio with OpenAI Voice Agent")
            android.util.Log.d("ConversationRepository", "Audio base64 length: ${audioBase64.length}")
            
            // Convert base64 to bytes
            val audioBytes = Base64.decode(audioBase64, Base64.DEFAULT)
            android.util.Log.d("ConversationRepository", "Audio bytes length: ${audioBytes.size}")
            
            // Create multipart request body with correct MIME type for MP4
            val requestBody = RequestBody.create(
                "audio/mp4".toMediaType(),
                audioBytes
            )
            
            val filePart = MultipartBody.Part.createFormData(
                "file",
                "audio.mp4",
                requestBody
            )
            
            val modelBody = RequestBody.create(
                "text/plain".toMediaType(),
                "whisper-1"
            )
            
            val languageBody = RequestBody.create(
                "text/plain".toMediaType(),
                "en"
            )
            
            val responseFormatBody = RequestBody.create(
                "text/plain".toMediaType(),
                "json"
            )
            
            val response = voiceAgentService.transcribeAudio(
                "Bearer $apiKey",
                filePart,
                modelBody,
                languageBody,
                responseFormatBody
            )
            
            android.util.Log.d("ConversationRepository", "Voice Agent transcription response code: ${response.code()}, message: ${response.message()}")
            
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.text != null) {
                    android.util.Log.d("ConversationRepository", "Transcription successful: ${responseBody.text}")
                    Result.success(responseBody.text)
                } else {
                    Result.failure(Exception("Empty transcription from Voice Agent"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                android.util.Log.e("ConversationRepository", "Voice Agent transcription error: ${response.code()} ${response.message()}, body: $errorBody")
                Result.failure(Exception("Voice Agent transcription error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            android.util.Log.e("ConversationRepository", "Voice Agent transcription error", e)
            Result.failure(e)
        }
    }
    
    suspend fun generateSpeechWithVoiceAgent(
        text: String,
        apiKey: String
    ): Result<ByteArray> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("ConversationRepository", "Generating speech with OpenAI Voice Agent")
            
            val request = VoiceAgentSpeechRequest(input = text)
            val response = voiceAgentService.generateSpeech("Bearer $apiKey", request)
            
            android.util.Log.d("ConversationRepository", "Voice Agent TTS response code: ${response.code()}, message: ${response.message()}")
            
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val audioBytes = responseBody.bytes()
                    android.util.Log.d("ConversationRepository", "Speech generation successful, ${audioBytes.size} bytes")
                    Result.success(audioBytes)
                } else {
                    Result.failure(Exception("Empty audio response from Voice Agent"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                android.util.Log.e("ConversationRepository", "Voice Agent TTS error: ${response.code()} ${response.message()}, body: $errorBody")
                Result.failure(Exception("Voice Agent TTS error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            android.util.Log.e("ConversationRepository", "Voice Agent TTS error", e)
            Result.failure(e)
        }
    }
}

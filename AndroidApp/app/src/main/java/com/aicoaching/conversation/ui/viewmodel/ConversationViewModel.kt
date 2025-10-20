package com.aicoaching.conversation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.aicoaching.conversation.config.DemoConfig
import com.aicoaching.conversation.data.model.ConversationMessage
import com.aicoaching.conversation.data.repository.ConversationRepository
import com.aicoaching.conversation.utils.ApiKeyManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class ConversationViewModel(private val context: Context) : ViewModel() {
    
    private val repository = ConversationRepository()
    private val apiKeyManager = ApiKeyManager(context)
    
    init {
        // Initialize demo API keys if demo mode is enabled and no keys are saved
        if (DemoConfig.ENABLE_DEMO_MODE && !apiKeyManager.hasApiKeys()) {
            android.util.Log.d("ConversationViewModel", "Demo mode enabled, saving demo API keys")
            apiKeyManager.saveOpenAIKey(DemoConfig.DEMO_OPENAI_KEY)
            android.util.Log.d("ConversationViewModel", "Demo API keys saved successfully")
        } else {
            android.util.Log.d("ConversationViewModel", "Demo mode disabled or keys already exist")
        }
        
        // Test API key retrieval
        testApiKeyRetrieval()
    }
    
    private val _messages = MutableStateFlow<List<ConversationMessage>>(emptyList())
    val messages: StateFlow<List<ConversationMessage>> = _messages.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    fun setApiKeys(openAIKey: String) {
        apiKeyManager.saveOpenAIKey(openAIKey)
    }
    
    fun hasApiKeys(): Boolean {
        return apiKeyManager.hasApiKeys()
    }
    
    fun sendTextMessage(content: String) {
        val openAIKey = apiKeyManager.getOpenAIKey()
        if (content.isBlank()) return
        
        if (openAIKey == null) {
            _error.value = "OpenAI API key not found. Please configure your API keys in Settings."
            return
        }
        
        // Debug: Log API key info (first 10 chars only for security)
        android.util.Log.d("ConversationViewModel", "Using OpenAI API key: ${openAIKey.take(10)}...")
        
        val userMessage = ConversationMessage(
            id = UUID.randomUUID().toString(),
            content = content,
            isFromUser = true,
            timestamp = System.currentTimeMillis(),
            isVoice = false
        )
        
        _messages.value = _messages.value + userMessage
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch {
            val result = repository.sendMessageToOpenAI(_messages.value, openAIKey)
            result.fold(
                onSuccess = { response ->
                    val aiMessage = ConversationMessage(
                        id = UUID.randomUUID().toString(),
                        content = response,
                        isFromUser = false,
                        timestamp = System.currentTimeMillis(),
                        isVoice = false
                    )
                    _messages.value = _messages.value + aiMessage
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Unknown error occurred"
                    _isLoading.value = false
                }
            )
        }
    }
    
    fun sendVoiceMessage(audioBase64: String) {
        val openAIKey = apiKeyManager.getOpenAIKey()
        
        if (audioBase64.isBlank()) return
        
        if (openAIKey == null) {
            _error.value = "OpenAI API key not found. Please configure your API keys in Settings."
            return
        }
        
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch {
            // First transcribe the audio using OpenAI Voice Agent
            val transcriptionResult = repository.transcribeAudioWithVoiceAgent(audioBase64, openAIKey)
            transcriptionResult.fold(
                onSuccess = { transcribedText ->
                    android.util.Log.d("ConversationViewModel", "Transcription successful: $transcribedText")
                    
                    val userMessage = ConversationMessage(
                        id = UUID.randomUUID().toString(),
                        content = transcribedText,
                        isFromUser = true,
                        timestamp = System.currentTimeMillis(),
                        isVoice = true
                    )
                    
                    _messages.value = _messages.value + userMessage
                    
                    // Then send to OpenAI for response
                    val openAIResult = repository.sendMessageToOpenAI(_messages.value, openAIKey)
                    openAIResult.fold(
                        onSuccess = { response ->
                            android.util.Log.d("ConversationViewModel", "OpenAI response: $response")
                            
                            val aiMessage = ConversationMessage(
                                id = UUID.randomUUID().toString(),
                                content = response,
                                isFromUser = false,
                                timestamp = System.currentTimeMillis(),
                                isVoice = true
                            )
                            _messages.value = _messages.value + aiMessage
                            
                            // Generate speech for the AI response using Voice Agent
                            generateAndPlaySpeech(response, openAIKey)
                        },
                        onFailure = { exception ->
                            _error.value = exception.message ?: "OpenAI error occurred"
                            _isLoading.value = false
                        }
                    )
                },
                onFailure = { exception ->
                    _error.value = "Speech-to-text error: ${exception.message}"
                    _isLoading.value = false
                }
            )
        }
    }
    
    private fun generateAndPlaySpeech(text: String, apiKey: String) {
        viewModelScope.launch {
            val speechResult = repository.generateSpeechWithVoiceAgent(text, apiKey)
            speechResult.fold(
                onSuccess = { audioBytes ->
                    android.util.Log.d("ConversationViewModel", "Speech generated successfully, ${audioBytes.size} bytes")
                    // TODO: Play the audio bytes using Android's MediaPlayer or ExoPlayer
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    android.util.Log.e("ConversationViewModel", "Speech generation failed", exception)
                    _error.value = "Speech generation error: ${exception.message}"
                    _isLoading.value = false
                }
            )
        }
    }
    
    fun setRecordingState(isRecording: Boolean) {
        _isRecording.value = isRecording
    }
    
    fun setPlayingState(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun clearMessages() {
        _messages.value = emptyList()
    }
    
    fun testApiKeyRetrieval() {
        val openAIKey = apiKeyManager.getOpenAIKey()
        
        android.util.Log.d("ConversationViewModel", "OpenAI Key retrieved: ${openAIKey?.take(10)}...")
        android.util.Log.d("ConversationViewModel", "Has OpenAI key: ${openAIKey != null}")
    }
}

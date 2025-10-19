package com.aicoaching.conversation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    
    fun setApiKeys(openAIKey: String, elevenLabsKey: String) {
        apiKeyManager.saveOpenAIKey(openAIKey)
        apiKeyManager.saveElevenLabsKey(elevenLabsKey)
    }
    
    fun hasApiKeys(): Boolean {
        return apiKeyManager.hasApiKeys()
    }
    
    fun sendTextMessage(content: String) {
        val openAIKey = apiKeyManager.getOpenAIKey()
        if (content.isBlank() || openAIKey == null) return
        
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
        val elevenLabsKey = apiKeyManager.getElevenLabsKey()
        val openAIKey = apiKeyManager.getOpenAIKey()
        if (audioBase64.isBlank() || elevenLabsKey == null || openAIKey == null) return
        
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch {
            // First transcribe the audio
            val transcriptionResult = repository.transcribeAudioWithElevenLabs(audioBase64, elevenLabsKey)
            transcriptionResult.fold(
                onSuccess = { transcribedText ->
                    val userMessage = ConversationMessage(
                        id = UUID.randomUUID().toString(),
                        content = transcribedText,
                        isFromUser = true,
                        timestamp = System.currentTimeMillis(),
                        isVoice = true
                    )
                    
                    _messages.value = _messages.value + userMessage
                    
                    // Then send to OpenAI
                    val openAIResult = repository.sendMessageToOpenAI(_messages.value, openAIKey)
                    openAIResult.fold(
                        onSuccess = { response ->
                            val aiMessage = ConversationMessage(
                                id = UUID.randomUUID().toString(),
                                content = response,
                                isFromUser = false,
                                timestamp = System.currentTimeMillis(),
                                isVoice = true
                            )
                            _messages.value = _messages.value + aiMessage
                            _isLoading.value = false
                        },
                        onFailure = { exception ->
                            _error.value = exception.message ?: "OpenAI error occurred"
                            _isLoading.value = false
                        }
                    )
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Transcription error occurred"
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
}

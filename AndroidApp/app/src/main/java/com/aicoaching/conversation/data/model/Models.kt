package com.aicoaching.conversation.data.model

import com.google.gson.annotations.SerializedName

data class ConversationMessage(
    val id: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long,
    val isVoice: Boolean = false
)

data class OpenAIMessage(
    val role: String, // "user" or "assistant"
    val content: String
)

data class OpenAIRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<OpenAIMessage>,
    @SerializedName("max_tokens")
    val maxTokens: Int = 1000,
    val temperature: Double = 0.7
)

data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: OpenAIMessage
)

// OpenAI Voice Agents models
data class VoiceAgentTranscriptionResponse(
    val text: String
)

data class VoiceAgentSpeechRequest(
    val model: String = "tts-1",
    val input: String,
    val voice: String = "alloy",
    val response_format: String = "mp3",
    val speed: Double = 1.0
)

package com.aicoaching.conversation.data.model

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
    val max_tokens: Int = 1000,
    val temperature: Double = 0.7
)

data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: OpenAIMessage
)

data class ElevenLabsTranscriptionRequest(
    val audio: String // Base64 encoded audio
)

data class ElevenLabsTranscriptionResponse(
    val text: String
)

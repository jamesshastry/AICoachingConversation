export interface ConversationMessage {
  id: string;
  content: string;
  isFromUser: boolean;
  timestamp: number;
  isVoice: boolean;
}

export interface ApiKeys {
  openAIKey: string;
  elevenLabsKey: string;
}

export interface AppState {
  messages: ConversationMessage[];
  isLoading: boolean;
  error: string | null;
  isRecording: boolean;
  isPlaying: boolean;
  apiKeys: ApiKeys | null;
  hasApiKeys: boolean;
}

export interface OpenAIRequest {
  model: string;
  messages: Array<{
    role: 'user' | 'assistant';
    content: string;
  }>;
  max_tokens: number;
  temperature: number;
}

export interface OpenAIResponse {
  choices: Array<{
    message: {
      role: string;
      content: string;
    };
  }>;
}

export interface ElevenLabsTranscriptionRequest {
  audio: string; // Base64 encoded audio
}

export interface ElevenLabsTranscriptionResponse {
  text: string;
}

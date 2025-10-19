import axios from 'axios';
import { OpenAIRequest, OpenAIResponse, ElevenLabsTranscriptionRequest, ElevenLabsTranscriptionResponse } from '../types';

const OPENAI_API_URL = process.env.REACT_APP_OPENAI_API_URL || 'https://api.openai.com/v1/chat/completions';
const ELEVENLABS_API_URL = process.env.REACT_APP_ELEVENLABS_API_URL || 'https://api.elevenlabs.io/v1/speech-to-text';
const OPENAI_MODEL = process.env.REACT_APP_OPENAI_MODEL || 'gpt-3.5-turbo';
const OPENAI_MAX_TOKENS = parseInt(process.env.REACT_APP_OPENAI_MAX_TOKENS || '1000');
const OPENAI_TEMPERATURE = parseFloat(process.env.REACT_APP_OPENAI_TEMPERATURE || '0.7');

// Get API keys from environment variables or use provided keys
const getOpenAIKey = (providedKey?: string): string => {
  return providedKey || process.env.REACT_APP_OPENAI_API_KEY || '';
};

const getElevenLabsKey = (providedKey?: string): string => {
  return providedKey || process.env.REACT_APP_ELEVENLABS_API_KEY || '';
};

export class ApiService {
  static async sendMessageToOpenAI(
    messages: Array<{ role: 'user' | 'assistant'; content: string }>,
    apiKey?: string
  ): Promise<string> {
    const key = getOpenAIKey(apiKey);
    if (!key) {
      throw new Error('OpenAI API key is required. Please set REACT_APP_OPENAI_API_KEY environment variable or provide an API key.');
    }
    const request: OpenAIRequest = {
      model: OPENAI_MODEL,
      messages,
      max_tokens: OPENAI_MAX_TOKENS,
      temperature: OPENAI_TEMPERATURE,
    };

    try {
      const response = await axios.post<OpenAIResponse>(OPENAI_API_URL, request, {
        headers: {
          'Authorization': `Bearer ${key}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.data.choices && response.data.choices.length > 0) {
        return response.data.choices[0].message.content;
      } else {
        throw new Error('Empty response from OpenAI');
      }
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(`OpenAI API error: ${error.response?.status} ${error.response?.statusText}`);
      }
      throw error;
    }
  }

  static async transcribeAudioWithElevenLabs(
    audioBase64: string,
    apiKey?: string
  ): Promise<string> {
    const key = getElevenLabsKey(apiKey);
    if (!key) {
      throw new Error('ElevenLabs API key is required. Please set REACT_APP_ELEVENLABS_API_KEY environment variable or provide an API key.');
    }
    const request: ElevenLabsTranscriptionRequest = {
      audio: audioBase64,
    };

    try {
      const response = await axios.post<ElevenLabsTranscriptionResponse>(
        ELEVENLABS_API_URL,
        request,
        {
          headers: {
            'xi-api-key': key,
            'Content-Type': 'application/json',
          },
        }
      );

      if (response.data.text) {
        return response.data.text;
      } else {
        throw new Error('Empty transcription from ElevenLabs');
      }
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(`ElevenLabs API error: ${error.response?.status} ${error.response?.statusText}`);
      }
      throw error;
    }
  }
}

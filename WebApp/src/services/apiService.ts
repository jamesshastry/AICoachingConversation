import axios from 'axios';
import { OpenAIRequest, OpenAIResponse, ElevenLabsTranscriptionRequest, ElevenLabsTranscriptionResponse } from '../types';

const OPENAI_API_URL = 'https://api.openai.com/v1/chat/completions';
const ELEVENLABS_API_URL = 'https://api.elevenlabs.io/v1/speech-to-text';

export class ApiService {
  static async sendMessageToOpenAI(
    messages: Array<{ role: 'user' | 'assistant'; content: string }>,
    apiKey: string
  ): Promise<string> {
    const request: OpenAIRequest = {
      model: 'gpt-3.5-turbo',
      messages,
      max_tokens: 1000,
      temperature: 0.7,
    };

    try {
      const response = await axios.post<OpenAIResponse>(OPENAI_API_URL, request, {
        headers: {
          'Authorization': `Bearer ${apiKey}`,
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
    apiKey: string
  ): Promise<string> {
    const request: ElevenLabsTranscriptionRequest = {
      audio: audioBase64,
    };

    try {
      const response = await axios.post<ElevenLabsTranscriptionResponse>(
        ELEVENLABS_API_URL,
        request,
        {
          headers: {
            'xi-api-key': apiKey,
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

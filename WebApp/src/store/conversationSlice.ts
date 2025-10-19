import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { AppState, ConversationMessage, ApiKeys } from '../types';

const initialState: AppState = {
  messages: [],
  isLoading: false,
  error: null,
  isRecording: false,
  isPlaying: false,
  apiKeys: null,
  hasApiKeys: false,
};

const conversationSlice = createSlice({
  name: 'conversation',
  initialState,
  reducers: {
    addMessage: (state, action: PayloadAction<ConversationMessage>) => {
      state.messages.push(action.payload);
    },
    clearMessages: (state) => {
      state.messages = [];
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },
    setRecording: (state, action: PayloadAction<boolean>) => {
      state.isRecording = action.payload;
    },
    setPlaying: (state, action: PayloadAction<boolean>) => {
      state.isPlaying = action.payload;
    },
    setApiKeys: (state, action: PayloadAction<ApiKeys>) => {
      state.apiKeys = action.payload;
      state.hasApiKeys = true;
      // Store in localStorage for persistence
      localStorage.setItem('apiKeys', JSON.stringify(action.payload));
    },
    loadApiKeys: (state) => {
      const stored = localStorage.getItem('apiKeys');
      if (stored) {
        try {
          state.apiKeys = JSON.parse(stored);
          state.hasApiKeys = true;
        } catch (error) {
          console.error('Failed to parse stored API keys:', error);
        }
      }
    },
    clearApiKeys: (state) => {
      state.apiKeys = null;
      state.hasApiKeys = false;
      localStorage.removeItem('apiKeys');
    },
  },
});

export const {
  addMessage,
  clearMessages,
  setLoading,
  setError,
  setRecording,
  setPlaying,
  setApiKeys,
  loadApiKeys,
  clearApiKeys,
} = conversationSlice.actions;

export default conversationSlice.reducer;

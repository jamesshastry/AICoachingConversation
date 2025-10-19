import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import {
  Box,
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  TextField,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Alert,
  Snackbar,
} from '@mui/material';
import {
  ArrowBack as ArrowBackIcon,
  Send as SendIcon,
  Mic as MicIcon,
  Stop as StopIcon,
  PlayArrow as PlayIcon,
} from '@mui/icons-material';
import { RootState } from '../store/store';
import {
  addMessage,
  clearMessages,
  setLoading,
  setError,
  setRecording,
  setPlaying,
} from '../store/conversationSlice';
import { ApiService } from '../services/apiService';
import { AudioService } from '../services/audioService';
import { ConversationMessage } from '../types';

const ConversationScreen: React.FC = () => {
  const { mode } = useParams<{ mode: 'text' | 'voice' }>();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  
  const { messages, isLoading, error, isRecording, isPlaying, apiKeys } = useSelector(
    (state: RootState) => state.conversation
  );

  const [textInput, setTextInput] = useState('');
  const [audioService] = useState(() => new AudioService());

  useEffect(() => {
    if (error) {
      const timer = setTimeout(() => {
        dispatch(setError(null));
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [error, dispatch]);

  const sendTextMessage = async () => {
    if (!textInput.trim() || !apiKeys?.openAIKey) return;

    const userMessage: ConversationMessage = {
      id: Date.now().toString(),
      content: textInput,
      isFromUser: true,
      timestamp: Date.now(),
      isVoice: false,
    };

    dispatch(addMessage(userMessage));
    setTextInput('');
    dispatch(setLoading(true));

    try {
      const conversationHistory = messages.map(msg => ({
        role: msg.isFromUser ? 'user' as const : 'assistant' as const,
        content: msg.content,
      }));

      const response = await ApiService.sendMessageToOpenAI(
        conversationHistory,
        apiKeys.openAIKey
      );

      const aiMessage: ConversationMessage = {
        id: (Date.now() + 1).toString(),
        content: response,
        isFromUser: false,
        timestamp: Date.now(),
        isVoice: false,
      };

      dispatch(addMessage(aiMessage));
    } catch (error) {
      dispatch(setError(error instanceof Error ? error.message : 'Unknown error'));
    } finally {
      dispatch(setLoading(false));
    }
  };

  const handleVoiceRecording = async () => {
    if (!apiKeys?.openAIKey || !apiKeys?.elevenLabsKey) {
      dispatch(setError('API keys not configured'));
      return;
    }

    if (isRecording) {
      try {
        dispatch(setRecording(false));
        const audioBase64 = await audioService.stopRecording();
        
        dispatch(setLoading(true));
        
        // Transcribe audio
        const transcribedText = await ApiService.transcribeAudioWithElevenLabs(
          audioBase64,
          apiKeys.elevenLabsKey
        );

        const userMessage: ConversationMessage = {
          id: Date.now().toString(),
          content: transcribedText,
          isFromUser: true,
          timestamp: Date.now(),
          isVoice: true,
        };

        dispatch(addMessage(userMessage));

        // Send to OpenAI
        const conversationHistory = messages.map(msg => ({
          role: msg.isFromUser ? 'user' as const : 'assistant' as const,
          content: msg.content,
        }));

        const response = await ApiService.sendMessageToOpenAI(
          conversationHistory,
          apiKeys.openAIKey
        );

        const aiMessage: ConversationMessage = {
          id: (Date.now() + 1).toString(),
          content: response,
          isFromUser: false,
          timestamp: Date.now(),
          isVoice: true,
        };

        dispatch(addMessage(aiMessage));
      } catch (error) {
        dispatch(setError(error instanceof Error ? error.message : 'Recording failed'));
      } finally {
        dispatch(setLoading(false));
      }
    } else {
      try {
        await audioService.startRecording();
        dispatch(setRecording(true));
      } catch (error) {
        dispatch(setError('Failed to start recording'));
      }
    }
  };

  const playAudio = async (message: ConversationMessage) => {
    if (message.isVoice && !message.isFromUser) {
      try {
        dispatch(setPlaying(true));
        // In a real implementation, you would synthesize the text to speech
        // For now, we'll use Web Speech API
        const utterance = new SpeechSynthesisUtterance(message.content);
        utterance.onend = () => dispatch(setPlaying(false));
        speechSynthesis.speak(utterance);
      } catch (error) {
        dispatch(setError('Failed to play audio'));
        dispatch(setPlaying(false));
      }
    }
  };

  return (
    <Box sx={{ height: '100vh', display: 'flex', flexDirection: 'column' }}>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            edge="start"
            color="inherit"
            onClick={() => navigate('/')}
            sx={{ mr: 2 }}
          >
            <ArrowBackIcon />
          </IconButton>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            {mode === 'voice' ? 'Voice Chat' : 'Text Chat'}
          </Typography>
          <Button color="inherit" onClick={() => dispatch(clearMessages())}>
            Clear
          </Button>
        </Toolbar>
      </AppBar>

      <Box sx={{ flex: 1, overflow: 'auto', p: 2 }}>
        {messages.map((message) => (
          <Card
            key={message.id}
            sx={{
              mb: 2,
              ml: message.isFromUser ? 'auto' : 0,
              mr: message.isFromUser ? 0 : 'auto',
              maxWidth: '70%',
              backgroundColor: message.isFromUser ? 'primary.main' : 'grey.100',
              color: message.isFromUser ? 'primary.contrastText' : 'text.primary',
            }}
          >
            <CardContent>
              <Typography variant="body1">{message.content}</Typography>
              {message.isVoice && !message.isFromUser && mode === 'voice' && (
                <IconButton
                  onClick={() => playAudio(message)}
                  disabled={isPlaying}
                  sx={{ mt: 1 }}
                >
                  <PlayIcon />
                </IconButton>
              )}
              {message.isVoice && message.isFromUser && (
                <Typography variant="caption" sx={{ display: 'block', mt: 1 }}>
                  🎤 Voice message
                </Typography>
              )}
            </CardContent>
          </Card>
        ))}

        {isLoading && (
          <Card sx={{ mb: 2, maxWidth: '70%' }}>
            <CardContent sx={{ display: 'flex', alignItems: 'center' }}>
              <CircularProgress size={20} sx={{ mr: 2 }} />
              <Typography variant="body2">AI is thinking...</Typography>
            </CardContent>
          </Card>
        )}
      </Box>

      <Box sx={{ p: 2, borderTop: 1, borderColor: 'divider' }}>
        {mode === 'text' ? (
          <Box sx={{ display: 'flex', gap: 1 }}>
            <TextField
              fullWidth
              placeholder="Type your message..."
              value={textInput}
              onChange={(e) => setTextInput(e.target.value)}
              onKeyPress={(e) => {
                if (e.key === 'Enter' && !e.shiftKey) {
                  e.preventDefault();
                  sendTextMessage();
                }
              }}
              disabled={isLoading}
            />
            <Button
              variant="contained"
              onClick={sendTextMessage}
              disabled={!textInput.trim() || isLoading}
            >
              <SendIcon />
            </Button>
          </Box>
        ) : (
          <Box sx={{ display: 'flex', justifyContent: 'center' }}>
            <IconButton
              onClick={handleVoiceRecording}
              disabled={isLoading}
              sx={{
                width: 80,
                height: 80,
                backgroundColor: isRecording ? 'error.main' : 'primary.main',
                color: 'white',
                '&:hover': {
                  backgroundColor: isRecording ? 'error.dark' : 'primary.dark',
                },
              }}
            >
              {isRecording ? <StopIcon /> : <MicIcon />}
            </IconButton>
          </Box>
        )}
      </Box>

      <Snackbar
        open={!!error}
        autoHideDuration={5000}
        onClose={() => dispatch(setError(null))}
      >
        <Alert severity="error" onClose={() => dispatch(setError(null))}>
          {error}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default ConversationScreen;

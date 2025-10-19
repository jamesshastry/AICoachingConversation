import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import {
  Box,
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  TextField,
  Button,
  Alert,
  Snackbar,
  InputAdornment,
} from '@mui/material';
import {
  ArrowBack as ArrowBackIcon,
  Visibility as VisibilityIcon,
  VisibilityOff as VisibilityOffIcon,
} from '@mui/icons-material';
import { RootState } from '../store/store';
import { setApiKeys, setError } from '../store/conversationSlice';

const ApiKeySettingsScreen: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { error } = useSelector((state: RootState) => state.conversation);

  const [openAIKey, setOpenAIKey] = useState('');
  const [elevenLabsKey, setElevenLabsKey] = useState('');
  const [showOpenAIKey, setShowOpenAIKey] = useState(false);
  const [showElevenLabsKey, setShowElevenLabsKey] = useState(false);

  useEffect(() => {
    // Load existing API keys from localStorage
    const stored = localStorage.getItem('apiKeys');
    if (stored) {
      try {
        const keys = JSON.parse(stored);
        setOpenAIKey(keys.openAIKey || '');
        setElevenLabsKey(keys.elevenLabsKey || '');
      } catch (error) {
        console.error('Failed to load stored API keys:', error);
      }
    }
  }, []);

  const handleSave = () => {
    if (!openAIKey.trim() || !elevenLabsKey.trim()) {
      dispatch(setError('Please enter both API keys'));
      return;
    }

    dispatch(setApiKeys({
      openAIKey: openAIKey.trim(),
      elevenLabsKey: elevenLabsKey.trim(),
    }));

    navigate('/');
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
            API Keys Configuration
          </Typography>
        </Toolbar>
      </AppBar>

      <Box sx={{ flex: 1, p: 3 }}>
        <Typography variant="h5" gutterBottom>
          Configure your API keys
        </Typography>
        
        <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
          Enter your API keys to enable AI coaching conversations. Your keys are stored securely in your browser's local storage.
        </Typography>

        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, maxWidth: 600 }}>
          <TextField
            fullWidth
            label="OpenAI API Key"
            placeholder="sk-..."
            value={openAIKey}
            onChange={(e) => setOpenAIKey(e.target.value)}
            type={showOpenAIKey ? 'text' : 'password'}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    onClick={() => setShowOpenAIKey(!showOpenAIKey)}
                    edge="end"
                  >
                    {showOpenAIKey ? <VisibilityOffIcon /> : <VisibilityIcon />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
            helperText="Get your API key from https://platform.openai.com/api-keys"
          />

          <TextField
            fullWidth
            label="ElevenLabs API Key"
            placeholder="Your ElevenLabs API key"
            value={elevenLabsKey}
            onChange={(e) => setElevenLabsKey(e.target.value)}
            type={showElevenLabsKey ? 'text' : 'password'}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    onClick={() => setShowElevenLabsKey(!showElevenLabsKey)}
                    edge="end"
                  >
                    {showElevenLabsKey ? <VisibilityOffIcon /> : <VisibilityIcon />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
            helperText="Get your API key from https://elevenlabs.io/"
          />

          <Button
            variant="contained"
            onClick={handleSave}
            disabled={!openAIKey.trim() || !elevenLabsKey.trim()}
            sx={{ alignSelf: 'flex-start' }}
          >
            Save API Keys
          </Button>
        </Box>

        <Alert severity="info" sx={{ mt: 3, maxWidth: 600 }}>
          <Typography variant="body2">
            <strong>Security Note:</strong> Your API keys are stored locally in your browser and are not shared with any third parties. 
            Make sure to keep your API keys secure and never share them publicly.
          </Typography>
        </Alert>
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

export default ApiKeySettingsScreen;

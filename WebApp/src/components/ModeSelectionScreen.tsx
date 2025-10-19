import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  Grid,
  IconButton,
} from '@mui/material';
import {
  Chat as ChatIcon,
  Mic as MicIcon,
  Settings as SettingsIcon,
} from '@mui/icons-material';

const ModeSelectionScreen: React.FC = () => {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 3,
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      }}
    >
      <Typography
        variant="h3"
        component="h1"
        gutterBottom
        sx={{
          color: 'white',
          fontWeight: 'bold',
          textAlign: 'center',
          mb: 2,
        }}
      >
        AI Coaching Conversation
      </Typography>

      <Typography
        variant="h6"
        sx={{
          color: 'rgba(255, 255, 255, 0.8)',
          textAlign: 'center',
          mb: 6,
          maxWidth: 600,
        }}
      >
        Choose your preferred conversation mode
      </Typography>

      <Grid container spacing={3} sx={{ maxWidth: 800, mb: 4 }}>
        <Grid item xs={12} md={6}>
          <Card
            sx={{
              height: 250,
              cursor: 'pointer',
              transition: 'transform 0.2s',
              '&:hover': {
                transform: 'translateY(-4px)',
              },
            }}
            onClick={() => navigate('/conversation/text')}
          >
            <CardContent
              sx={{
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                textAlign: 'center',
              }}
            >
              <ChatIcon sx={{ fontSize: 60, color: 'primary.main', mb: 2 }} />
              <Typography variant="h5" component="h2" gutterBottom>
                Text Chat
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Type your messages and receive text responses
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={6}>
          <Card
            sx={{
              height: 250,
              cursor: 'pointer',
              transition: 'transform 0.2s',
              '&:hover': {
                transform: 'translateY(-4px)',
              },
            }}
            onClick={() => navigate('/conversation/voice')}
          >
            <CardContent
              sx={{
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                textAlign: 'center',
              }}
            >
              <MicIcon sx={{ fontSize: 60, color: 'primary.main', mb: 2 }} />
              <Typography variant="h5" component="h2" gutterBottom>
                Voice Chat
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Speak your messages and receive voice responses
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Button
        variant="outlined"
        startIcon={<SettingsIcon />}
        onClick={() => navigate('/settings')}
        sx={{
          color: 'white',
          borderColor: 'white',
          '&:hover': {
            borderColor: 'white',
            backgroundColor: 'rgba(255, 255, 255, 0.1)',
          },
        }}
      >
        Configure API Keys
      </Button>
    </Box>
  );
};

export default ModeSelectionScreen;

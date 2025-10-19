import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { Container, Box } from '@mui/material';
import ModeSelectionScreen from './components/ModeSelectionScreen';
import ConversationScreen from './components/ConversationScreen';
import ApiKeySettingsScreen from './components/ApiKeySettingsScreen';

function App() {
  return (
    <Box sx={{ height: '100vh', display: 'flex', flexDirection: 'column' }}>
      <Container maxWidth="md" sx={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <Routes>
          <Route path="/" element={<ModeSelectionScreen />} />
          <Route path="/conversation/:mode" element={<ConversationScreen />} />
          <Route path="/settings" element={<ApiKeySettingsScreen />} />
        </Routes>
      </Container>
    </Box>
  );
}

export default App;

# AI Coaching Conversation - Web Application

A modern web application for AI-powered coaching conversations built with React, TypeScript, and Material-UI.

## Features

- **Text & Voice Modes**: Choose between text chat or voice conversations
- **OpenAI Integration**: Powered by GPT-3.5-turbo for intelligent coaching responses
- **ElevenLabs Integration**: Voice transcription and text-to-speech capabilities
- **Responsive Design**: Works on desktop, tablet, and mobile browsers
- **Progressive Web App**: Installable on mobile devices
- **Real-time Communication**: Instant responses and audio playback
- **Secure Storage**: API keys stored locally in browser

## Technology Stack

- **Frontend**: React 18 with TypeScript
- **UI Library**: Material-UI (MUI) v5
- **State Management**: Redux Toolkit
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **Audio**: Web Audio API + Speech Recognition API
- **Styling**: Emotion (CSS-in-JS)

## Prerequisites

- Node.js 18+ 
- npm or yarn
- OpenAI API key
- ElevenLabs API key

## Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd WebApp
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```

4. **Open your browser**
   Navigate to `http://localhost:3000`

## Configuration

1. **Configure API Keys**
   - Click "Configure API Keys" on the main screen
   - Enter your OpenAI API key (starts with "sk-...")
   - Enter your ElevenLabs API key
   - Click "Save API Keys"

2. **Start Conversations**
   - Choose "Text Chat" for typing messages
   - Choose "Voice Chat" for speaking messages
   - Grant microphone permission when prompted

## Usage

### Text Mode
- Type your messages in the text field
- Press Enter or click Send to send your message
- Receive text responses from the AI coach

### Voice Mode
- Click the microphone button to start recording
- Speak your message clearly
- Click the stop button to end recording
- Your voice will be transcribed and sent to the AI
- Receive voice responses (click play button to hear them)

## API Integration

### OpenAI API
- **Endpoint**: `https://api.openai.com/v1/chat/completions`
- **Model**: GPT-3.5-turbo
- **Authentication**: Bearer token in Authorization header

### ElevenLabs API
- **Endpoint**: `https://api.elevenlabs.io/v1/speech-to-text`
- **Authentication**: API key in xi-api-key header

## Browser Compatibility

- **Chrome**: Full support
- **Firefox**: Full support
- **Safari**: Full support
- **Edge**: Full support
- **Mobile Browsers**: Full support

## Security

- API keys are stored locally in browser's localStorage
- No data is sent to external servers except OpenAI and ElevenLabs
- All communication uses HTTPS
- Microphone access is only used for voice recording

## Development

### Available Scripts

- `npm start`: Start development server
- `npm build`: Build for production
- `npm test`: Run tests
- `npm eject`: Eject from Create React App

### Project Structure

```
src/
├── components/          # React components
│   ├── ModeSelectionScreen.tsx
│   ├── ConversationScreen.tsx
│   └── ApiKeySettingsScreen.tsx
├── services/           # API and utility services
│   ├── apiService.ts
│   └── audioService.ts
├── store/              # Redux store and slices
│   ├── store.ts
│   └── conversationSlice.ts
├── types/              # TypeScript type definitions
│   └── index.ts
├── App.tsx             # Main app component
└── index.tsx           # App entry point
```

## Deployment

### Build for Production
```bash
npm run build
```

### Deploy to Static Hosting
The `build` folder contains static files that can be deployed to:
- Netlify
- Vercel
- GitHub Pages
- AWS S3
- Any static hosting service

## Troubleshooting

### Common Issues

1. **Microphone Permission Denied**
   - Grant microphone permission in browser settings
   - Refresh the page and try again

2. **API Key Errors**
   - Verify your API keys are correct
   - Check that you have sufficient credits
   - Ensure your API keys have the right permissions

3. **Audio Playback Issues**
   - Check browser audio settings
   - Ensure speakers/headphones are connected
   - Try refreshing the page

### Browser Console Errors

- Check the browser console for detailed error messages
- Ensure all required permissions are granted
- Verify network connectivity

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License.

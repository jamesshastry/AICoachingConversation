# AI Coaching Conversation - Replit Setup

## Overview
This is a React-based AI Coaching Conversation web application that enables users to have intelligent conversations with an AI coach through both text and voice modes. The application uses OpenAI's GPT-3.5-turbo for conversation and ElevenLabs for voice transcription and text-to-speech.

## Recent Changes
- **October 19, 2025**: Initial Replit setup completed
  - Configured server to run on port 5000 with 0.0.0.0 binding
  - Fixed TypeScript compilation errors in routing and theme configuration
  - Set up build workflow and deployment configuration
  - Configured environment for Replit's proxy system

## Project Architecture

### Structure
```
AICoachingConversation/
├── AndroidApp/          # Android application (not used in Replit)
├── WebApp/             # React web application (active)
│   ├── src/            # Source code
│   │   ├── components/ # React components
│   │   ├── services/   # API and audio services
│   │   ├── store/      # Redux state management
│   │   └── types/      # TypeScript type definitions
│   ├── build/          # Production build output (generated)
│   ├── server.js       # Express server for serving the app
│   └── package.json    # Node.js dependencies
└── README.md
```

### Technology Stack
- **Frontend**: React 18 with TypeScript
- **UI Library**: Material-UI (MUI)
- **State Management**: Redux Toolkit
- **Routing**: React Router v6
- **Backend Server**: Express.js (serves static files)
- **AI Integration**: OpenAI GPT-3.5-turbo
- **Voice Integration**: ElevenLabs API

### Key Features
1. **Text Chat Mode**: Type messages and receive text responses from AI coach
2. **Voice Chat Mode**: Speak messages and receive voice responses
3. **API Key Management**: Built-in settings screen for managing API keys
4. **Responsive Design**: Works on desktop, tablet, and mobile browsers
5. **Material Design**: Modern UI with Material-UI components

## Environment Variables

### Required Secrets (Set in Replit Secrets)
- `REACT_APP_OPENAI_API_KEY` - Your OpenAI API key for GPT-3.5-turbo
- `REACT_APP_ELEVENLABS_API_KEY` - Your ElevenLabs API key for voice features

### Optional Configuration
These are configured in the app but can be overridden:
- `REACT_APP_OPENAI_MODEL=gpt-3.5-turbo`
- `REACT_APP_OPENAI_MAX_TOKENS=1000`
- `REACT_APP_OPENAI_TEMPERATURE=0.7`
- `REACT_APP_ENABLE_VOICE_MODE=true`
- `REACT_APP_ENABLE_TEXT_MODE=true`

## Running the Application

### Development Mode
The app runs in production mode in Replit (build + serve) for optimal performance.

**Workflow**: `WebApp`
- Builds the React app using `npm run build`
- Serves the built files using Express on port 5000
- Binds to 0.0.0.0 for Replit's proxy system

### Build Process
1. `npm install` - Installs dependencies
2. `npm run build` - Creates optimized production build
3. `node server.js` - Serves the app on port 5000

## Deployment Configuration

**Deployment Type**: Autoscale (stateless web application)
**Build Command**: `cd WebApp && npm install && npm run build`
**Run Command**: `cd WebApp && node server.js`
**Port**: 5000

## API Keys Setup

### Getting OpenAI API Key
1. Go to https://platform.openai.com/api-keys
2. Sign in or create an account
3. Click "Create new secret key"
4. Copy the key (starts with "sk-")
5. Add to Replit Secrets as `REACT_APP_OPENAI_API_KEY`

### Getting ElevenLabs API Key
1. Go to https://elevenlabs.io/
2. Sign in or create an account
3. Go to Profile → API Key
4. Copy your API key
5. Add to Replit Secrets as `REACT_APP_ELEVENLABS_API_KEY`

## Configuration Files

### Important Files
- `WebApp/server.js` - Express server configured for port 5000 and 0.0.0.0 binding
- `WebApp/package.json` - npm scripts and dependencies
- `WebApp/.env` - Local environment configuration for dev server
- `WebApp/.gitignore` - Excludes node_modules, build, and sensitive files

### Fixed Issues
1. **Routing**: Changed from prop-based to URL parameter-based routing for conversation mode
2. **Theme**: Removed unsupported `tertiary` color from MUI theme
3. **Port Configuration**: Updated server to use port 5000 with 0.0.0.0 binding
4. **Host Check**: Disabled host check for Replit's proxy system

## Testing

### Health Check
The server provides a health check endpoint at `/health` that returns:
```json
{
  "status": "healthy",
  "timestamp": "2025-10-19T10:19:26.090Z",
  "uptime": 123.456,
  "environment": "production"
}
```

### Application Routes
- `/` - Main mode selection screen
- `/conversation/text` - Text chat interface
- `/conversation/voice` - Voice chat interface
- `/settings` - API key configuration screen

## Known Warnings
The build completes successfully but shows two ESLint warnings:
- Unused `apiKeys` variable in ConversationScreen.tsx (line 43)
- Unused `IconButton` import in ModeSelectionScreen.tsx (line 10)

These are non-critical and don't affect functionality.

## User Preferences
- None specified yet

## Future Enhancements
- Fix ESLint warnings by cleaning up unused variables
- Add conversation history persistence
- Implement ElevenLabs text-to-speech for voice responses
- Add user authentication and profile management
- Implement conversation export functionality

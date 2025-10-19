# AI Coaching Conversation Platform

A comprehensive platform for AI-powered coaching conversations available on both Android and Web platforms.

## Project Structure

```
AICoachingConversation/
├── AndroidApp/          # Android application (Jetpack Compose)
├── WebApp/             # Web application (React + TypeScript)
└── README.md           # This file
```

## Features

### 🤖 AI Coaching Conversations
- **Dual Platform Support**: Native Android app and modern web application
- **Text & Voice Modes**: Choose between text chat or voice conversations
- **OpenAI Integration**: Powered by GPT-3.5-turbo for intelligent coaching responses
- **ElevenLabs Integration**: Voice transcription and text-to-speech capabilities
- **Secure API Management**: Encrypted storage of API keys

### 📱 Android App Features
- **Modern UI**: Built with Jetpack Compose and Material3 design
- **Native Performance**: Optimized for Android devices
- **Offline Capability**: Local storage and caching
- **Push Notifications**: Real-time conversation updates
- **Biometric Security**: Secure API key storage

### 🌐 Web App Features
- **Responsive Design**: Works on desktop, tablet, and mobile browsers
- **Real-time Communication**: WebSocket connections for instant responses
- **Progressive Web App**: Installable on mobile devices
- **Cross-platform**: Works on any device with a modern browser
- **Cloud Sync**: Conversation history synchronized across devices

## Quick Start

### Android App
```bash
cd AndroidApp
./gradlew assembleDebug
```

### Web App (Local Development)
```bash
cd WebApp
npm install
npm start
```

### Web App (Production Deployment)

#### Deploy to Render.com
1. Follow the guide in `WebApp/DEPLOYMENT.md`
2. Set environment variables in Render dashboard
3. Deploy as static site or web service

#### Deploy to Replit
1. Follow the guide in `WebApp/REPLIT_DEPLOYMENT.md`
2. Import WebApp folder to Replit
3. Set API keys in Replit Secrets
4. Run the application

## API Keys Required

Both applications require:
- **OpenAI API Key**: For AI coaching responses
- **ElevenLabs API Key**: For voice transcription and synthesis

## Technology Stack

### Android App
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with StateFlow
- **Networking**: Retrofit + OkHttp
- **Security**: EncryptedSharedPreferences
- **Audio**: MediaRecorder + MediaPlayer

### Web App
- **Language**: TypeScript
- **Framework**: React 18
- **UI Library**: Material-UI (MUI)
- **State Management**: Redux Toolkit
- **Networking**: Axios
- **Audio**: Web Audio API + Speech Recognition API
- **Real-time**: Socket.io
- **Deployment**: Render.com, Replit

## Development

### Prerequisites
- **Android**: Android Studio, JDK 11+
- **Web**: Node.js 18+, npm/yarn

### Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test on both platforms
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
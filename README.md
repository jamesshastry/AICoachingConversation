# AI Coaching Conversation App

An Android app that enables users to have conversations with an AI coach through both text and voice interactions.

## Features

- **Dual Interaction Modes**: Choose between text chat or voice conversation
- **Voice Transcription**: Uses ElevenLabs API to transcribe voice messages to text
- **AI Responses**: Integrates with OpenAI API for intelligent coaching responses
- **Modern UI**: Built with Jetpack Compose for a modern, responsive interface
- **Secure API Key Storage**: API keys are stored securely on the device

## Setup Instructions

### Prerequisites

1. **Android Studio** (latest version recommended)
2. **Android SDK** with API level 24+ (Android 7.0)
3. **OpenAI API Key** - Get yours from [OpenAI Platform](https://platform.openai.com/api-keys)
4. **ElevenLabs API Key** - Get yours from [ElevenLabs](https://elevenlabs.io/)

### Installation

1. **Clone or download** this project to your local machine
2. **Open Android Studio** and select "Open an existing project"
3. **Navigate** to the project folder and select it
4. **Wait** for Gradle sync to complete
5. **Connect** an Android device or start an emulator
6. **Run** the app by clicking the "Run" button

### API Key Configuration

1. **Launch** the app
2. **Tap** "Configure API Keys" on the main screen
3. **Enter** your OpenAI API key (starts with "sk-...")
4. **Enter** your ElevenLabs API key
5. **Tap** "Save API Keys"
6. **Return** to the main screen and select your preferred conversation mode

## Usage

### Text Mode
- Tap "Text Chat" on the main screen
- Type your messages in the text field
- Tap the send button to send your message
- Receive text responses from the AI coach

### Voice Mode
- Tap "Voice Chat" on the main screen
- Grant microphone permission when prompted
- Tap the microphone button to start recording
- Tap the stop button to end recording and send your message
- Your voice will be transcribed and sent to the AI
- Receive voice responses (tap play button to hear them)

## Technical Details

### Architecture
- **MVVM Pattern** with ViewModel and StateFlow
- **Jetpack Compose** for UI
- **Retrofit** for API communication
- **Coroutines** for asynchronous operations

### API Integration
- **OpenAI GPT-3.5-turbo** for AI responses
- **ElevenLabs Speech-to-Text** for voice transcription
- **Base64 encoding** for audio data transmission

### Permissions
- `RECORD_AUDIO` - Required for voice recording
- `INTERNET` - Required for API calls
- `WRITE_EXTERNAL_STORAGE` - For temporary audio files

## Future Enhancements

- Firebase Authentication integration
- Conversation history with Firebase Database
- Custom AI coaching instructions
- Audio playback for voice responses
- Conversation export functionality
- Offline mode support

## Troubleshooting

### Common Issues

1. **API Key Errors**: Ensure your API keys are valid and have sufficient credits
2. **Microphone Permission**: Grant microphone permission in device settings if prompted
3. **Network Issues**: Check your internet connection for API calls
4. **Audio Recording**: Ensure your device has a working microphone

### Support

For technical issues or feature requests, please check the project documentation or contact the development team.

## License

This project is for educational and personal use. Please ensure you comply with OpenAI and ElevenLabs terms of service when using their APIs.
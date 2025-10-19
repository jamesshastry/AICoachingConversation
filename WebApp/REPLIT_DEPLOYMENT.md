# Replit Deployment Guide

This guide will help you deploy the AI Coaching Conversation Web App to Replit.

## Prerequisites

- Replit account (free tier available)
- GitHub repository with the WebApp code
- OpenAI API key
- ElevenLabs API key

## Deployment Steps

### 1. Create a New Repl

#### Option A: Import from GitHub
1. **Go to [Replit](https://replit.com)**
2. **Click "Create Repl"**
3. **Select "Import from GitHub"**
4. **Enter your repository URL**: `https://github.com/jamesshastry/AICoachingConversation`
5. **Select "WebApp" folder** as the root directory
6. **Click "Import"**

#### Option B: Create Blank Repl
1. **Go to [Replit](https://replit.com)**
2. **Click "Create Repl"**
3. **Select "Node.js" template**
4. **Upload your WebApp files** or clone from GitHub

### 2. Configure Environment Variables

1. **Go to Tools → Secrets**
2. **Add the following secrets:**

```
REACT_APP_OPENAI_API_KEY=sk-your-openai-api-key-here
REACT_APP_ELEVENLABS_API_KEY=your-elevenlabs-api-key-here
NODE_ENV=production
REACT_APP_ENVIRONMENT=production
REACT_APP_APP_NAME=AI Coaching Conversation
REACT_APP_ENABLE_VOICE_MODE=true
REACT_APP_ENABLE_TEXT_MODE=true
REACT_APP_ENABLE_DEMO_MODE=false
```

### 3. Install Dependencies

1. **Open the Shell** (bottom panel)
2. **Run the following commands:**
   ```bash
   npm install
   ```

### 4. Build and Run

1. **Click the "Run" button** or press `Ctrl+Enter`
2. **The app will automatically:**
   - Build the React app
   - Start the Express server
   - Serve the application

### 5. Access Your App

- **Local URL**: `http://localhost:3000`
- **Replit URL**: `https://your-repl-name.your-username.repl.co`

## Configuration Files

### `.replit` Configuration
```json
{
  "name": "AI Coaching Conversation",
  "description": "AI-powered coaching conversations with text and voice support",
  "run": "npm run replit",
  "alwaysOn": false
}
```

### `replit.nix` Configuration
```nix
{ pkgs }: {
  deps = [
    pkgs.nodejs-18_x
    pkgs.nodePackages.npm
    pkgs.nodePackages.typescript
  ];
}
```

## Environment Variables

### Required Secrets (Set in Replit Secrets)
- `REACT_APP_OPENAI_API_KEY` - Your OpenAI API key
- `REACT_APP_ELEVENLABS_API_KEY` - Your ElevenLabs API key

### Optional Configuration
- `NODE_ENV=production`
- `REACT_APP_ENVIRONMENT=production`
- `REACT_APP_APP_NAME=AI Coaching Conversation`
- `REACT_APP_ENABLE_VOICE_MODE=true`
- `REACT_APP_ENABLE_TEXT_MODE=true`
- `REACT_APP_ENABLE_DEMO_MODE=false`

## Getting API Keys

### OpenAI API Key
1. Go to [OpenAI Platform](https://platform.openai.com/api-keys)
2. Sign in or create an account
3. Click "Create new secret key"
4. Copy the key (starts with "sk-")
5. Add as `REACT_APP_OPENAI_API_KEY` in Replit Secrets

### ElevenLabs API Key
1. Go to [ElevenLabs](https://elevenlabs.io/)
2. Sign in or create an account
3. Go to Profile → API Key
4. Copy your API key
5. Add as `REACT_APP_ELEVENLABS_API_KEY` in Replit Secrets

## Replit Features

### Always-On (Paid Feature)
- **Free Tier**: Repl sleeps after 1 hour of inactivity
- **Paid Tier**: Keep your app running 24/7
- **Upgrade**: Go to Replit → Settings → Billing

### Custom Domain (Paid Feature)
1. **Go to your Repl settings**
2. **Click "Custom Domain"**
3. **Add your domain**
4. **Configure DNS** to point to Replit

### Collaboration
- **Share your Repl** with others
- **Real-time collaboration** on code
- **Comments and suggestions**

## Troubleshooting

### Common Issues

1. **Build Failures**
   - Check Node.js version compatibility
   - Verify all dependencies are installed
   - Check console for specific errors

2. **Environment Variables Not Working**
   - Ensure variables are set in Secrets tab
   - Check variable names (case-sensitive)
   - Restart the Repl after adding secrets

3. **API Errors**
   - Verify API keys are correct
   - Check API key permissions
   - Test API endpoints separately

4. **Port Issues**
   - Replit automatically assigns ports
   - Use `process.env.PORT` in your server
   - Check Replit's port configuration

### Debug Commands

```bash
# Check Node.js version
node --version

# Check npm version
npm --version

# Install dependencies
npm install

# Build the app
npm run build

# Start the server
npm start

# Run in development mode
npm run dev
```

## Performance Optimization

### Build Optimization
- **Production builds** are optimized and minified
- **Static assets** are cached
- **Code splitting** reduces bundle size

### Replit Resources
- **Free Tier**: 500MB RAM, 0.5 CPU cores
- **Paid Tier**: More resources available
- **Monitor usage** in Replit dashboard

## Security Considerations

### API Keys
- **Never commit** API keys to code
- **Use Replit Secrets** for sensitive data
- **Rotate keys** regularly

### HTTPS
- **Replit provides** automatic HTTPS
- **SSL certificates** are included
- **Secure connections** by default

## Deployment Options

### Option 1: Replit Hosting (Recommended)
- **Free hosting** with Replit
- **Automatic HTTPS**
- **Easy deployment**
- **Built-in collaboration**

### Option 2: Export to Other Platforms
- **Export to GitHub** for other deployments
- **Download as ZIP** for manual deployment
- **Deploy to Vercel/Netlify** from Replit

## Monitoring and Analytics

### Replit Analytics
- **View usage statistics**
- **Monitor performance**
- **Track errors**

### Application Monitoring
- **Health check endpoint**: `/health`
- **Error logging** in console
- **Performance metrics**

## Cost Optimization

### Free Tier Limits
- **1 hour** of uptime per day
- **500MB RAM**
- **0.5 CPU cores**
- **Public Repls**

### Paid Plans
- **Always-on** instances
- **More resources**
- **Private Repls**
- **Custom domains**

## Support

- **Replit Documentation**: https://docs.replit.com
- **Community Support**: https://replit.com/community
- **Discord**: https://replit.com/discord

## Next Steps

After successful deployment:

1. **Test Your App**
   - Verify all features work correctly
   - Test on different devices
   - Check API integrations

2. **Set Up Monitoring**
   - Monitor Replit usage
   - Track application performance
   - Set up alerts

3. **Optimize Performance**
   - Monitor resource usage
   - Optimize build process
   - Implement caching

4. **Scale as Needed**
   - Upgrade to paid plan if needed
   - Consider custom domain
   - Implement CDN if required

## Quick Start Checklist

- [ ] Create Replit account
- [ ] Import WebApp from GitHub
- [ ] Set API keys in Secrets
- [ ] Install dependencies
- [ ] Run the application
- [ ] Test text conversation
- [ ] Test voice conversation
- [ ] Verify API key validation
- [ ] Share your Repl (optional)
- [ ] Set up custom domain (optional)

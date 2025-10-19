# Render.com Deployment Guide

This guide will help you deploy the AI Coaching Conversation Web App to Render.com.

## Prerequisites

- GitHub repository with the WebApp code
- Render.com account (free tier available)
- OpenAI API key
- ElevenLabs API key

## Deployment Steps

### 1. Prepare Your Repository

Ensure your WebApp folder contains all the necessary files:
- `package.json` with proper scripts
- `render.yaml` configuration file
- `env.example` for environment variables
- `server.js` for production serving

### 2. Create a Render.com Account

1. Go to [render.com](https://render.com)
2. Sign up with your GitHub account
3. Connect your GitHub repository

### 3. Deploy as Static Site (Recommended)

#### Option A: Using Render Dashboard

1. **Create New Static Site**
   - Click "New +" → "Static Site"
   - Connect your GitHub repository
   - Select the repository containing your WebApp

2. **Configure Build Settings**
   - **Root Directory**: `WebApp`
   - **Build Command**: `npm install && npm run build`
   - **Publish Directory**: `build`

3. **Set Environment Variables**
   Go to Environment tab and add:
   ```
   NODE_ENV=production
   REACT_APP_ENVIRONMENT=production
   REACT_APP_APP_NAME=AI Coaching Conversation
   REACT_APP_APP_VERSION=1.0.0
   REACT_APP_ENABLE_VOICE_MODE=true
   REACT_APP_ENABLE_TEXT_MODE=true
   REACT_APP_ENABLE_DEMO_MODE=false
   REACT_APP_ENABLE_API_KEY_VALIDATION=true
   REACT_APP_MAX_MESSAGE_LENGTH=2000
   REACT_APP_ENABLE_CACHING=true
   REACT_APP_CACHE_DURATION=300000
   ```

4. **Deploy**
   - Click "Create Static Site"
   - Wait for build to complete (5-10 minutes)
   - Your app will be available at `https://your-app-name.onrender.com`

#### Option B: Using render.yaml (Infrastructure as Code)

1. **Push render.yaml to Repository**
   - The `render.yaml` file is already configured
   - Push your code to GitHub

2. **Deploy from Blueprint**
   - In Render dashboard, click "New +" → "Blueprint"
   - Select your repository
   - Render will automatically detect and use the `render.yaml` configuration

### 4. Deploy as Web Service (Alternative)

If you prefer a Node.js web service:

1. **Create New Web Service**
   - Click "New +" → "Web Service"
   - Connect your GitHub repository

2. **Configure Settings**
   - **Root Directory**: `WebApp`
   - **Build Command**: `npm install`
   - **Start Command**: `npm run start:prod`
   - **Environment**: `Node`

3. **Set Environment Variables** (same as above)

## Environment Variables

### Required Variables
- `NODE_ENV=production`
- `REACT_APP_ENVIRONMENT=production`

### Optional Configuration Variables
- `REACT_APP_APP_NAME`: Application name
- `REACT_APP_APP_VERSION`: Version number
- `REACT_APP_ENABLE_VOICE_MODE`: Enable voice features (true/false)
- `REACT_APP_ENABLE_TEXT_MODE`: Enable text features (true/false)
- `REACT_APP_ENABLE_DEMO_MODE`: Enable demo mode (true/false)
- `REACT_APP_MAX_MESSAGE_LENGTH`: Maximum message length
- `REACT_APP_ENABLE_CACHING`: Enable caching (true/false)
- `REACT_APP_CACHE_DURATION`: Cache duration in milliseconds

### API Configuration Variables
- `REACT_APP_OPENAI_API_URL`: OpenAI API endpoint
- `REACT_APP_OPENAI_MODEL`: OpenAI model to use
- `REACT_APP_OPENAI_MAX_TOKENS`: Maximum tokens per request
- `REACT_APP_OPENAI_TEMPERATURE`: Response creativity level
- `REACT_APP_ELEVENLABS_API_URL`: ElevenLabs API endpoint

## Custom Domain (Optional)

1. **Add Custom Domain**
   - Go to your service settings
   - Click "Custom Domains"
   - Add your domain name

2. **Configure DNS**
   - Add a CNAME record pointing to your Render URL
   - Or add an A record with Render's IP address

## SSL Certificate

- Render automatically provides SSL certificates
- HTTPS is enabled by default
- No additional configuration needed

## Performance Optimization

### Caching Headers
The `render.yaml` includes optimized caching headers:
- Static assets: 1 year cache
- HTML files: No cache (always fresh)

### Build Optimization
- Production builds are optimized and minified
- Unused code is eliminated
- Assets are compressed

## Monitoring and Logs

1. **View Logs**
   - Go to your service dashboard
   - Click "Logs" tab
   - Monitor build and runtime logs

2. **Metrics**
   - View performance metrics
   - Monitor response times
   - Track error rates

## Troubleshooting

### Common Issues

1. **Build Failures**
   - Check Node.js version compatibility
   - Verify all dependencies are in package.json
   - Review build logs for specific errors

2. **Environment Variables**
   - Ensure all required variables are set
   - Check variable names (case-sensitive)
   - Verify values don't contain special characters

3. **Routing Issues**
   - Ensure React Router is configured correctly
   - Check that all routes return to index.html
   - Verify static file serving configuration

4. **API Issues**
   - Test API endpoints locally first
   - Check CORS settings
   - Verify API keys are valid

### Debug Commands

```bash
# Test build locally
npm run build

# Test production server locally
npm run start:prod

# Check environment variables
echo $NODE_ENV
```

## Security Considerations

1. **API Keys**
   - Never commit API keys to repository
   - Use environment variables for sensitive data
   - Rotate keys regularly

2. **HTTPS**
   - Always use HTTPS in production
   - Render provides automatic SSL

3. **Headers**
   - Security headers are configured in render.yaml
   - X-Frame-Options, CSP, etc.

## Cost Optimization

### Free Tier Limits
- 750 hours per month
- Automatic sleep after 15 minutes of inactivity
- Cold start delay (~30 seconds)

### Paid Plans
- Always-on instances
- Custom domains
- Priority support
- Higher resource limits

## Support

- **Render Documentation**: https://render.com/docs
- **Community Support**: https://community.render.com
- **Status Page**: https://status.render.com

## Next Steps

After successful deployment:

1. **Test Your App**
   - Verify all features work correctly
   - Test on different devices and browsers
   - Check API integrations

2. **Set Up Monitoring**
   - Configure error tracking
   - Set up performance monitoring
   - Create alerts for downtime

3. **Optimize Performance**
   - Monitor Core Web Vitals
   - Optimize images and assets
   - Implement caching strategies

4. **Scale as Needed**
   - Monitor usage patterns
   - Upgrade plan if necessary
   - Implement CDN if required

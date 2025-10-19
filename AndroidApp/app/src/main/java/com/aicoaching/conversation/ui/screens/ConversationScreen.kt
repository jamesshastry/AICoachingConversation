package com.aicoaching.conversation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aicoaching.conversation.ui.components.MessageBubble
import com.aicoaching.conversation.ui.viewmodel.ConversationViewModel
import com.aicoaching.conversation.utils.AudioRecorder
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    isVoiceMode: Boolean,
    onBackPressed: () -> Unit,
    viewModel: ConversationViewModel = viewModel()
) {
    val context = LocalContext.current
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isRecording by viewModel.isRecording.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    var textInput by remember { mutableStateOf("") }
    val audioRecorder = remember { AudioRecorder(context) }
    
    // Request microphone permission for voice mode
    val microphonePermissionState = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO
    )
    
    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
    
    // Show error dialog
    error?.let { errorMessage ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = if (isVoiceMode) "Voice Chat" else "Text Chat",
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(painterResource(android.R.drawable.ic_menu_revert), contentDescription = "Back")
                }
            },
            actions = {
                if (messages.isNotEmpty()) {
                    TextButton(onClick = { viewModel.clearMessages() }) {
                        Text("Clear")
                    }
                }
            }
        )
        
        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isVoiceMode = isVoiceMode,
                    isPlaying = isPlaying,
                    onPlayAudio = { /* TODO: Implement audio playback */ }
                )
            }
            
            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Card(
                            modifier = Modifier.padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("AI is thinking...")
                            }
                        }
                    }
                }
            }
        }
        
        // Input Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isVoiceMode) {
                    // Voice input button
                    IconButton(
                        onClick = {
                            if (isRecording) {
                                scope.launch {
                                    audioRecorder.stopRecording().fold(
                                        onSuccess = { audioBase64 ->
                                            viewModel.sendVoiceMessage(audioBase64)
                                        },
                                        onFailure = { /* Handle error */ }
                                    )
                                    viewModel.setRecordingState(false)
                                }
                            } else {
                                if (microphonePermissionState.status.isGranted) {
                                    scope.launch {
                                        audioRecorder.startRecording()
                                        viewModel.setRecordingState(true)
                                    }
                                } else {
                                    microphonePermissionState.launchPermissionRequest()
                                }
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = if (isRecording) painterResource(android.R.drawable.ic_media_pause) else painterResource(android.R.drawable.ic_btn_speak_now),
                            contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                            tint = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = if (isRecording) "Recording... Tap to stop" else "Tap to record",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    // Text input
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Type your message...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                viewModel.sendTextMessage(textInput)
                                textInput = ""
                            }
                        },
                        enabled = textInput.isNotBlank() && !isLoading
                    ) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_send),
                            contentDescription = "Send",
                            tint = if (textInput.isNotBlank() && !isLoading) 
                                MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

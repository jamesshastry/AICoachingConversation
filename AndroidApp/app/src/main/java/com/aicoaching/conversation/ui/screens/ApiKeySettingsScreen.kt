package com.aicoaching.conversation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.aicoaching.conversation.config.DemoConfig
import com.aicoaching.conversation.ui.viewmodel.ConversationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeySettingsScreen(
    onBackPressed: () -> Unit,
    onKeysSaved: (openAIKey: String) -> Unit,
    viewModel: ConversationViewModel
) {
    var openAIKey by remember { mutableStateOf("") }
    var showOpenAIKey by remember { mutableStateOf(false) }
    
    // Load existing API keys
    LaunchedEffect(Unit) {
        if (DemoConfig.ENABLE_DEMO_MODE) {
            openAIKey = DemoConfig.DEMO_OPENAI_KEY
            // Automatically save demo key
            viewModel.setApiKeys(DemoConfig.DEMO_OPENAI_KEY)
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("API Keys") },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(painterResource(android.R.drawable.ic_menu_revert), contentDescription = "Back")
                }
            }
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Configure your OpenAI API key to enable AI coaching conversations with speech-to-speech functionality",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // OpenAI API Key
            OutlinedTextField(
                value = openAIKey,
                onValueChange = { openAIKey = it },
                label = { Text("OpenAI API Key") },
                placeholder = { Text("sk-...") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showOpenAIKey) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showOpenAIKey = !showOpenAIKey }) {
                        Icon(
                            painter = if (showOpenAIKey) painterResource(android.R.drawable.ic_menu_view) else painterResource(android.R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = if (showOpenAIKey) "Hide" else "Show"
                        )
                    }
                }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    if (openAIKey.isNotBlank()) {
                        onKeysSaved(openAIKey)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = openAIKey.isNotBlank()
            ) {
                Text("Save API Key")
            }
            
            Text(
                text = "Your API key is stored securely on your device and is not shared with any third parties.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

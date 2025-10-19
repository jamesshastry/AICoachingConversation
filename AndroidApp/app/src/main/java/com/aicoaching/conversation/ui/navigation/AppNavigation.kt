package com.aicoaching.conversation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aicoaching.conversation.ui.screens.ApiKeySettingsScreen
import com.aicoaching.conversation.ui.screens.ConversationScreen
import com.aicoaching.conversation.ui.screens.ModeSelectionScreen
import com.aicoaching.conversation.ui.viewmodel.ConversationViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: ConversationViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "mode_selection",
        modifier = modifier
    ) {
        composable("mode_selection") {
            ModeSelectionScreen(
                onTextModeSelected = {
                    navController.navigate("conversation/text")
                },
                onVoiceModeSelected = {
                    navController.navigate("conversation/voice")
                },
                onSettingsPressed = {
                    navController.navigate("settings")
                }
            )
        }
        composable("conversation/text") {
            ConversationScreen(
                isVoiceMode = false,
                onBackPressed = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
        composable("conversation/voice") {
            ConversationScreen(
                isVoiceMode = true,
                onBackPressed = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
        composable("settings") {
            ApiKeySettingsScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onKeysSaved = { openAIKey, elevenLabsKey ->
                    viewModel.setApiKeys(openAIKey, elevenLabsKey)
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
    }
}

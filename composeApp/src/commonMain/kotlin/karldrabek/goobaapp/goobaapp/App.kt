package karldrabek.goobaapp.goobaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import karldrabek.goobaapp.goobaapp.backend.networkModule
import karldrabek.goobaapp.goobaapp.state.AppScreen
import karldrabek.goobaapp.goobaapp.state.AppUiState
import karldrabek.goobaapp.goobaapp.state.AppViewModel
import karldrabek.goobaapp.goobaapp.ui.screens.EnterNameScreen
import karldrabek.goobaapp.goobaapp.ui.screens.ErrorScreen
import karldrabek.goobaapp.goobaapp.ui.screens.HistoryScreen
import karldrabek.goobaapp.goobaapp.ui.screens.LoadingScreen
import karldrabek.goobaapp.goobaapp.ui.screens.MainScreen
import karldrabek.goobaapp.goobaapp.ui.screens.SettingsScreen
import karldrabek.goobaapp.goobaapp.ui.theme.GoobaTheme
import org.koin.core.context.startKoin

/**
 * Entry Point for Gooba App
 *
 * @param viewModel handles state for the app as well as local and db storage
 * @param context this is the context used for android apps, specifically pickTime. pass null if on IOS
 */
@Composable
fun App(
    viewModel: AppViewModel,
    context: Any?,
) {
    // Launch coroutines to get all the user and task data when app is first launched
    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
    }

    // Stores const state data
    val state = viewModel.uiState

    GoobaTheme {
        // This box is the background for the app
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        ) {
            when (state) {
                // Loading Screen
                AppUiState.Loading -> {
                    LoadingScreen()
                }

                // Error Screen
                is AppUiState.Error -> {
                    ErrorScreen(
                        errorMessage = state.message,
                        onRetry = viewModel::loadInitialData,
                    )
                }

                // Name Entry Screen
                is AppUiState.NameEntry -> {
                    EnterNameScreen(
                        validName = { viewModel.validName(it) },
                        onSave = {
                            viewModel.registerUser(it)
                        },
                    )
                }

                // User is logged-in and we have pulled data from db
                is AppUiState.Ready -> {
                    when (state.currentScreen) {
                        // Main Menu Screen
                        AppScreen.MAIN_MENU -> {
                            MainScreen(
                                state.currentUser,
                                registerTask = { viewModel.registerTask(it) },
                                updateTask = { viewModel.updateTask(it) },
                                deleteTask = { viewModel.deleteTask(it) },
                                onOpenSettings = { viewModel.goTo(AppScreen.SETTINGS) },
                                onOpenHistory = { viewModel.goTo(AppScreen.HISTORY) },
                                users = state.users,
                            )
                        }

                        // Settings Screen
                        AppScreen.SETTINGS -> {
                            SettingsScreen(
                                state.currentUser,
                                onExit = { viewModel.goTo(AppScreen.MAIN_MENU) },
                                onLogout = { viewModel.logout() },
                                onDeleteUser = { viewModel.deleteCurrentUser() },
                                onSaveClicked = {
                                    viewModel.updateUser(it)
                                },
                            )
                        }

                        // History Screen
                        AppScreen.HISTORY -> {
                            HistoryScreen(state.currentUser) { viewModel.goTo(AppScreen.MAIN_MENU) }
                        }
                    }
                }
            }
        }
    }
}

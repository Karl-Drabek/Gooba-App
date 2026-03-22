package karldrabek.goobaapp.goobaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import karldrabek.goobaapp.goobaapp.ui.EnterNameScreen
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.ui.HistoryScreen
import karldrabek.goobaapp.goobaapp.ui.MainScreen
import karldrabek.goobaapp.goobaapp.ui.SettingsScreen
import karldrabek.goobaapp.goobaapp.ui.theme.GoobaTheme
import kotlinx.datetime.DayOfWeek

/**
 * Entry Point for Gooba App
 *
 */
@Composable
@Preview
fun App() {
    /** Stores state */
    var state by remember { mutableStateOf(AppState.NAME_ENTRY) }
    var user by remember { mutableStateOf<User?>(null) }
    var assignments: Map<User, DayOfWeek> by remember { mutableStateOf(mapOf()) }

    val userSnap = user
    val stateSnap = state

    GoobaTheme {
        /** This box is the background for the app */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
        ) {
            if (stateSnap == AppState.NAME_ENTRY || userSnap == null) {
                EnterNameScreen(
                    onSave = { newUser ->
                        user = newUser
                        state = AppState.MAIN_MENU
                    }
                )
            } else {
                when (stateSnap) {
                    AppState.MAIN_MENU -> MainScreen(
                        userSnap,
                        onOpenSettings = { state = AppState.SETTINGS },
                        onOpenHistory = { state = AppState.HISTORY })

                    AppState.SETTINGS -> SettingsScreen(userSnap) { state = AppState.MAIN_MENU }
                    AppState.HISTORY -> HistoryScreen(userSnap) { state = AppState.MAIN_MENU }
                }
            }
        }
    }
}
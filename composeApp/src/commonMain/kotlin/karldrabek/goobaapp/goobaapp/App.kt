package karldrabek.goobaapp.goobaapp

import androidx.compose.material3.MaterialTheme
import karldrabek.goobaapp.goobaapp.ui.EnterNameScreen
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.ui.HistoryScreen
import karldrabek.goobaapp.goobaapp.ui.MainScreen
import karldrabek.goobaapp.goobaapp.ui.SettingsScreen
import kotlinx.datetime.DayOfWeek

/**
 * Entry Point for Gooba App
 *
 */
@Composable
@Preview
fun App() {
    /** Stores state */
    var state: AppState by remember { mutableStateOf(AppState.NAME_ENTRY) }
    var user: User? by remember { mutableStateOf(null) }
    var assignments: Map<User, DayOfWeek> by remember { mutableStateOf(mapOf<User, DayOfWeek>()) }

    val userSnap = user
    val stateSnap = state

    MaterialTheme {
        if (stateSnap == AppState.NAME_ENTRY || userSnap == null) {
            EnterNameScreen(
                onSave = { newUser ->
                    user = newUser
                    state = AppState.MAIN_MENU
                }
            )
        } else {
            when (stateSnap) {
                AppState.MAIN_MENU -> MainScreen(userSnap,
                    onOpenSettings = { state = AppState.SETTINGS },
                    onOpenHistory = { state = AppState.HISTORY })
                AppState.SETTINGS -> SettingsScreen(userSnap) { state = AppState.MAIN_MENU }
                AppState.HISTORY -> HistoryScreen(userSnap) { state = AppState.MAIN_MENU }
            }
        }
    }
}
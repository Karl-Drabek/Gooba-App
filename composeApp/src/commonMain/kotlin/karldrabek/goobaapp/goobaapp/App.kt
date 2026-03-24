package karldrabek.goobaapp.goobaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import karldrabek.goobaapp.goobaapp.ui.screens.EnterNameScreen
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.ui.screens.HistoryScreen
import karldrabek.goobaapp.goobaapp.ui.screens.MainScreen
import karldrabek.goobaapp.goobaapp.ui.screens.SettingsScreen
import karldrabek.goobaapp.goobaapp.ui.theme.GoobaTheme
import kotlinx.datetime.DayOfWeek
import org.koin.core.context.startKoin

/**
 * Entry Point for Gooba App
 *
 * @param context this is the context used for android apps, specifically pickTime. pass null if on IOS
 */
@Composable
@Preview
fun App() {
    /** Module Setup */
    initKoin()

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

                    AppState.SETTINGS -> SettingsScreen(
                        userSnap,
                        onExit = { state = AppState.MAIN_MENU },
                        onSaveClicked = {
                            user = it;
                            state = AppState.MAIN_MENU
                        })
                    AppState.HISTORY -> HistoryScreen(userSnap) { state = AppState.MAIN_MENU }
                }
            }
        }
    }
}

fun initKoin() {
    startKoin {
        modules(networkModule)
    }
}
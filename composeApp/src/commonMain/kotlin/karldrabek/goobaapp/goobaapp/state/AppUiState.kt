package karldrabek.goobaapp.goobaapp.state

import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.utils.TaskCompletionDay

/**
 * Stores all the possible state of the app. This is sealed so that based
 * on the type of the app we can make certain assumptions about the fields.
 */
sealed class AppUiState {
    /** Loading screen - no data */
    data object Loading : AppUiState()

    /** error screen - just holds an error message */
    data class Error(
        val message: String,
    ) : AppUiState()

    /** name entry screen - data is loaded from db, but the user is not logged in */
    data class NameEntry(
        val users: List<User>,
        val tasks: TaskCompletionDay,
    ) : AppUiState()

    /** ready screens - any screen where the db data is loading and the user is logged in */
    data class Ready(
        val currentScreen: AppScreen,
        val currentUser: User,
        val users: List<User>,
        val tasks: TaskCompletionDay,
    ) : AppUiState()
}

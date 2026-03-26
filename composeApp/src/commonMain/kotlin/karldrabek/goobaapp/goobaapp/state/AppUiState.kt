package karldrabek.goobaapp.goobaapp.state

import karldrabek.goobaapp.goobaapp.backend.Task
import karldrabek.goobaapp.goobaapp.backend.User

sealed class AppUiState {
    data object Loading : AppUiState()
    data class Error(val message: String) : AppUiState()
    data class NameEntry(
        val users: List<User>,
        val tasks: List<Task>
    ) : AppUiState()
    data class Ready(
        val currentScreen: AppScreen,
        val currentUser: User,
        val users: List<User>,
        val tasks: List<Task>
    ) : AppUiState()
}
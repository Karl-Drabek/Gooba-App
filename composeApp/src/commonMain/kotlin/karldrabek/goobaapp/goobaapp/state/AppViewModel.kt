package karldrabek.goobaapp.goobaapp.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import karldrabek.goobaapp.goobaapp.backend.Task
import karldrabek.goobaapp.goobaapp.backend.TaskRemoteManager
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.backend.UserRemoteManager
import karldrabek.goobaapp.goobaapp.ui.localStorage.SessionStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.collections.plus

/**
 * Stores state for the app and handles function calls to the local storage and to the database
 * functions querying the database launch coroutines and will run in the background
 *
 * @property sessionStorage the local storage for the correct platform, i.e. IOS or Android
 */
class AppViewModel(
    private val sessionStorage: SessionStorage,
) : ViewModel() {
    // This is the bit that holds state for the app.
    // It is private so that it can only be mutated by these functions.
    var uiState by mutableStateOf<AppUiState>(AppUiState.Loading)
        private set

    // Should be called on app launch or whenever you want to reload the data
    // Gets all the users and tasks in the database.
    fun loadInitialData() {
        uiState = AppUiState.Loading

        viewModelScope.launch {
            try {
                coroutineScope {
                    // Call suspends
                    val usersDeferred = async { UserRemoteManager.getAllUsers() }
                    val tasksDeferred = async { TaskRemoteManager.getAllTasks() }

                    // Wait while both are running. this is non-blocking
                    // so ui should still be rendering right now
                    val users = usersDeferred.await()
                    val tasks = tasksDeferred.await()

                    if (users == null) {
                        uiState = AppUiState.Error("Users not found")
                    } else if (tasks == null) {
                        uiState = AppUiState.Error("Tasks not found")
                    } else {
                        // Check if the local userid is stored and choose the user with
                        // that ID. SavedUser is null if the saved userID does not exist
                        // in the database or nothing is saved.
                        val savedUserId = sessionStorage.getSavedUserId()
                        val savedUser = users.find { it.id == savedUserId }
                        if (savedUser == null && savedUserId != null) {
                            sessionStorage.clearSavedUserId()
                        }

                        uiState =
                            // If the user was found then we are logged in
                            if (savedUser != null) {
                                AppUiState.Ready(
                                    currentScreen = AppScreen.MAIN_MENU,
                                    currentUser = savedUser,
                                    users = users,
                                    tasks = tasks,
                                )
                                // Otherwise we need to log in the user
                            } else {
                                AppUiState.NameEntry(
                                    users = users,
                                    tasks = tasks,
                                )
                            }
                    }
                }
            } catch (e: Exception) {
                e.stackTraceToString().lineSequence().forEach { line ->
                    println("STACK_FRAME: $line") // or log.debug(line)
                }
                uiState = AppUiState.Error("Error Loading Data: ${e.message}")
            }
        }
    }

    /**
     * Checks if the name already exists in the set of users
     * CAUTION, this is the local cache of the DB. This does not guarantee
     * that the name is valid. Also remember that the DB is highly mutable
     *
     * @param name the name which will be checked against the set of users
     * @return whether there is a user in the db with the same name
     */
    fun validName(name: String): Boolean =
        when (val state = uiState) {
            is AppUiState.Ready -> {
                !state.users.any { user -> user.name == name }
            }

            is AppUiState.NameEntry -> {
                !state.users.any { user -> user.name == name }
            }

            else -> {
                false
            }
        }

    /**
     * Registers a new user in the database. If the user has the same name as another,
     * they will only be differentiated by their userID. If this fails it will enter the error
     * state. If it was successfully it will switch to the main screen and set the
     * current user to the newly registered user. this should usually be called from the
     * [EnterNameScreen][karldrabek.goobaapp.goobaapp.ui.screens.EnterNameScreen].
     *
     * @param user the user which will be registered
     */
    fun registerUser(user: User) {
        viewModelScope.launch {
            val state = uiState
            try {
                val newUser = UserRemoteManager.registerUser(user)

                if (newUser == null) {
                    uiState = AppUiState.Error("Failed to create user")
                    return@launch
                }

                sessionStorage.saveUserId(newUser.id)

                when (state) {
                    is AppUiState.NameEntry -> {
                        uiState =
                            AppUiState.Ready(
                                currentScreen = AppScreen.MAIN_MENU,
                                currentUser = newUser,
                                users = state.users + newUser,
                                tasks = state.tasks,
                            )
                    }

                    // for robustness. Not actionable in our app right now.
                    is AppUiState.Ready -> {
                        uiState =
                            state.copy(
                                currentUser = newUser,
                                currentScreen = AppScreen.MAIN_MENU,
                                users = state.users + newUser,
                            )
                    }

                    else -> {
                        uiState = AppUiState.Error("Unexpected App state for register user: $state")
                    }
                }
            } catch (e: Exception) {
                uiState = AppUiState.Error("Failed to register user: ${e.message}")
            }
        }
    }

    /**
     * Change screens to from one screen to another in the ready state
     *
     * @param newScreen The AppScreen to switch to
     */
    fun goTo(newScreen: AppScreen) {
        val state = uiState
        if (state is AppUiState.Ready) {
            uiState =
                state.copy(
                    currentScreen = newScreen,
                )
        }
    }

    /**
     * Updates a task in the database to a new value from any screen. Can switch to the error screen.
     *
     *
     * @param task the new task which will replace the old one. The old task is found be the date and type of the new task.
     */
    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                val successful = TaskRemoteManager.updateTask(task)

                val state = uiState

                if (!successful) {
                    uiState = AppUiState.Error("Failed to update task")
                } else if (state is AppUiState.Ready) {
                    uiState =
                        state.copy(
                            tasks =
                                state.tasks.map { oldTask ->
                                    if (oldTask.date == task.date && oldTask.type == task.type) task else oldTask
                                },
                        )
                }
            } catch (e: Exception) {
                uiState = AppUiState.Error("Failed to update task: ${e.message}")
            }
        }
    }

    /**
     * Deletes a task in the database. Can switch to the error screen.
     *
     * @param task the task to be deleted.
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                val successful = TaskRemoteManager.removeTask(task.type, task.date)

                val state = uiState

                if (!successful) {
                    uiState = AppUiState.Error("Failed to delete task")
                } else if (state is AppUiState.Ready) {
                    uiState = state.copy(tasks = state.tasks - task)
                }
            } catch (e: Exception) {
                uiState = AppUiState.Error("Failed to delete task: ${e.message}")
            }
        }
    }

    /**
     * Adds a new task to the database.
     * Make sure that a task with the same date and time does not already exist in the database.
     * Can switch to the error screen.
     *
     * @param task the task to be registered.
     */
    fun registerTask(task: Task) {
        viewModelScope.launch {
            try {
                val successful = TaskRemoteManager.addTask(task)

                val state = uiState

                if (!successful) {
                    uiState = AppUiState.Error("Failed to add task")
                } else if (state is AppUiState.Ready) {
                    uiState = state.copy(tasks = state.tasks + task)
                }
            } catch (e: Exception) {
                uiState = AppUiState.Error("Failed to add task: ${e.message}")
            }
        }
    }

    /**
     * updates a user to have new values in the database.
     *
     * @param newUser The new value that the user should have in the database. The old user is found via the userID which cannot be changed.
     */
    fun updateUser(newUser: User) {
        viewModelScope.launch {
            val state = uiState
            try {
                val newUser = UserRemoteManager.updateUser(newUser)

                if (newUser == null) {
                    uiState = AppUiState.Error("Failed to update user")
                } else if (state is AppUiState.Ready) {
                    uiState =
                        state.copy(
                            currentUser = newUser,
                            users =
                                state.users.map { user ->
                                    if (user.id == newUser.id) newUser else user
                                },
                        )
                }
            } catch (e: Exception) {
                uiState = AppUiState.Error("Failed to update user: ${e.message}")
            }
        }
    }

    /**
     * Selects a user to be logged in as without creating a new user in the database
     * this should be used to log into an existing account.
     *
     * @param user the user that will be logged in as.
     */
    fun selectUser(user: User) {
        sessionStorage.saveUserId(user.id)

        val state = uiState
        if (state is AppUiState.Ready) {
            uiState =
                state.copy(
                    currentUser = user,
                    currentScreen = AppScreen.MAIN_MENU,
                )
        } else if (state is AppUiState.NameEntry) {
            uiState =
                AppUiState.Ready(
                    currentUser = user,
                    currentScreen = AppScreen.MAIN_MENU,
                    users = state.users,
                    tasks = state.tasks,
                )
        }
    }

    /**
     * logs the user out of the local storage and returns them to the main screen.
     */
    fun logout() {
        sessionStorage.clearSavedUserId()

        val state = uiState
        if (state is AppUiState.Ready) {
            uiState =
                AppUiState.NameEntry(
                    users = state.users,
                    tasks = state.tasks,
                )
        }
    }

    /**
     * deletes the currently logged-in user from the database.
     * This calls [loadInitialData] after it has deleted the user or,
     * it will enter and error screen
     */
    fun deleteCurrentUser() {
        viewModelScope.launch {
            try {
                when (val state = uiState) {
                    is AppUiState.Ready -> {
                        UserRemoteManager.deleteUser(state.currentUser.id)
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                uiState = AppUiState.Error("Failed to update user: ${e.message}")
            }
        }
        loadInitialData()
    }
}

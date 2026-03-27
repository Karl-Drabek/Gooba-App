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

class AppViewModel(
    private val sessionStorage: SessionStorage,
) : ViewModel() {
    var uiState by mutableStateOf<AppUiState>(AppUiState.Loading)
        private set

    fun loadInitialData() {
        viewModelScope.launch {
            try {
                coroutineScope {
                    val usersDeferred = async { UserRemoteManager.getAllUsers() }
                    val tasksDeferred = async { TaskRemoteManager.getAllTasks() }

                    val users = usersDeferred.await()
                    val tasks = tasksDeferred.await()

                    if (users == null) {
                        uiState = AppUiState.Error("Users not found")
                    } else if (tasks == null) {
                        uiState = AppUiState.Error("Tasks not found")
                    } else {
                        val savedUserId = sessionStorage.getSavedUserId()

                        val savedUser = users.find { it.id == savedUserId }

                        if (savedUser == null && savedUserId != null) {
                            sessionStorage.clearSavedUserId()
                        }

                        uiState =
                            if (savedUser != null) {
                                AppUiState.Ready(
                                    currentScreen = AppScreen.MAIN_MENU,
                                    currentUser = savedUser,
                                    users = users,
                                    tasks = tasks,
                                )
                            } else {
                                AppUiState.NameEntry(
                                    users = users,
                                    tasks = tasks,
                                )
                            }
                    }
                }
            } catch (e: Exception) {
                uiState = AppUiState.Error("Error Loading Data: ${e.message}")
            }
        }
    }

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

    fun goTo(newScreen: AppScreen) {
        val state = uiState
        if (state is AppUiState.Ready) {
            uiState =
                state.copy(
                    currentScreen = newScreen,
                )
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            val state = uiState
            try {
                val successful = TaskRemoteManager.updateTask(task)

                if (!successful) {
                    uiState = AppUiState.Error("Failed to update task")
                }
            } catch (e: Exception) {
                uiState = AppUiState.Error("Failed to update task: ${e.message}")
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val state = uiState
            try {
                val successful = TaskRemoteManager.removeTask(task.type, task.date)

                if (!successful) {
                    uiState = AppUiState.Error("Failed to delete task")
                }
            } catch (e: Exception) {
                uiState = AppUiState.Error("Failed to delete task: ${e.message}")
            }
        }
    }

    fun registerTask(task: Task) {
        viewModelScope.launch {
            val state = uiState
            try {
                val successful = TaskRemoteManager.addTask(task)

                if (!successful) {
                    uiState = AppUiState.Error("Failed to add task")
                }
            } catch (e: Exception) {
                uiState = AppUiState.Error("Failed to add task: ${e.message}")
            }
        }
    }

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
                        )
                }
            } catch (e: Exception) {
                uiState = AppUiState.Error("Failed to update user: ${e.message}")
            }
        }
    }

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
}

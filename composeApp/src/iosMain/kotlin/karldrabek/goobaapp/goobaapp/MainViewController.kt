package karldrabek.goobaapp.goobaapp

import androidx.compose.ui.window.ComposeUIViewController
import karldrabek.goobaapp.goobaapp.localStorage.IosSessionStorage
import karldrabek.goobaapp.goobaapp.state.AppViewModel

/** Important for TimePicker */
fun MainViewController() = ComposeUIViewController {
    val sessionStorage = IosSessionStorage()
    val viewModel = AppViewModel(sessionStorage = sessionStorage)

    App(viewModel = viewModel, null)
}
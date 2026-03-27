package karldrabek.goobaapp.goobaapp

import androidx.compose.ui.window.ComposeUIViewController
import karldrabek.goobaapp.goobaapp.localStorage.IosSessionStorage
import karldrabek.goobaapp.goobaapp.state.AppViewModel

/**
 * Entry point for IOS applications
 *
 * Important to set Content for TimePicker and setup session storage
 */
fun MainViewController() =
    ComposeUIViewController {
        initKoin()
        val sessionStorage = IosSessionStorage()
        val viewModel = AppViewModel(sessionStorage = sessionStorage)

        App(viewModel = viewModel, null)
    }

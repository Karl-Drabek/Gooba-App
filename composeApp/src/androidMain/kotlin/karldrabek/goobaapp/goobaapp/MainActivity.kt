package karldrabek.goobaapp.goobaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import karldrabek.goobaapp.goobaapp.localStorage.AndroidSessionStorage
import karldrabek.goobaapp.goobaapp.state.AppViewModel

/**
 * Entry point for Android applications
 *
 * Important to set Content for TimePicker
 */
class MainActivity : ComponentActivity() {
    /**
     * called at the start of the session and launches the common app code
     *
     * @param savedInstanceState idk tbh. seems important for super?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        /** setup local storage for android */
        val sessionStorage =
            AndroidSessionStorage(
                getSharedPreferences("gooba_session", MODE_PRIVATE),
            )

        /** pass our android model for session storage into the viewmodel */
        val viewModel = AppViewModel(sessionStorage)

        setContent {
            App(viewModel, this)
        }
    }
}

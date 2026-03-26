package karldrabek.goobaapp.goobaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import karldrabek.goobaapp.goobaapp.state.AppViewModel
import karldrabek.goobaapp.goobaapp.localStorage.AndroidSessionStorage

/** important to set Content for TimePicker */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val sessionStorage = AndroidSessionStorage(
            getSharedPreferences("gooba_session", MODE_PRIVATE)
        )

        val viewModel = AppViewModel(sessionStorage)

        setContent {
            App(viewModel, this)
        }
    }
}
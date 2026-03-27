package karldrabek.goobaapp.goobaapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorScreen(errorMessage: String) {
    // TODO, make look pretty
    Text(text = errorMessage)
}

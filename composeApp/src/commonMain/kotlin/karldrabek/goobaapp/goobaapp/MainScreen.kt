package karldrabek.goobaapp.goobaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun EnterMainScreen() {
    Box {
        // Takes size of its parent Box
        Spacer(
            Modifier
                .matchParentSize()
                .background(Color.LightGray)
        )
        Text("test")
        Text("Hello")
    }
}
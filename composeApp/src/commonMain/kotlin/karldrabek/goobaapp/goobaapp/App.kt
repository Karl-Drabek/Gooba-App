package karldrabek.goobaapp.goobaapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun App() {
    MaterialTheme {
        val greeting = remember { Greeting().greet() }

        Column(
            modifier = Modifier
                .padding(all = 10.dp)
                .safeContentPadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            greeting.forEach { greeting ->
                Text(greeting)
                HorizontalDivider()
            }
        }
    }
}
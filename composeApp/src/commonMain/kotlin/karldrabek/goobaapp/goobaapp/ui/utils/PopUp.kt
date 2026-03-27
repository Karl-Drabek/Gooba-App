package karldrabek.goobaapp.goobaapp.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Creates a pop-up in the center of the screen which blocks the rest of the screen
 * with an alpha effect
 *
 * @param content the content to be displayed.
 */
@Composable
fun PopUp(content: @Composable () -> Unit) {
    Box(
        // modifier fills the whole screen with an alpha so everything else appears darker
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .padding(24.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                // do nothing to eat up clicks on other buttons behind alpha
            },
        contentAlignment = Alignment.Center,
    ) {
        // border for popup
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            // Display flat with canvas
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            // pads elements away from edge of the card
            Box(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

package karldrabek.goobaapp.goobaapp.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.ui.theme.PrimaryPurple

/**
 * A confirmation popup for confirming important decisions.
 *
 * @param title title shown at the top of the popup
 * @param message explanatory text for the action
 * @param confirmText text on the confirm button
 * @param onCancel called when the user cancels
 * @param onConfirm called when the user confirms
 */
@Composable
fun ConfirmationPopUp(
    title: String,
    message: String,
    confirmText: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    PopUp {
        // Display contents top the bottom
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )

            // Message
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
            )

            // Display the confirm and cancel buttons side by side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Cancel button
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Cancel")
                }

                // confirm button
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple,
                        ),
                ) {
                    Text(confirmText)
                }
            }
        }
    }
}

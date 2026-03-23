package karldrabek.goobaapp.goobaapp.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import karldrabek.goobaapp.goobaapp.ui.theme.InputBackground
import karldrabek.goobaapp.goobaapp.ui.theme.MutedText
import karldrabek.goobaapp.goobaapp.ui.theme.PrimaryPurple


@Composable
fun NameEntryBox(name: String, onValueChanged: (String) -> Unit)  {
    OutlinedTextField(
        value = name,
        onValueChange = onValueChanged,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true, /** force one line even on enter */
        placeholder = {
            Text("Your name")
        },
        /** By default, capitalize the first letter of each word */
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words
        ),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = InputBackground,
            unfocusedBorderColor = InputBackground,
            focusedContainerColor = InputBackground,
            unfocusedContainerColor = InputBackground,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedPlaceholderColor = MutedText,
            unfocusedPlaceholderColor = MutedText,
            cursorColor = PrimaryPurple
        )
    )
}
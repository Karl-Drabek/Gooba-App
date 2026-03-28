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
import karldrabek.goobaapp.goobaapp.ui.theme.DarkText
import karldrabek.goobaapp.goobaapp.ui.theme.InputBackground
import karldrabek.goobaapp.goobaapp.ui.theme.MutedText
import karldrabek.goobaapp.goobaapp.ui.theme.PrimaryPurple

/**
 * Name Entry box allows someone to enter their name. used in enter name screen and no longer used for editing tasks
 *
 * @param text the default name for the box
 * @param onValueChanged call when the name value is changed. you should remember name locally and change it here so that when you call this again it will be updated
 */
@Composable
fun TextEntryBox(
    text: String,
    onValueChanged: (String) -> Unit,
    defaultValue: String,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChanged,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true, // force one line even on enter
        placeholder = { Text(defaultValue) },
        // By default, capitalize the first letter of each word
        keyboardOptions =
            KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
            ),
        shape = MaterialTheme.shapes.medium,
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = InputBackground,
                unfocusedBorderColor = InputBackground,
                focusedContainerColor = InputBackground,
                unfocusedContainerColor = InputBackground,
                focusedTextColor = DarkText,
                unfocusedTextColor = DarkText,
                focusedPlaceholderColor = MutedText,
                unfocusedPlaceholderColor = MutedText,
                cursorColor = PrimaryPurple,
            ),
    )
}

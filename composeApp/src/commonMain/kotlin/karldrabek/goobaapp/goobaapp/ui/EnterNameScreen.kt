package karldrabek.goobaapp.goobaapp.ui

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import karldrabek.goobaapp.goobaapp.backend.User
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.backend.registerName

/**
 * EnterNameScreen handles the name entry for the first time that
 * the user enters the application
 *
 *  @property onSave called to save a particular username for the user.
 */
@Composable
fun EnterNameScreen(onSave: (User) -> Unit) {
    /** Save whether the user entered and existing name, and
      * the current name in the input box */
    var name by remember { mutableStateOf("") }
    var existingName by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        /** Input box */
        TextField(
            value = name,
            onValueChange = {
                name = it
                existingName = false
            },
            label = { Text("Enter your name") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        /** Save button */
        Button(
            onClick = {
                if (registerName(name)) {
                    onSave(User(name))
                } else {
                    if (existingName) {
                        onSave(User(name))
                    } else {
                        existingName = true
                    }
                }
            },
            enabled = name.trim().isNotEmpty()
        ) {
            Text(if (existingName) "Use existing name" else "Save")
        }
    }
}
package karldrabek.goobaapp.goobaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.backend.UserRemoteManager.registerUser
import karldrabek.goobaapp.goobaapp.ui.theme.ButtonDisabled
import karldrabek.goobaapp.goobaapp.ui.theme.InputBackground
import karldrabek.goobaapp.goobaapp.ui.theme.MutedText
import karldrabek.goobaapp.goobaapp.ui.theme.PrimaryPurple
import kotlinx.coroutines.launch

import karldrabek.goobaapp.goobaapp.ui.utils.NameEntryBox

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
    val scope = rememberCoroutineScope()

    /** background */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        /** canvas element containing all the enter name info */
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 680.dp), /** set max size */
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface /** white */
            ),
            /** Cards are raised by default with a shadow, so we remove it */
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                /** purple circle behind pet icon */
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(
                            color = PrimaryPurple.copy(alpha = 0.12f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    /** paw icon, make sure to refresh Gradle */
                    Icon(
                        imageVector = Icons.Outlined.Pets,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Text(
                    text = "Welcome to Gooba Tracker",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Please enter your name to continue",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MutedText
                )

                NameEntryBox(name){
                    name = it
                    existingName = false
                }

                Button(
                    onClick = {
                        val trimmed = name.trim()
                        if (trimmed.isEmpty()) return@Button /** labeled return exits  button lambda */

                        // TODO -> LUKE FUCKED THIS UP FR !!

//                        if (false) {
//                            onSave(User(trimmed))
//                        } else if (existingName) {
//                            onSave(User(trimmed))
//                        } else {
//                            existingName = true
//                        }

                        // The scope is a mutex, db requires Coroutines which are threads
                        scope.launch {
                            // Adds a new user, returns the existing user if it exists
                            val registeredUser = registerUser(User(name = trimmed))
                            if(registeredUser != null) {
                                onSave(registeredUser)
                            } else {
                                existingName = true
                            }
                        }

                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonDisabled,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = name.trim().isNotEmpty()
                ) {
                    Text(
                        text = if (existingName) "Use existing name" else "Continue",
                        modifier = Modifier.padding(vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
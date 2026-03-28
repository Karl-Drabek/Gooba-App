package karldrabek.goobaapp.goobaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.ui.theme.ButtonDisabled
import karldrabek.goobaapp.goobaapp.ui.theme.MutedText
import karldrabek.goobaapp.goobaapp.ui.theme.PrimaryPurple
import karldrabek.goobaapp.goobaapp.ui.utils.PopUp
import karldrabek.goobaapp.goobaapp.ui.utils.TextEntryBox

/**
 * EnterNameScreen handles the name entry for the first time that
 * the user enters the application
 *
 *  @param onSave called to save a particular username for the user.
 *  @param users a list of all the users in the database
 *  @param selectUser like on save but for when the user just wants to log into an existing account without creating a new user in the database.
 *  @param validName used to check if a name already exists in the database. This doesn't need to handle empty names or trimming.
 */
@Composable
fun EnterNameScreen(
    onSave: (User) -> Unit,
    users: List<User>,
    selectUser: (User) -> Unit,
    validName: (String) -> Boolean,
) {
    // Save whether the user entered and existing name, and
    // the current name in the input box
    var name by remember { mutableStateOf("") }
    var duplicateName by remember { mutableStateOf(false) }

    // background
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        // canvas element containing all the enter name info
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
            // set max size
            shape = MaterialTheme.shapes.large,
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface, // white
                ),
            // Cards are raised by default with a shadow, so we remove it
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                // purple circle behind pet icon
                Box(
                    modifier =
                        Modifier
                            .size(96.dp)
                            .background(
                                color = PrimaryPurple.copy(alpha = 0.12f),
                                shape = CircleShape,
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    // paw icon, make sure to refresh Gradle
                    Icon(
                        imageVector = Icons.Outlined.Pets,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(42.dp),
                    )
                }

                // Welcome text
                Text(
                    text = "Welcome to Gooba Tracker",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                // Name entry prompt
                Text(
                    text = "Please enter your name to continue",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MutedText,
                )

                // Place for user to enter their name
                TextEntryBox(
                    text = name,
                    defaultValue = "Enter name",
                    onValueChanged = {
                        name = it
                    },
                )

                Button(
                    onClick = {
                        if (validName(name)) {
                            onSave(User(name))
                        } else {
                            duplicateName = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = ButtonDisabled,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    enabled = name.trim().isNotEmpty(),
                ) {
                    Text(
                        text = if (validName(name)) "Continue" else "Name already exists",
                        modifier = Modifier.padding(vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

        // Display a pop-up if the username already exists
        if (duplicateName) {
            DuplicateNamePopUp(
                name = name,
                onCancel = { duplicateName = false },
                onLogin = { idStr: String ->

                    // check if the input was a valid integer for the userId
                    val id: Int
                    try {
                        id = idStr.toInt()
                    } catch (e: NumberFormatException) {
                        return@DuplicateNamePopUp false
                    }

                    val user = users.find({ user -> user.id == id })

                    // Check if a use by the given id actually exists
                    if (user != null) {
                        selectUser(user)
                        return@DuplicateNamePopUp true
                    } else {
                        return@DuplicateNamePopUp false
                    }
                },
            )
        }
    }
}

/**
 * Creates a pop-up when the user has entered a name that already exists in the database
 * this allows the user to cancel, or enter the userid of an account they wish to log
 * in to with that given name.
 *
 * @param name the name which already existed
 * @param onCancel called when the user decides to cancel
 * @param onLogin called when the user has entered an ID and wishes to log into the account associated with the name and ID
 */
@Composable
fun DuplicateNamePopUp(
    name: String,
    onCancel: () -> Unit,
    onLogin: (String) -> Boolean,
) {
    var id by remember { mutableStateOf("") }
    var invalidId by remember { mutableStateOf<String?>(null) }

    PopUp {
        // Top to bottom
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Header
            Text(
                text = "Username already exists",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )

            // Subtitle
            Text(
                text = "To log in as \"$name\", enter your user ID from the settings screen.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )

            // Error message displayed if the ID is not valid for the name
            if (invalidId != null) {
                Text(
                    text = "No user named \"$name\" exists with user ID $invalidId.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }

            // Entry box for the user to enter their name
            TextEntryBox(
                text = id,
                defaultValue = "Enter id",
                onValueChanged = {
                    id = it
                    invalidId = null
                },
            )

            // Display two buttons from left to right
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

                // Log-In button
                Button(
                    onClick = {
                        val trimmedID = id.trim()
                        if (!onLogin(trimmedID)) {
                            invalidId = trimmedID
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = id.isNotBlank(),
                ) {
                    Text("Log In")
                }
            }
        }
    }
}

package karldrabek.goobaapp.goobaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.ui.theme.InputBackground
import karldrabek.goobaapp.goobaapp.ui.theme.MutedText
import karldrabek.goobaapp.goobaapp.ui.theme.PrimaryPurple

// gooba poop day Luke gay ---> Kearnan GAYER --> Kearnan GAYEST --> doesn't rhyme faggot -> something a gay person would say -> my IDE keeps saying "typo in word Kearnan, did you mean Gaybo?"

// TODO:
// * ensure that only one person is assigned to a particular day
//   - this might involve some server end stuff too
// * comment the code. wait until the first bit is done.

/**
 * loads the setting screen which allows users to change their name and the
 * day they are assigned to scoop poop. users can also log out or delete
 * their account here.
 *
 * @param user the currently logged-in user
 * @param onExit called when the back button is clicked. Returns the user to the main menu. Caution, does not necessarily save.
 * @param onSaveClicked called when the user clicks save. This updates their name and scoop day in the database to potentially new values.
 * @param onLogout called when logout is clicked. logs the user out of the phone and returns to the enter name screen.
 * @param onDeleteUser called when a user is deleted. deletes the user from the database and reload the app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    user: User,
    onExit: () -> Unit,
    onSaveClicked: (User) -> Unit,
    onLogout: () -> Unit,
    onDeleteUser: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var editedUser by remember(user) { mutableStateOf(user) }

    val dayOptions =
        listOf(
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday",
        )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onExit) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                    )
                }

                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }

            SettingsCard(title = "Poop Scoop Day") {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                ) {
                    OutlinedTextField(
                        value = editedUser.scoopDay,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = { TrailingIcon(expanded = expanded) },
                        modifier =
                            Modifier
                                .menuAnchor(PrimaryNotEditable)
                                .fillMaxWidth(),
                        colors = settingsTextFieldColors(),
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        dayOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    editedUser = editedUser.copy(scoopDay = option)
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }

            SettingsCard(title = "Current User") {
                OutlinedTextField(
                    value = editedUser.name,
                    onValueChange = { editedUser = editedUser.copy(name = it) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = {
                        Text("New name")
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                        ),
                    shape = MaterialTheme.shapes.medium,
                    colors = settingsTextFieldColors(),
                )

                Button(
                    onClick = { onSaveClicked(editedUser) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                ) {
                    Text("Save Changes")
                }
            }

            SettingsCard(title = "Account") {
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = null,
                    )
                    Text(
                        text = "Logout",
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }

                Button(
                    onClick = onDeleteUser,
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                    )
                    Text(
                        text = "Delete User",
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            content()
        }
    }
}

@Composable
private fun settingsTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = InputBackground,
        unfocusedBorderColor = InputBackground,
        focusedContainerColor = InputBackground,
        unfocusedContainerColor = InputBackground,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedPlaceholderColor = MutedText,
        unfocusedPlaceholderColor = MutedText,
        cursorColor = PrimaryPurple,
    )

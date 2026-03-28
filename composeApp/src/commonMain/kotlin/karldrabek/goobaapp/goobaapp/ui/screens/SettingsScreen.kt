package karldrabek.goobaapp.goobaapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.ui.theme.CardBackground
import karldrabek.goobaapp.goobaapp.ui.theme.CardBorder
import karldrabek.goobaapp.goobaapp.ui.theme.DarkText
import karldrabek.goobaapp.goobaapp.ui.theme.InputBackground
import karldrabek.goobaapp.goobaapp.ui.theme.MutedText
import karldrabek.goobaapp.goobaapp.ui.theme.PrimaryPurple
import karldrabek.goobaapp.goobaapp.ui.utils.ConfirmationPopUp
import karldrabek.goobaapp.goobaapp.ui.utils.TextEntryBox

/**
 * holds information about whether the user is confirming logging out or deleting or neither.
 */
private enum class ConfirmationType {
    NONE,
    LOGOUT,
    DELETE_ACCOUNT,
}

/**
 * The settings menu has options for the user to change the litter-box days which they are assigned
 * and compare theirs with others', change their name, check their user id, logout or delete their account.
 *
 * @param user the currently logged-in user
 * @param users a list of all users in the database
 * @param onExit called when the user clicks the back button to return to the main menu.
 * @param onSaveClicked called when the user saves new settings for their user. Updates the user with the same id in the database to reflect these changes
 * @param onLogout called when the user logs out of their account. Clears the local storage for the user.
 * @param onDeleteUser called when the user is being deleted. Removes the user from the database.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    user: User,
    users: List<User>,
    onExit: () -> Unit,
    onSaveClicked: (User) -> Unit,
    onLogout: () -> Unit,
    onDeleteUser: () -> Unit,
) {
    // Saves state. Use user for information we want to reflect the database, and editedUser for any changes.
    var editedUser by remember { mutableStateOf(user) }
    var confirmationType by remember { mutableStateOf(ConfirmationType.NONE) }

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

    // Background
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        // Top to bottom
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Left to right
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Back button
                IconButton(onClick = onExit) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkText,
                    )
                }

                // Settings header
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                )
            }

            // Section for litter box day assignments
            SettingsCard(title = "Poop Scoop Day") {
                ScoopDaySelector(
                    days = dayOptions,
                    assignments =
                        users.associateBy(
                            { it.scoopDay },
                            { it },
                        ),
                    user = editedUser,
                    onSelectDay = {
                        editedUser = editedUser.copy(scoopDay = it)
                    },
                )
            }

            // Section for "Current User" settings
            SettingsCard(title = "Current User") {
                // Name title
                Text(
                    text = "Display Name",
                    style = MaterialTheme.typography.labelLarge,
                    color = MutedText,
                )

                // Box to enter a new name
                TextEntryBox(
                    text = editedUser.name,
                    defaultValue = "Cannot be empty",
                    onValueChanged = {
                        editedUser = editedUser.copy(name = it)
                    },
                )

                // Save button for a new name
                Button(
                    onClick = { onSaveClicked(editedUser) },
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple,
                        ),
                ) {
                    Text("Save Changes")
                }
            }

            // Section for "Account" settings
            SettingsCard(title = "Account") {
                // Card for displaying the userID
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = InputBackground),
                    border = BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                ) {
                    // Top to bottom
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        // Section title
                        Text(
                            text = "User ID",
                            style = MaterialTheme.typography.labelLarge,
                            color = MutedText,
                        )

                        // Actual id
                        Text(
                            text = user.id.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkText,
                        )

                        // Description
                        Text(
                            text = "Use this ID to log into this account on another device.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedText,
                        )
                    }
                }

                // Logout and delete buttons for account displayed left to right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Logout button
                    OutlinedButton(
                        onClick = { confirmationType = ConfirmationType.LOGOUT },
                        modifier = Modifier.weight(1f),
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

                    // delete account button
                    Button(
                        onClick = { confirmationType = ConfirmationType.DELETE_ACCOUNT },
                        modifier = Modifier.weight(1f),
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
        // Display a pop-up if the user tries to log out to warn them
        if (confirmationType == ConfirmationType.LOGOUT) {
            ConfirmationPopUp(
                title = "Log out?",
                message =
                    "Are you sure you want to log out of this device? " +
                        "If you wish to log back in again, enter the name: ${user.name} " +
                        "and enter the userID: ${user.id} when prompted",
                confirmText = "Log out",
                onCancel = { confirmationType = ConfirmationType.NONE },
                onConfirm = {
                    confirmationType = ConfirmationType.NONE
                    onLogout()
                },
            )
            // Another warning for if the user tries to delete their account
        } else if (confirmationType == ConfirmationType.DELETE_ACCOUNT) {
            ConfirmationPopUp(
                title = "Delete account?",
                message =
                    "This will permanently delete your account. " +
                        "This action cannot be undone. " +
                        "This will not delete events which you have completed.",
                confirmText = "Delete",
                onCancel = { confirmationType = ConfirmationType.NONE },
                onConfirm = {
                    confirmationType = ConfirmationType.NONE
                    onDeleteUser()
                },
            )
        }
    }
}

/**
 * A set of cards showing for each day which user is assigned to that day. This also allows
 * users to change their assigned day to a callback as long as the day is valid. There is also
 * some nice color coding.
 *
 * @param days list of possible days as strings
 * @param assignments map of assigned days to users who are assigned to those days
 * @param user the user from which to draw the scoop day data
 * @param onSelectDay called when the user clicks on a new day
 */
@Composable
fun ScoopDaySelector(
    days: List<String>,
    assignments: Map<String, User?>,
    user: User,
    onSelectDay: (String) -> Unit,
) {
    // Top the bottom
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Display one of these elements per day one on top of the other
        days.forEach { day ->
            // These three are exclusive and exhaustive
            val assignedUser = assignments[day]
            val isSelected = user.scoopDay == day
            val isTaken = assignedUser != null && assignedUser.id != user.id

            val backgroundColor =
                when {
                    isSelected -> PrimaryPurple.copy(alpha = 0.15f)
                    isTaken -> InputBackground
                    else -> CardBackground
                }

            // Make a card with corresponding background color
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        // Ensure that no one else has the day
                        .clickable(enabled = !isTaken) {
                            onSelectDay(day)
                        },
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                border = BorderStroke(1.dp, CardBorder),
            ) {
                // Display from left to right
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Display the name of the day
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkText,
                    )

                    // Show the status of the day's assigment
                    Text(
                        text =
                            when {
                                assignedUser == null -> "Unassigned"
                                assignedUser.id == user.id -> "You"
                                else -> assignedUser.name
                            },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedText,
                    )
                }
            }
        }
    }
}

/**
 * Displays a card with a tittle containing text, and below it some content.
 *
 * @param title displayed above the content on the card
 * @param content displayed on a card below the content. Allows for column scope to adjust weight.
 */
@Composable
private fun SettingsCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    // A smaller white background on which to present the composable
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        // Top to bottom
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = DarkText,
            )

            // users content inside a column
            content()
        }
    }
}

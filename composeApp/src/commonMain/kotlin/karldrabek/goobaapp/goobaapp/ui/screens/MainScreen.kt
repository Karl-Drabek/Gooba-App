package karldrabek.goobaapp.goobaapp.ui.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.ui.theme.MorningBadgeBg
import karldrabek.goobaapp.goobaapp.ui.theme.MorningBadgeFg
import kotlinx.datetime.TimeZone
import karldrabek.goobaapp.goobaapp.utils.timeToText
import karldrabek.goobaapp.goobaapp.ui.theme.EveningBadgeBg
import karldrabek.goobaapp.goobaapp.ui.theme.EveningBadgeFg
import karldrabek.goobaapp.goobaapp.ui.theme.LitterBadgeBg
import karldrabek.goobaapp.goobaapp.ui.theme.LitterBadgeFg
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.window.Dialog
import karldrabek.goobaapp.goobaapp.ui.theme.InputBackground
import karldrabek.goobaapp.goobaapp.ui.theme.MutedText
import karldrabek.goobaapp.goobaapp.ui.theme.PrimaryPurple
import karldrabek.goobaapp.goobaapp.ui.utils.DropDown
import karldrabek.goobaapp.goobaapp.ui.utils.NameEntryBox
import karldrabek.goobaapp.goobaapp.ui.utils.PopUp
import karldrabek.goobaapp.goobaapp.ui.utils.Task
import karldrabek.goobaapp.goobaapp.ui.utils.TimeDropDown
import karldrabek.goobaapp.goobaapp.utils.EventCompletedData
import karldrabek.goobaapp.goobaapp.utils.TimeFormat
import karldrabek.goobaapp.goobaapp.utils.timeToValues
import karldrabek.goobaapp.goobaapp.utils.valuesToTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

/**
 * MainScreen handles the main screen for the gooba app
 * which displays buttons to indicate a user fed Gooba in the
 * morning, the afternoon, or they scooped her poop.
 *
 *  @property user the user currently logged in.
 *  @property onOpenSettings settings button is clicked.
 *  @property onOpenHistory history button is clicked.
 */
@Composable
fun MainScreen(
    user: User,
    onOpenSettings: () -> Unit,
    onOpenHistory: () -> Unit,
) {
    /** State */
    var morningFoodData: EventCompletedData? by remember { mutableStateOf(null) }
    var eveningFoodData: EventCompletedData? by remember { mutableStateOf(null) }
    var poopScoopData: EventCompletedData? by remember { mutableStateOf(null) }
    var editTask: Task? by remember { mutableStateOf(null) }

    val editTaskConst: Task? = editTask

    /** layout elements on top of each other */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        /** header at the top */
        Header(userName = user.toString(), onOpenSettings = onOpenSettings)

        /** Add a card for feeding with buttons for morning and evening */
        TaskSectionCard(
            title = "Feeding Schedule"
        ) {
            /** morning feeding row */
            StatusRow(
                data = morningFoodData,
                pendingLabel = "Feed Breakfast",
                completeTitle = "Morning - Fed ✓",
                badgeBackground = MorningBadgeBg,
                badgeForeground = MorningBadgeFg,
                onEditClick = { editTask = Task.MORNING_FOOD },
                onClick = { morningFoodData = EventCompletedData(user.name, Clock.System.now()) },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.WbSunny,
                        contentDescription = null
                    )
                }
            )

            /** evening feeding row */
            StatusRow(
                data = eveningFoodData,
                pendingLabel = "Feed Dinner",
                completeTitle = "Evening - Fed ✓",
                badgeBackground = EveningBadgeBg,
                badgeForeground = EveningBadgeFg,
                onClick = { eveningFoodData = EventCompletedData(user.name, Clock.System.now()) },
                onEditClick = { editTask = Task.EVENING_FOOD },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Bedtime,
                        contentDescription = null
                    )
                }
            )
        }

        /** Add another card just for the litter box */
        TaskSectionCard(
            title = "Litter box"
        ) {
            /** litter box row */
            StatusRow(
                data = poopScoopData,
                pendingLabel = "Scoop Poop",
                completeTitle = "Litter box - Done ✓",
                badgeBackground = LitterBadgeBg,
                badgeForeground = LitterBadgeFg,
                onClick = { poopScoopData = EventCompletedData(user.name, Clock.System.now()) },
                onEditClick = { editTask = Task.SCOOP_POOP },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = null
                    )
                }
            )
        }
    }

    /** We are editing one of the entries */
    if(editTaskConst != null) {
        /** get the data in question and make sure it won't change so we can
         * verify that it is not null */
        val dataConst = when (editTaskConst) {
            Task.MORNING_FOOD -> morningFoodData
            Task.EVENING_FOOD -> eveningFoodData
            Task.SCOOP_POOP -> poopScoopData
        }

        /** popup that updates data accordingly */
        if(dataConst != null){
            EditPopup(
                data = dataConst,
                onCancel = {
                    editTask = null
                },
                onUpdateData = {
                    when(editTaskConst){
                        Task.MORNING_FOOD -> morningFoodData = it
                        Task.EVENING_FOOD -> eveningFoodData = it
                        Task.SCOOP_POOP -> poopScoopData = it
                    }
                    editTask = null
                }
            )
        }
    }
}


/**
 * Displays a header for the main menu This has the following format:
 * PawIcon - Title -------------------- Settings
 * Date
 * UserNameIntro
 *
 * @property userName the name of the currently logged-in user.
 * @property onOpenSettings the function to be called when the settings are clicked.
 */
@Composable
private fun Header(
    userName: String,
    onOpenSettings: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
    /** layout three elements from right to left */
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            /** nested row for just icon and text
             * this excludes settings to allow for the spacer
             */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                /** paw icon */
                Icon(
                    imageVector = Icons.Outlined.Pets,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(34.dp)
                )

                /** main header text */
                Text(
                    text = "Cat Care Tracker",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            /** push last element to the far right */
            Spacer(modifier = Modifier.weight(1f))

            /** far right is the settings button
             * If there are errors, try refreshing Gradle
             */
            IconButton(onClick = onOpenSettings) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            }
        }

        /** Shows today's date */
        Text(
            text = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        /** displays user's username */
        Text(
            text = "Hello, $userName!",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}


/**
 * Displays a card for task selection with the following format:
 * Title
 * Content
 *
 * @property title title text to be displayed at the top of the card.
 * @property content the composable to be displayed in the card
 */
@Composable
private fun TaskSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    /** Wrap content in a card */
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        /** Display flat with canvas */
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            /** tittle text */
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            /** whatever content we passed to the function */
            content()
        }
    }
}



/**
 * Creates a status row with an icon and content depending on one whether it has been completed.
 * if the even has been completed it shows some text to give information about the completion.
 * otherwise it shows a button to allow the user to complete the task
 *
 * @property data Whether the client is currently editing
 * @property pendingLabel Label for the button when the task has not been completed.
 * @property completeTitle Main text to be displayed when the task is complete.
 * @property badgeBackground color of the circle behind the icon.
 * @property badgeForeground color of the icon.
 * @property onEditClick called when the edit button is clicked.
 * @property onClick called when the complete button is clicked.
 * @property icon composable displayed to the left of the button.
 */
@Composable
private fun StatusRow(
    data: EventCompletedData?,
    pendingLabel: String,
    completeTitle: String,
    badgeBackground: Color,
    badgeForeground: Color,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    /** align things from left to right */
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        /** create a circular element at this point in the row*/
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(badgeBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        /** no one has completed the task so allow user to complete with a button */
        if (data == null) {
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline
                )
            ) {
                /** text displayed on the button */
                Text(
                    text = pendingLabel,
                    modifier = Modifier.padding(vertical = 10.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else { /** task has been completed by someone */
            /** display two pieces of text one on top of the other */
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                /** main text for the task being done */
                Text(
                    text = completeTitle,
                    style = MaterialTheme.typography.titleLarge
                )
                /** subtext for when the user did the task */
                Text(
                    text = "By ${data.name} at ${timeToText(data.time)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            /** Edit data button */
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit"
                )
            }
        }
    }
}

/**
 * Creates a pop-up in the center of the screen which blocks the rest of the screen
 * with an alpha effect
 *
 * @property data the initial data to  be displayed
 * @property onCancel called when the cancel button is clicked.
 * @property onUpdateData called when the save button is clicked or the data is deleted.
 */
@Composable
fun EditPopup(
    data: EventCompletedData,
    onCancel: () -> Unit,
    onUpdateData: (EventCompletedData?) -> Unit,
) {
    /** first->Hour, second->Minute, third->am/pm*/
    val time = timeToValues(data.time)

    /** State */
    var name by remember { mutableStateOf(data.name) }
    var timeFormat by remember { mutableStateOf(time) }

    PopUp(){
        Box {
            Column(
                verticalArrangement = Arrangement.spacedBy(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(Modifier.height(24.dp)) /** space for top icon */

                /** Text entry for the username */
                NameEntryBox(name) {
                    name = it
                }

                /** Dropdown to enter the new time */
                TimeDropDown(timeFormat.hour, timeFormat.minute, timeFormat.amPm) {
                    timeFormat = it
                }

                Spacer(Modifier.height(24.dp)) /** space for bottom icons */
            }

            /** Top-right cancel button */
            IconButton(
                onClick = onCancel,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Outlined.Cancel, contentDescription = "Cancel")
            }

            /** Bottom row buttons */
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onUpdateData(EventCompletedData(name, valuesToTime(timeFormat))) }
                ) {
                    Icon(Icons.Outlined.Save, contentDescription = "Save")
                }

                IconButton(
                    onClick = { onUpdateData(null) }
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

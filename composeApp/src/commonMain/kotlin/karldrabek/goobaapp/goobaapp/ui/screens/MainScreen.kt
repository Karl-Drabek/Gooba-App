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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Save
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.backend.Task
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.ui.theme.EveningBadgeBg
import karldrabek.goobaapp.goobaapp.ui.theme.EveningBadgeFg
import karldrabek.goobaapp.goobaapp.ui.theme.LitterBadgeBg
import karldrabek.goobaapp.goobaapp.ui.theme.LitterBadgeFg
import karldrabek.goobaapp.goobaapp.ui.theme.MorningBadgeBg
import karldrabek.goobaapp.goobaapp.ui.theme.MorningBadgeFg
import karldrabek.goobaapp.goobaapp.ui.utils.DropDown
import karldrabek.goobaapp.goobaapp.ui.utils.PopUp
import karldrabek.goobaapp.goobaapp.ui.utils.TimeDropDown
import karldrabek.goobaapp.goobaapp.utils.EventCompletedData
import karldrabek.goobaapp.goobaapp.utils.TaskCompletionDay
import karldrabek.goobaapp.goobaapp.utils.TaskType
import karldrabek.goobaapp.goobaapp.utils.timeToText
import karldrabek.goobaapp.goobaapp.utils.timeToValues
import karldrabek.goobaapp.goobaapp.utils.valuesToTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

/**
 * MainScreen handles the main screen for the gooba app
 * which displays buttons to indicate a user fed Gooba in the
 * morning, the afternoon, or they scooped her poop.
 *
 *  @param user the user currently logged in.
 *  @param users the list of all users.
 *  @param tasks the task data for the day.
 *  @param registerTask called when a task is marked complete and sends the data to the database.
 *  @param updateTask called when an edit is saved and updates the task in the database.
 *  @param deleteTask called when a task is deleted and deletes the task in the database.
 *  @param onOpenSettings settings button is clicked.
 *  @param onOpenHistory history button is clicked.
 */
@Composable
fun MainScreen(
    user: User,
    users: List<User>,
    tasks: TaskCompletionDay,
    registerTask: (Task) -> Unit,
    updateTask: (Task) -> Unit,
    deleteTask: (Task) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenHistory: () -> Unit,
) {
    // State
    var morningFoodData: EventCompletedData? by remember { mutableStateOf(tasks.morning) }
    var eveningFoodData: EventCompletedData? by remember { mutableStateOf(tasks.evening) }
    var poopScoopData: EventCompletedData? by remember { mutableStateOf(tasks.poop) }
    var editTask: TaskType? by remember { mutableStateOf(null) }

    val editTaskConst: TaskType? = editTask

    // layout elements on top of each other
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(28.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // header at the top
        Header(userName = user.toString(), onOpenSettings = onOpenSettings)

        // Add a card for feeding with buttons for morning and evening
        TaskSectionCard(
            title = "Feeding Schedule",
        ) {
            // morning feeding row
            StatusRow(
                data = morningFoodData,
                pendingLabel = "Feed Breakfast",
                completeTitle = "Morning - Fed ✓",
                badgeBackground = MorningBadgeBg,
                badgeForeground = MorningBadgeFg,
                taskType = TaskType.MORNING_FOOD,
                onClick = { morningFoodData = it },
                registerTask = registerTask,
                onEditClick = { editTask = TaskType.MORNING_FOOD },
                user = user,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.WbSunny,
                        contentDescription = null,
                    )
                },
            )

            // evening feeding row
            StatusRow(
                data = eveningFoodData,
                pendingLabel = "Feed Dinner",
                completeTitle = "Evening - Fed ✓",
                badgeBackground = EveningBadgeBg,
                badgeForeground = EveningBadgeFg,
                taskType = TaskType.EVENING_FOOD,
                onClick = { eveningFoodData = it },
                registerTask = registerTask,
                onEditClick = { editTask = TaskType.EVENING_FOOD },
                user = user,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Bedtime,
                        contentDescription = null,
                    )
                },
            )
        }

        // Add another card just for the litter box
        TaskSectionCard(
            title = "Litter box",
        ) {
            // litter box row
            StatusRow(
                data = poopScoopData,
                pendingLabel = "Scoop Poop",
                completeTitle = "Litter box - Done ✓",
                badgeBackground = LitterBadgeBg,
                badgeForeground = LitterBadgeFg,
                taskType = TaskType.SCOOP_POOP,
                onClick = { poopScoopData = it },
                registerTask = registerTask,
                onEditClick = { editTask = TaskType.SCOOP_POOP },
                user = user,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = null,
                    )
                },
            )
        }
    }

    // We are editing one of the entries
    if (editTaskConst != null) {
        // / get the data in question and make sure it won't change so we can
        // verify that it is not null
        val dataConst =
            when (editTaskConst) {
                TaskType.MORNING_FOOD -> morningFoodData
                TaskType.EVENING_FOOD -> eveningFoodData
                TaskType.SCOOP_POOP -> poopScoopData
            }

        // popup that updates data accordingly
        if (dataConst != null) {
            EditPopup(
                data = dataConst,
                onCancel = {
                    editTask = null
                },
                users = users,
                onUpdateData = {
                    when (editTaskConst) {
                        TaskType.MORNING_FOOD -> morningFoodData = it
                        TaskType.EVENING_FOOD -> eveningFoodData = it
                        TaskType.SCOOP_POOP -> poopScoopData = it
                    }
                    editTask = null
                    updateTask(Task.from(editTaskConst, it))
                },
                onDeleteTask = {
                    when (editTaskConst) {
                        TaskType.MORNING_FOOD -> {
                            deleteTask(Task.from(TaskType.MORNING_FOOD, dataConst))
                            morningFoodData = null
                        }

                        TaskType.EVENING_FOOD -> {
                            deleteTask(Task.from(TaskType.EVENING_FOOD, dataConst))
                            eveningFoodData = null
                        }

                        TaskType.SCOOP_POOP -> {
                            deleteTask(Task.from(TaskType.SCOOP_POOP, dataConst))
                            poopScoopData = null
                        }
                    }
                    editTask = null
                },
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
 * @param userName the name of the currently logged-in user.
 * @param onOpenSettings the function to be called when the settings are clicked.
 */
@Composable
private fun Header(
    userName: String,
    onOpenSettings: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        // layout three elements from right to left
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            // nested row for just icon and text
            // this excludes settings to allow for the spacer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // paw icon
                Icon(
                    imageVector = Icons.Outlined.Pets,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(34.dp),
                )

                // main header text
                Text(
                    text = "Cat Care Tracker",
                    style = MaterialTheme.typography.headlineLarge,
                )
            }

            // push last element to the far right
            Spacer(modifier = Modifier.weight(1f))

            // far right is the settings button
            // If there are errors, try refreshing Gradle
            IconButton(onClick = onOpenSettings) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                )
            }
        }

        // Shows today's date
        Text(
            text = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        // displays user's username
        Text(
            text = "Hello, $userName!",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

/**
 * Displays a card for task selection with the following format:
 * Title
 * Content
 *
 * @param title title text to be displayed at the top of the card.
 * @param content the composable to be displayed in the card
 */
@Composable
private fun TaskSectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    // Wrap content in a card
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        // Display flat with canvas
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            // tittle text
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            // whatever content we passed to the function
            content()
        }
    }
}

/**
 * Creates a status row with an icon and content depending on one whether it has been completed.
 * if the even has been completed it shows some text to give information about the completion.
 * otherwise it shows a button to allow the user to complete the task
 *
 * @param data the EventComplete data corresponding to the type of task this is
 * @param pendingLabel Label for the button when the task has not been completed.
 * @param completeTitle Main text to be displayed when the task is complete.
 * @param badgeBackground color of the circle behind the icon.
 * @param badgeForeground color of the icon.
 * @param onClick called when the complete button is clicked.
 * @param registerTask called directly before onClick to register the task with the database
 * @param onEditClick called when the edit button is clicked.
 * @param taskType the type of task for the data
 * @param user the currently logged-in user
 * @param icon composable displayed to the left of the button.
 */
@Composable
private fun StatusRow(
    data: EventCompletedData?,
    pendingLabel: String,
    completeTitle: String,
    badgeBackground: Color,
    badgeForeground: Color,
    onClick: (EventCompletedData) -> Unit,
    registerTask: (Task) -> Unit,
    onEditClick: () -> Unit,
    taskType: TaskType,
    user: User,
    icon: @Composable () -> Unit,
) {
    // align things from left to right
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        // create a circular element at this point in the row
        Box(
            modifier =
                Modifier
                    .size(72.dp)
                    .background(badgeBackground, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            icon()
        }

        // no one has completed the task so allow user to complete with a button
        if (data == null) {
            OutlinedButton(
                onClick = {
                    val clockSnap = Clock.System.now()
                    registerTask(Task.from(taskType, user, clockSnap))
                    onClick(EventCompletedData(user, clockSnap))
                },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium,
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                border =
                    BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                    ),
            ) {
                // text displayed on the button
                Text(
                    text = pendingLabel,
                    modifier = Modifier.padding(vertical = 10.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        } else {
            // task has been completed by someone
            // display two pieces of text one on top of the other
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // main text for the task being done
                Text(
                    text = completeTitle,
                    style = MaterialTheme.typography.titleLarge,
                )
                // subtext for when the user did the task
                Text(
                    text = "By ${data.user.name} at ${timeToText(data.time)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Edit data button
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit",
                )
            }
        }
    }
}

/**
 * Creates a pop-up in the center of the screen which blocks the rest of the screen
 * with an alpha effect
 *
 * @param data the initial data to  be displayed
 * @param onCancel called when the cancel button is clicked.
 * @param onUpdateData called when the save button is clicked.
 * @param onDeleteTask called when the delete button is clicked for a task.
 * @param users list of all the current users.
 */
@Composable
fun EditPopup(
    data: EventCompletedData,
    onCancel: () -> Unit,
    onUpdateData: (EventCompletedData) -> Unit,
    onDeleteTask: () -> Unit,
    users: List<User>,
) {
    // first->Hour, second->Minute, third->am/pm
    val time = timeToValues(data.time)

    // State
    var user by remember { mutableStateOf(data.user) }
    var timeFormat by remember { mutableStateOf(time) }

    PopUp {
        Box {
            Column(
                verticalArrangement = Arrangement.spacedBy(22.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                // space for top icon
                Spacer(Modifier.height(24.dp))

                // select a valid user */
                DropDown(
                    label = "username",
                    options = users,
                    selected = user,
                    onSelected = { user = it },
                    Modifier.weight(1f),
                    formatter = { user -> user.name },
                )

                // Dropdown to enter the new time
                TimeDropDown(timeFormat.hour, timeFormat.minute, timeFormat.amPm) {
                    timeFormat = it
                }

                // space for bottom icons
                Spacer(Modifier.height(24.dp))
            }

            // Top-right cancel button
            IconButton(
                onClick = onCancel,
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                Icon(Icons.Outlined.Cancel, contentDescription = "Cancel")
            }

            // Bottom row buttons
            Row(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = {
                        onUpdateData(
                            EventCompletedData(
                                user,
                                valuesToTime(
                                    data.time.toLocalDateTime(TimeZone.currentSystemDefault()).date,
                                    time = timeFormat,
                                ),
                            ),
                        )
                    },
                ) {
                    Icon(Icons.Outlined.Save, contentDescription = "Save")
                }

                IconButton(
                    onClick = onDeleteTask,
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

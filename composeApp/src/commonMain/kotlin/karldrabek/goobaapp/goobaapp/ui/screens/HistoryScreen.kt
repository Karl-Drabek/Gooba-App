package karldrabek.goobaapp.goobaapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goobaapp.composeapp.generated.resources.Res
import io.ktor.http.parameters
import karldrabek.goobaapp.goobaapp.backend.Task
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.ui.theme.DarkText
import karldrabek.goobaapp.goobaapp.ui.utils.PopUp
import karldrabek.goobaapp.goobaapp.utils.EventCompletedData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import karldrabek.goobaapp.goobaapp.utils.*
import kotlinx.datetime.todayIn

/** HISTORY CONFIG */

data class CellConfig(
    val icon: ImageVector,
    val taskName: String,
    val user: User? = null,
    val time: String? = null
)

// Icons
val morningIcon: ImageVector = Icons.Default.WbSunny
val eveningIcon: ImageVector = Icons.Default.ModeNight
val scoopIcon: ImageVector = Icons.Default.Pets

// Text
const val morningText: String = "Morning Food"
const val eveningText: String = "Evening Food"
const val scoopText: String = "Poop Scoop"

// pair list, order matters, can be done with a map in the future
val taskPairs = listOf(
    Pair(morningIcon, morningText),
    Pair(eveningIcon, eveningText),
    Pair(scoopIcon, scoopText)
)
/** ==================================================== */

//TODO Luke

/** Returns a list of tasks from a range of tasks between two dates, range is inclusive on both ends
 *
 * @param startDate Date string to start at YYYY-MM-DD
 * @param endDate Date string to end at YYYY-MM-DD
 * @return List of tasks that fall within the range, empty if no tasks were found
 */
fun taskDateRange(
    startDate: String,
    endDate: String, tasks:
    List<Task>
): List<Task> {

    val start = LocalDate.parse(startDate)
    val end = LocalDate.parse(endDate)

    return tasks.filter {
        val taskDate = LocalDate.parse(it.date)
        taskDate in start..end
    }

}


// TODO: Change from hard coded show to variable based on screen size
/** Creates a calendar cell off of a list of tasks
 *
 * @param showIcons: Display icons
 * @param showUsers: Display users
 * @param showTimes: Display times
 * @param showTaskNames: Display Task name
 * @param tasks: Tasks to display !!! MUST BE FOR SINGULAR DAY
 * @param users: Database users
 */
@Composable
fun DayOfTasks(
    showIcons: Boolean = true,
    showUsers: Boolean = true,
    showTimes: Boolean = true,
    showTaskNames: Boolean = true,
    tasks: List<Task>,
    users: List<User>
) {
    BoxWithConstraints(
        modifier = Modifier
            .aspectRatio(1f) // Keep cells square
            .padding(4.dp)
    ) {
        // Calculate a scale factor based on the smallest dimension
        val sizePx = minOf(maxWidth, maxHeight)

        // Define sizes as a percentage of the container
        val iconSize = sizePx * 0.4f
        val fontSize = sizePx.value * 0.15f
        val headerFontSize = fontSize * 1.5f

        var cells : MutableList<CellConfig> = mutableListOf()

        val morningTask: Task? = tasks.singleOrNull { it.type == TaskType.MORNING_FOOD.toString() }
        val eveningTask: Task? = tasks.singleOrNull { it.type == TaskType.EVENING_FOOD.toString() }
        val scoopTask: Task? = tasks.singleOrNull { it.type == TaskType.SCOOP_POOP.toString() }

        val orderedTasks: List<Task?> = listOf(
            morningTask,
            eveningTask,
            scoopTask
        )

        for(i in 0 until orderedTasks.size) {
            val task: Task? = orderedTasks[i]
            val pair = taskPairs[i]
            if(task != null) {
                cells.add(
                    CellConfig(
                        icon=pair.first,
                        taskName = pair.second,
                        user = users.find { it.id == task.userID },
                        time = task.time
                    )
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            for (cell in cells) {

                Column(

                ){

                    // Task Name and Icon
                    Row(
                        modifier = Modifier.padding(2.dp),
                    ) {

                        // TODO: Change conditional
                        if(showIcons){

                            Icon(
                                cell.icon,
                                null,
                                modifier = Modifier.size(iconSize)
                            )

                        }

                        // TODO: Change conditional
                        if(showTaskNames){

                            Text(
                                text = cell.taskName,
                                color = DarkText,
                                fontWeight = FontWeight.Bold,
                                fontSize = headerFontSize.sp
                            )

                        }
                    }

                    // User and time
                    Row(
                        modifier = Modifier.padding(2.dp),
                    ){

                        // TODO: Change Conditional
                        if(showUsers){
                            Text(
                                text = "By ${cell.user}",
                                color = DarkText,
                                fontSize = fontSize.sp
                            )
                        }

                        // TODO: Change Conditional
                        if(showTimes) {
                            Text(
                                text = "At: ${cell.time}",
                                color = DarkText,
                                fontSize = fontSize.sp
                            )
                        }
                    }
                }

            }
        }
    }
}

enum class ScreenState {
    DAY,
    WEEK,
    MONTH
}
@Composable
fun HistoryScreen(
    users: List<User>,
    tasks: List<Task>,
    onExit: () -> Unit,
) {

    val menuWidth = 180f

    // Menu visibility
    var showRangeMenu by rememberSaveable { mutableStateOf(false) }
    var selectedState by rememberSaveable { mutableStateOf(ScreenState.DAY) }

    // Start with Today's Date YYYY-MM-DD
    var selectedDate by rememberSaveable {
        mutableStateOf(
            Clock.System.todayIn(timeZone = TimeZone.UTC).toString()
        )
    }

    var showDateMenu by rememberSaveable { mutableStateOf(false) }

    // Create a Date Picker State
    val datePickerState = rememberDatePickerState()

    // Create a format
    val datePickerFormat = remember { DatePickerDefaults.dateFormatter() }

    // Screen Background
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
                        tint = DarkText,
                    )
                }

                // Header
                Text(
                    text = "History",
                    color = DarkText,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                // set max size
                shape = MaterialTheme.shapes.large,
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface, // white
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ){

                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {

                        /** View Standpoint */
                        Button(
                            modifier = Modifier.padding(8.dp),
                            onClick = { showRangeMenu = !showRangeMenu },
                            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(4.dp)
                        ) {

                            Text(
                                text = selectedState.toString(),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                        }

                        DropdownMenu(
                            expanded = showRangeMenu,
                            onDismissRequest = { showRangeMenu = false },
                            modifier = Modifier.width(menuWidth.dp)
                        ) {
                            DropdownMenuItem(
                                text = { Text(ScreenState.DAY.toString()) },
                                onClick = {
                                    selectedState = ScreenState.DAY
                                    showRangeMenu = false
                                }
                            )

                            DropdownMenuItem(
                                text = { Text(ScreenState.WEEK.toString()) },
                                onClick = {
                                    selectedState = ScreenState.WEEK
                                    showRangeMenu = false
                                }
                            )

                            DropdownMenuItem(
                                text = { Text(ScreenState.MONTH.toString()) },
                                onClick = {
                                    selectedState = ScreenState.MONTH
                                    showRangeMenu = false
                                }
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        /** Date Button */
                        Button(
                            modifier = Modifier.padding(8.dp),
                            onClick =  { showDateMenu = !showDateMenu },
                            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(4.dp),
                        ){

                            Text(
                                text = "Date: $selectedDate",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                        }
                    }

                    HorizontalDivider(thickness = 2.dp)

                    if (showDateMenu) {

                        LaunchedEffect(datePickerState.selectedDateMillis) {
                            datePickerState.selectedDateMillis?.let { millis ->

                                val date = Instant.fromEpochMilliseconds(millis)
                                    .toLocalDateTime(TimeZone.UTC)
                                    .date
                                    .toString()

                                selectedDate = date
                            }
                        }

                        DatePicker(
                            state = datePickerState,
                            dateFormatter = datePickerFormat,
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer(
                                    scaleX = 0.9f,
                                    scaleY = 0.9f,
                                    transformOrigin = TransformOrigin(0f, 0f),
                                ),
                            colors = DatePickerDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = DarkText,
                                headlineContentColor = DarkText,
                                weekdayContentColor = DarkText,
                                subheadContentColor = DarkText,
                                dayContentColor = DarkText,
                                selectedDayContentColor = Color.White,
                                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                                todayContentColor = MaterialTheme.colorScheme.primary,
                                todayDateBorderColor = MaterialTheme.colorScheme.primary,
                            )
                        )
                    } else {
                       when(selectedState) {

                           // Day view implementation
                           ScreenState.DAY -> {

                               Column(
                                   modifier = Modifier.fillMaxWidth(),
                                   horizontalAlignment = Alignment.CenterHorizontally,
                                   verticalArrangement = Arrangement.spacedBy(16.dp),
                               ) {

                                   Text(
                                       "Day View",
                                       color = DarkText,
                                       style = MaterialTheme.typography.headlineMedium,
                                       fontWeight = FontWeight.Bold
                                   )

                                   HorizontalDivider(thickness = 2.dp)

                                   val taskList = taskDateRange(selectedDate, selectedDate, tasks)

                                   DayOfTasks(
                                       tasks = taskList,
                                       users = users
                                   )
                               }
                           }

                           // Week view implementation
                           ScreenState.WEEK -> {}

                           // Month view implementation
                           ScreenState.MONTH -> {}
                       }
                    }
                }
            }
        }
    }
}

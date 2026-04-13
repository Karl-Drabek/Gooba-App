package karldrabek.goobaapp.goobaapp.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.backend.Task
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.ui.theme.DarkText
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import karldrabek.goobaapp.goobaapp.utils.*
import kotlinx.datetime.todayIn

/** HISTORY CONFIG
 * ============================
 */
val dividerThickness : Dp = 2.dp
val todaysDate = Clock.System.todayIn(timeZone = TimeZone.UTC).toString()
data class CellConfig(
    val settings: Triple<ImageVector, String, Color>,
    val user: User?,
    val time: String
)

// Settings are expected in Icon, Name, Color
val morningSettings = Triple(Icons.Default.WbSunny, "Morning Food", Color.Yellow)
val eveningSettings = Triple(Icons.Default.ModeNight, "Evening Food", Color.Blue)
val scoopSettings = Triple(Icons.Default.Pets, "Scoop Food", Color.Gray)

// pair list, order matters, can be done with a map in the future
val taskSettings = listOf(
    morningSettings,
    eveningSettings,
    scoopSettings,
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
    endDate: String,
    tasks: List<Task>
): List<Task> {

    val start = LocalDate.parse(startDate)
    val end = LocalDate.parse(endDate)

    return tasks.filter {
        val taskDate = LocalDate.parse(it.date)
        taskDate in start..end
    }

}

enum class ScreenState {
    DAY,
    WEEK,
    MONTH
}

/** Creates a calendar cell off of a list of tasks
 *
 * @param screenState: Current view state of the calendar
 * @param tasks: Tasks to display !!! MUST BE FOR SINGULAR DAY
 * @param users: Database users
 */
@Composable
fun CalendarView(
    screenState: ScreenState,
    tasks: List<Task>,
    users: List<User>,
    dayNum: Int
) {
    var showTimes = false
    var showTaskNames = false

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {

        when(screenState) {

            ScreenState.DAY -> {
                showTimes = true
                showTaskNames = true
            }

            ScreenState.WEEK -> {
                showTimes = true
                showTaskNames = true
            }

            ScreenState.MONTH -> {

            }

        }

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
            if(task != null) {
                cells.add(
                    CellConfig(
                        settings = taskSettings[i],
                        user = users.find { it.id == task.userID },
                        time = task.time
                    )
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(16.dp),
        ) {

            for (cell in cells) {

                Column(
                    modifier =
                        Modifier.
                            fillMaxWidth().
                            padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ){

                    // Number of the day
                    Text(
                        text = dayNum.toString(),
                        fontWeight = FontWeight.Bold,
                        style=MaterialTheme.typography.bodyMedium,
                    )

                    HorizontalDivider(thickness = dividerThickness)

                    // Task Name and Icon
                    Row(
                        modifier = Modifier.padding(2.dp),
                    ) {

                            Icon(
                                cell.settings.first,
                                "Task",
                                tint=cell.settings.third
                            )

                            Spacer(Modifier.width(8.dp))

                        // TODO: Change conditional
                        if(showTaskNames){

                            Text(
                                text = cell.settings.second,
                                color = DarkText,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )

                        }
                    }

                    // User and time
                    Row(
                        modifier = Modifier.padding(2.dp),
                    ){
                        
                            Text(
                                text = "By ${cell.user} ",
                                color = DarkText,
                                style = MaterialTheme.typography.bodyMedium
                            )

                        // TODO: Change Conditional
                        if(showTimes) {
                            Text(
                                text = "At: ${cell.time}",
                                color = DarkText,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(Modifier.width(8.dp))
                        }
                    }
                }

            }
        }
    }
}

/**
 * Runs the History Screen
 *
 * @param users List of users on the app
 * @param tasks List of tasks from the loaded month
 * @param chosenDate Selected date to base view around
 * @param onExit Exit lambda, should go to main screen
 * @param loadHistory Reloads the history with the new date from a different month
 */
@Composable
fun HistoryScreen(
    tasks: List<Task>,
    users: List<User>,
    chosenDate: String,
    onExit: () -> Unit,
    loadHistory: (String) -> Unit
) {

    val menuWidth = 180f

    // Menu visibility
    var showRangeMenu by rememberSaveable { mutableStateOf(false) }
    var selectedState by rememberSaveable { mutableStateOf(ScreenState.DAY) }

    // Start with Today's Date YYYY-MM-DD
    var selectedDate by rememberSaveable {
        mutableStateOf(
            chosenDate
        )
    }

    var loadedTasks by rememberSaveable { mutableStateOf(tasks) }

    var showDateMenu by rememberSaveable { mutableStateOf(false) }

    // Create a Date Picker State
    val datePickerState = rememberDatePickerState()

    // Create a format
    val datePickerFormat = remember { DatePickerDefaults.dateFormatter() }

    @Composable
    fun CalendarWindow(
        title: String,
        content: @Composable () -> Unit
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Text(
                text = "$title View",
                color = DarkText,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )

            HorizontalDivider(thickness = dividerThickness)

            content()

        }

    }

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

                    HorizontalDivider(thickness = dividerThickness)

                    if (showDateMenu) {

                        LaunchedEffect(datePickerState.selectedDateMillis) {
                            datePickerState.selectedDateMillis?.let { millis ->

                                val date = Instant.fromEpochMilliseconds(millis)
                                    .toLocalDateTime(TimeZone.UTC)
                                    .date
                                    .toString()

                                // Compare month and year
                                if(date.take(6) != selectedDate.take(6)) {
                                    loadHistory(date) // Relaunch history window with loaded tasks for new date
                                } else {
                                    selectedDate = date
                                }

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

                               val dayTasks: List<Task> = taskDateRange(selectedDate, selectedDate, tasks)

                               CalendarWindow(
                                   title="Day",
                               ){
                                   CalendarView(
                                       ScreenState.DAY,
                                       tasks=dayTasks,
                                       users=users,
                                       dayNum=selectedDate.takeLast(2).toInt()
                                   )
                               }
                           }

                           // Week view implementation
                           ScreenState.WEEK -> {

                           }

                           // Month view implementation
                           ScreenState.MONTH -> {

                               // Base window for the calendar to be displayed
                               CalendarWindow (
                                   title="Month"
                               ) {
                                   Column(
                                       modifier = Modifier.fillMaxSize()
                                   ) {

                                       // 4 rows
                                       for(i in 0..4) {

                                           Row(
                                               modifier = Modifier.fillMaxWidth(),
                                           ) {

                                               // 7 Days per week
                                               for(j in 1..7) {

                                                   if(i*7 + j > 31) break
                                                   // Breaks between the cells
                                                   VerticalDivider(thickness = dividerThickness)

                                                   val dayOfTasks = tasks.filter {
                                                       it.date.endsWith("${(i*7)+j}")
                                                   }

                                                   // Show this cell
                                                   CalendarView(
                                                       ScreenState.MONTH,
                                                       tasks=dayOfTasks,
                                                       users=users,
                                                       dayNum= i * 7 + j
                                                   )

                                               }

                                               // Fence posting for the lines
                                               VerticalDivider(thickness = dividerThickness)
                                           }

                                           // Bottom line of the grid
                                           HorizontalDivider(thickness = dividerThickness)
                                       }
                                   }
                               }

                           }
                       }
                    }
                }
            }
        }
    }
}

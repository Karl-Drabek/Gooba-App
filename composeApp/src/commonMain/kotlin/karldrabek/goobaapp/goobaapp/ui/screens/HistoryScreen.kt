package karldrabek.goobaapp.goobaapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.backend.Task
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.ui.theme.DarkText
import karldrabek.goobaapp.goobaapp.utils.EventCompletedData
import kotlinx.datetime.LocalDate
import kotlin.time.Clock

// TODO: LUKE
@Composable
fun HistoryScreen(
    user: User,
    onExit: () -> Unit
) {

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
                    Text(
                        text = "Find Tasks from specified Date",
                        color = DarkText,
                        style = MaterialTheme.typography.headlineMedium,
                    )

                    HorizontalDivider(thickness = 2.dp)

                    // Create a Date Picker State
                    val datePickerState = rememberDatePickerState()

                    // Create a format
                    val datePickerFormat = remember { DatePickerDefaults.dateFormatter() }

                    CompositionLocalProvider(
                    ) {
                        DatePicker(
                            state= datePickerState,
                            dateFormatter = datePickerFormat,
                            modifier = Modifier.fillMaxWidth().graphicsLayer(
                                scaleX = 0.9f,
                                scaleY = 0.9f,
                                transformOrigin = TransformOrigin(0f,0f),
                            ),
                            colors=DatePickerDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = DarkText,
                                headlineContentColor = DarkText,
                                weekdayContentColor = DarkText,
                                subheadContentColor = DarkText,

                                // Calendar day text
                                dayContentColor = DarkText,
                                selectedDayContentColor = Color.White,

                                // Selected day background
                                selectedDayContainerColor = MaterialTheme.colorScheme.primary,

                                // Today highlight
                                todayContentColor = MaterialTheme.colorScheme.primary,
                                todayDateBorderColor = MaterialTheme.colorScheme.primary,
                            )
                        )
                    }

                    Button(
                        onClick = {
                            // Implement Date selection logic
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
                            )
                    ) {
                        Text(
                            text = "Enter",
                            modifier = Modifier.padding(vertical = 8.dp),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }
    }
}

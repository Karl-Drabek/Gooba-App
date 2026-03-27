package karldrabek.goobaapp.goobaapp.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.utils.TimeFormat

@Composable
fun TimeDropDown(
    hour: Int,
    minute: Int,
    amPm: String,
    onTimeChange: (TimeFormat) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        /** Hour dropdown */
        DropDown(
            label = "Hour",
            options = (1..12).toList(),
            selected = hour,
            onSelected = { onTimeChange(TimeFormat(it, minute, amPm)) },
            modifier = Modifier.weight(1f),
        )

        /** Minute dropdown*/
        DropDown(
            label = "Min",
            options = (0..59).toList(),
            selected = minute,
            onSelected = { onTimeChange(TimeFormat(hour, it, amPm)) },
            modifier = Modifier.weight(1f),
            formatter = { minute -> minute.toString().padStart(2, '0') },
        )

        /** AM/PM dropdown */
        DropDown(
            label = "AM/PM",
            options = listOf("AM", "PM"),
            selected = amPm,
            onSelected = { onTimeChange(TimeFormat(hour, minute, it)) },
            modifier = Modifier.weight(1f),
        )
    }
}

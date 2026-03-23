package karldrabek.goobaapp.goobaapp.ui.utils

import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import java.lang.String.format
import java.util.Locale


/**
 * Android-specific implementation of [karldrabek.goobaapp.goobaapp.ui.utils.pickTime]
 *
 *  @param context
 *  @param onTimePicked when the time is selected it calls onTimePicked with the current time in HH:MM
 */
actual fun pickTime(context: Any?, onTimePicked: (String) -> Unit) {
    /** Ensure context is of type Context */
    if (context !is Context) return

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    /** Android-specific TimePickerDialog */
    TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val time = format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
            onTimePicked(time)
        },
        hour,
        minute,
        false /** is24HourView */
    ).show()
}
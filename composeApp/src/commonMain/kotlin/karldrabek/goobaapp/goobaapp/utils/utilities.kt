package karldrabek.goobaapp.goobaapp.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * formatTime puts the Instant into the time format:
 * hour:minute:AM/PM
 *
 * @property Instant the time to be converted
 */
fun timeToText(instant: Instant): String {
    val time = timeToValues(instant)

    val minutePadded = time.minute.toString().padStart(2, '0')

    return "${time.hour}:$minutePadded ${time.amPm}"
}

/**
 * puts the Instant into minute hour and AM/PM:
 *
 *
 * @property Instant the time to be converted
 */
fun timeToValues(instant: Instant): TimeFormat {
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val hour24 = local.hour
    val minute = local.minute

    val amPm = if (hour24 < 12) "AM" else "PM"
    val hour12 =
        when {
            hour24 == 0 -> 12
            hour24 > 12 -> hour24 - 12
            else -> hour24
        }
    return TimeFormat(hour12, minute, amPm)
}

// TODO make it so that the time is for the correct day

/**
 * Takes the time format and recreates the instant:
 *
 * @property time the TimeFormat representing the time
 */
fun valuesToTime(time: TimeFormat): Instant {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    val hour24 =
        when (time.amPm) {
            "AM" if time.hour == 12 -> 0
            "PM" if time.hour != 12 -> time.hour + 12
            else -> time.hour
        }

    return LocalDateTime(
        year = today.year,
        month = today.month.number,
        day = today.day,
        hour = hour24,
        minute = time.minute,
        second = 0,
        nanosecond = 0,
    ).toInstant(TimeZone.currentSystemDefault())
}

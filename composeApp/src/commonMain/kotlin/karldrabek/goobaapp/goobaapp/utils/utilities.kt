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
    val triple = timeToValues(instant)

    val minutePadded = triple.second.toString().padStart(2, '0')

    return "${triple.first}:${minutePadded} ${triple.third}"
}

/**
 * puts the Instant into minute hour and AM/PM:
 *
 *
 * @property Instant the time to be converted
 */
fun timeToValues(instant: Instant): Triple<Int, Int, String> {
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val hour24 = local.hour
    val minute = local.minute

    val amPm = if (hour24 < 12) "AM" else "PM"
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }
    return Triple(hour12, minute, amPm)
}

/**
 * Takes the time format and recreates the instant:
 *
 * @property hour12 hour from 1-12
 * @property minute minute from 0-59
 * @property amPm AM for morning, PM for evening
 */
fun valuesToTime(
    hour12: Int,
    minute: Int,
    amPm: String
): Instant {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    val hour24 = when (amPm) {
        "AM" if hour12 == 12 -> 0
        "PM" if hour12 != 12 -> hour12 + 12
        else -> hour12
    }

    return LocalDateTime(
        year = today.year,
        month = today.month.number,
        day = today.day,
        hour = hour24,
        minute = minute,
        second = 0,
        nanosecond = 0
    ).toInstant(TimeZone.currentSystemDefault())
}


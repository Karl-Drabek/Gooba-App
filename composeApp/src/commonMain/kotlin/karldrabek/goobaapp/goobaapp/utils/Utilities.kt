package karldrabek.goobaapp.goobaapp.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

/** Converts an instant to a date and time string
 *
 * @param instant Datetime instant
 * @return Pair of a date and a time string
 */
fun getDateAndTimeAsString(instant: Instant = Clock.System.now()): DateTime {
    val systemTimeZone = TimeZone.currentSystemDefault()
    val dateAndTime = instant.toLocalDateTime(systemTimeZone)
    val date = "${dateAndTime.year}-${dateAndTime.month.number}-${dateAndTime.day}"
    val time = "${dateAndTime.hour}:${dateAndTime.minute}:${dateAndTime.second}"
    return DateTime(date, time)
}

/**
 *
 * @param date
 * @param time
 * @return instant from that date and time
 */
fun dateAndTimeStringToInstant(date : String, time : String): Instant {

    val localDateTime = LocalDateTime.parse("${date}T${time}")
    val timeZone = TimeZone.currentSystemDefault()
    return localDateTime.toInstant(timeZone)
}

/**
 * puts the Instant into minute hour and AM/PM:
 *
 * @param instant the time to be converted
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

/**
 * formatTime puts the Instant into the time format:
 * hour:minute:AM/PM
 *
 * @param instant the time to be converted
 */
fun timeToText(instant: Instant): String {
    val time = timeToValues(instant)

    val minutePadded = time.minute.toString().padStart(2, '0')

    return "${time.hour}:$minutePadded ${time.amPm}"
}

/**
 * Takes the time format and recreates the instant:
 *
 * @param date the date at which the new time will be constructed for.
 * @param time the TimeFormat representing the time.
 */
fun valuesToTime(
    date: LocalDate,
    time: TimeFormat,
): Instant {
    val hour24 =
        when (time.amPm) {
            "AM" if time.hour == 12 -> 0
            "PM" if time.hour != 12 -> time.hour + 12
            else -> time.hour
        }

    val dateTime = date.atTime(hour24, time.minute)
    return dateTime.toInstant(TimeZone.currentSystemDefault())
}

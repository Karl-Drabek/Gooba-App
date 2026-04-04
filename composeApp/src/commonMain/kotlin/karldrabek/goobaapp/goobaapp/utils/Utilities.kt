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

fun getDateAndTimeAsString(instant: Instant = Clock.System.now()): DateTime {
    val systemTimeZone = TimeZone.currentSystemDefault()
    val dateAndTime = instant.toLocalDateTime(systemTimeZone)
    val date = "${dateAndTime.day} : ${dateAndTime.month} : ${dateAndTime.year}"
    val time = "${dateAndTime.hour} : ${dateAndTime.minute} : ${dateAndTime.second}"
    return DateTime(date, time)
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
 * textToTime puts the string formatted time into an instant on the current day:
 * hour:minute:AM/PM
 *
 * @param text the string version to be converted in the form hour:minute:AM/PM
 * @param timeZone the timezone that the current time will be adjusted for
 */
fun textToTime(
    text: String,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): Instant {
    // Split "5:07 PM" → ["5:07", "PM"]
    val (timePart, amPm) = text.split(" ")

    // Split "5:07" → ["5", "07"]
    val (hourStr, minuteStr) = timePart.split(":")

    var hour = hourStr.toInt()
    val minute = minuteStr.toInt()

    // Convert to 24-hour format
    if (amPm == "PM" && hour != 12) hour += 12
    if (amPm == "AM" && hour == 12) hour = 0

    // Use today's date
    val today =
        Clock.System
            .now()
            .toLocalDateTime(timeZone)
            .date

    val localDateTime =
        LocalDateTime(
            year = today.year,
            monthNumber = today.month.number,
            dayOfMonth = today.day,
            hour = hour,
            minute = minute,
        )

    return localDateTime.toInstant(timeZone)
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

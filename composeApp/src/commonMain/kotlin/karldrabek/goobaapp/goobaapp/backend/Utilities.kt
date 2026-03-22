package karldrabek.goobaapp.goobaapp.backend

import kotlinx.datetime.*
import kotlin.time.Instant
import kotlin.time.Clock

data class serverMessage(
    val user: User,
    val time: Instant = Clock.System.now(),
    val message: String = ""
)

enum class Mealtime {
    EVENING,
    MORNING
}

fun feed(user : User, mealtime: Mealtime, time: Instant = Clock.System.now()) {
    // TODO: add entry to the DB
}

//
fun scoop(user: User) {
    // Check the day
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    if (user.scoopDay != today.dayOfWeek){
        // Prompt warning
    }

}

// Add the name to the DB and return whether it already exists
fun registerName(name: String) : Boolean {
    return false //TODO: Implement
}

/**
 * formatTime puts the Instant into the time format:
 * hour:minute:AM/PM
 *
 * @property Instant the time to be converted
 */
fun formatTime(instant: Instant): String {
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val hour24 = local.hour
    val minute = local.minute

    val amPm = if (hour24 < 12) "AM" else "PM"
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }

    val minutePadded = minute.toString().padStart(2, '0')

    return "$hour12:$minutePadded $amPm"
}
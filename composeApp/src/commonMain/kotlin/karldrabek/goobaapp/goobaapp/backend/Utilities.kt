package karldrabek.goobaapp.goobaapp.backend

import kotlinx.datetime.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
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
    if (user.scoopDay.uppercase() != today.dayOfWeek.toString().uppercase()) {
        // Prompt warning
    }

}

// Add the name to the DB and return whether it already exists
fun registerName(name: String) : Boolean {
    return false //TODO: Implement
}

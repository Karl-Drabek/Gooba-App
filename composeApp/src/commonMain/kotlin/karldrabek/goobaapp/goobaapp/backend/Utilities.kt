package karldrabek.goobaapp.goobaapp.backend

import io.ktor.client.HttpClient
import kotlinx.datetime.*
import kotlin.time.Instant
import kotlin.time.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import karldrabek.goobaapp.goobaapp.*

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
        // Prompt warning, return early if no
    }


}

/** Adds a User to the database if it does not exist, always returns the clients user
 * @param name username
 * @return App user
 */
suspend fun KoinComponent.registerUser (name: String, scoopDay : String = "") : User? {
    val client : HttpClient by inject()

    // Get the list of users with matching name from db
    val users: List<User>? = client.get(searchUserUrl(name)).body()
    if (!users.isNullOrEmpty() && users.size == 1) {
        // Return the user
        return users[0]
    } else { // User not found in the list
        // Return the added user
        return client.post(postUsersUrl) {
            contentType(ContentType.Application.Json)
            setBody(User(name=name, scoopDay=scoopDay))
        }.body()
    }
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
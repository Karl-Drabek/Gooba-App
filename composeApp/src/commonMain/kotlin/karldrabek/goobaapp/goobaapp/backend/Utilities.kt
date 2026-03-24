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
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import karldrabek.goobaapp.goobaapp.*
import kotlinx.serialization.json.JsonElement

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

object UserRemoteManager: KoinComponent {

    /** Adds a User to the database if it does not exist, always returns the clients user
     * @param user user
     * @return App user
     */
    suspend fun registerUser (user: User) : User? {
        val client : HttpClient by inject()

        // Get the list of users with matching name from db
        val users: List<User>? = searchUser(user)
        if (!users.isNullOrEmpty()) return users[0]

        // Return the added user
        val user: User? = client.post(postUsersUrl) {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body()

        return user
    }

    /** Updates a given user on the server
     * @param user new user credentials, id must remain the same
     * @return new user information
     */
    suspend fun updateUser (user: User) : User? {
        val client : HttpClient by inject()

        val response: HttpResponse =  client.put(putUsersUrl) {
            contentType(ContentType.Application.Json)
            setBody(user)
        }

        if (response.status == HttpStatusCode.OK) {
            val users = searchUser(user)
            if (!users.isNullOrEmpty()) return users[0]
        }
        return null
    }

    /** Searches for a user on the DB */
    suspend fun searchUser(user: User): List<User>?  {
        val client : HttpClient by inject()
        val response = client.get(searchUserUrl(user.name))

        return if (response.status == HttpStatusCode.OK) {
            // Only try to parse JSON if the server said "OK"
            response.body<List<User>?>()
        } else if (response.status == HttpStatusCode.NotFound) {
            // If 404, just return an empty list instead of crashing
            emptyList()
        } else {
            println("Server Error: ${response.status}")
            emptyList()
        }
    }

    /** =======================================
     *                  WARNING
     *  =======================================
     *     CLEARS ALL USERS IN THE DATABASE
     */
    suspend fun clearAllUsers() {
        val client : HttpClient by inject()
        client.get(clearUsersUrl)
    }

    /** Returns all users that exist on the database */
    suspend fun getAllUsers() : List<User>? {
        val client : HttpClient by inject()
        return client.get(getUsersUrl).body()
    }

    /** Returns true if user is successfully deleted */
    suspend fun deleteUser(user : User?) : Boolean {
        val client : HttpClient by inject()
        val response: HttpResponse = client.delete(deleteUsersUrl) {
            contentType(ContentType.Application.Json)
            setBody(user)
        }

        return response.status == HttpStatusCode.OK
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
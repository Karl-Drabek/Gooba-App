package karldrabek.goobaapp.goobaapp.backend

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

/**
 * User stores all data for the user that is stored in the DB.
 *
 * @property name the name of the User.
 * @property scoopDay the DayofWeek which the user is assigned to scoop the poop.
 * @constructor Creates a user with a name who is assigned to no day to scoop poop.
 */
@Serializable
data class User(
    var name: String,
    var scoopDay: String = "",
    var id: Int = 0,
) {
    override fun toString(): String = name
}

object UserRemoteManager : KoinComponent {
    /** Adds a User to the database if it does not exist, always returns the clients user
     * @param user user
     * @return App user
     */
    suspend fun registerUser(user: User): User? {
        val client: HttpClient by inject()

        // Get the list of users with matching name from db
        val searchedUser: User? = searchUserById(user.id)
        if (searchedUser != null) return searchedUser

        // Return the added user
        val response: HttpResponse =
            client.post(postUsersUrl) {
                contentType(ContentType.Application.Json)
                setBody(user)
            }

        return when (response.status) {
            HttpStatusCode.Created -> response.body()
            HttpStatusCode.NotImplemented -> null
            HttpStatusCode.BadRequest -> null
            else -> throw Exception("Server Error")
        }
    }

    /** Updates a given user on the server
     * @param user new user credentials, id must remain the same
     * @return new user information
     */
    suspend fun updateUser(user: User): User? {
        val client: HttpClient by inject()

        val response: HttpResponse =
            client.put(putUsersUrl) {
                contentType(ContentType.Application.Json)
                setBody(user)
            }

        if (response.status == HttpStatusCode.OK) {
            val user = searchUserById(user.id)
            if (user != null) return user
        }

        return null
    }

    /** Searches for a user on the DB */
    suspend fun searchUserById(id: Int): User? {
        val client: HttpClient by inject()
        val response: HttpResponse = client.get(searchUserByIdUrl(id))

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> null
            HttpStatusCode.BadRequest -> null
            else -> throw Exception("Unknown Error")
        }
    }

    suspend fun searchUsersByName(name: String): List<User>? {
        val client: HttpClient by inject()
        val response: HttpResponse = client.get(searchUserByNameUrl(name))

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> null
            HttpStatusCode.BadRequest -> null
            else -> throw Exception("Server Error")
        }
    }

    /** =======================================
     *                  WARNING
     *  =======================================
     *     CLEARS ALL USERS IN THE DATABASE
     */
    suspend fun clearAllUsers() {
        val client: HttpClient by inject()
        client.get(clearUsersUrl)
    }

    /** Returns all users that exist on the database */
    suspend fun getAllUsers(): List<User>? {
        val client: HttpClient by inject()
        val users: List<User> = client.get(getUsersUrl).body()

        return users.ifEmpty {
            null
        }
    }

    /** Returns true if user is successfully deleted */
    suspend fun deleteUser(id: Int): Boolean {
        val client: HttpClient by inject()
        val response: HttpResponse = client.delete(deleteUsersUrl(id))
        return response.status == HttpStatusCode.OK
    }
}

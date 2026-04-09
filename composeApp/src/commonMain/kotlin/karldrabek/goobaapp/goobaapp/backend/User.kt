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
 * @property scoopDay the day of the week which the user is assigned to scoop the poop.
 * @property id the userID to uniquely identify the user
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

/** Don't instantiate. can be used to call various server functions */
object UserRemoteManager : KoinComponent {
    /** Adds a User to the database if it does not exist
     * @param user the user which you wish to register with the database.
     * @return the same user which was passed in if all was successful. if the user already exists or another error occurs this returns null.
     */
    suspend fun registerUser(user: User): User? {
        val client: HttpClient by inject()

        // Get the list of users with matching name from db
        val searchedUser: User? = searchUserById(user.id)
        if (searchedUser != null) return searchedUser

        // Return the added user
        val response: HttpResponse =
            client.post(POST_USERS_URL) {
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
     * @return new user information if it was successful, otherwise null
     */
    suspend fun updateUser(user: User): User? {
        val client: HttpClient by inject()

        val response: HttpResponse =
            client.put(PUT_USERS_URL) {
                contentType(ContentType.Application.Json)
                setBody(user)
            }

        if (response.status == HttpStatusCode.OK) {
            val user = searchUserById(user.id)
            if (user != null) return user
        }

        return null
    }

    /**
     * Searches for a user on the DB
     *
     * @param id the id of the user which you want to search for
     * @return the user if it exists, or null if it does not or there was an error
     */
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

    /**
     * Searches the database for a list of users which have a particular name
     *
     * @param name the name of the users which we are searching for
     * @return the list of all users in the database with that name. The list is empty if no such users exist. the result is null if there was an error in the query.
     */
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
        client.get(CLEAR_USERS_URL)
    }

    /** Returns all users that exist on the database
     *
     * @return the list of all users in the database. The list is empty if no such users exist. the result is null if there was an error in the query.
     */
    suspend fun getAllUsers(): List<User> {
        val client: HttpClient by inject()
        val users: List<User> = client.get(GET_USERS_URL).body()

        return users
    }

    /**
     * Deletes the user from the database
     *
     * @param id the id of the user which is to be deleted
     * @return true if the user was successfully deleted, otherwise false
     */
    suspend fun deleteUser(id: Int): Boolean {
        val client: HttpClient by inject()
        val response: HttpResponse = client.delete(deleteUsersUrl(id))
        return response.status == HttpStatusCode.OK
    }
}

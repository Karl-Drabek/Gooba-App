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
import karldrabek.goobaapp.goobaapp.clearUsersUrl
import karldrabek.goobaapp.goobaapp.deleteUsersUrl
import karldrabek.goobaapp.goobaapp.getUsersUrl
import karldrabek.goobaapp.goobaapp.postUsersUrl
import karldrabek.goobaapp.goobaapp.putUsersUrl
import karldrabek.goobaapp.goobaapp.searchUserUrl
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
data class User(var name: String, var scoopDay: String = "", var id:Int=0){
    override fun toString(): String {
        return name
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
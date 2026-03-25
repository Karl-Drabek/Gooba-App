package karldrabek.goobaapp.goobaapp.backend

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import karldrabek.goobaapp.goobaapp.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// TODO : CRACK KEARNAN HELLA (HE NEVER GONNA SEE THIS FR)

/** Stores data for a DB task request
 * @param type Activity ex Feeding/Scooping
 * @param userID associated ID of the user that completed the task
 * @param date String of the current date
 * @param time String of the current time
 */
@Serializable
data class Task (
    val type: String,
    val userID:Int,
    val date:String,
    val time:String
)

object TaskRemoteManager : KoinComponent {

    /** Registers a task to the DB
     * @param task Task to add to the DB
     * @return true if the task was successfully added
     */
    suspend fun addTask(task: Task) : Boolean {
        val client : HttpClient by inject()

        val response : HttpResponse = client.post(postTasksUrl) {
            contentType(ContentType.Application.Json)
            setBody(task)
        }

        return response.status == HttpStatusCode.OK
    }

    /** Deletes a task from the DB
     * @param type
     * @param date date task was completed
     * @return True upon successful deletion
     */
    suspend fun removeTask(type: String, date: String) : Boolean {
        val client : HttpClient by inject()
        val response : HttpResponse = client.delete(deleteTasksUrl(date, type))
        return response.status == HttpStatusCode.OK
    }

    /** Gets all completed tasks from the DB
     * @return List of tasks
     */
    suspend fun getAllTasks() : List<Task>? {
        val client : HttpClient by inject()
        val tasks: List<Task> = client.get(getTasksUrl).body()
        return tasks.ifEmpty {
            null
        }

    }

    /** Clears all completed tasks
     * @return true after succesful clear
     */
    suspend fun clearAllTasks() : Boolean {
        val client : HttpClient by inject()
        val response : HttpResponse = client.delete(clearTasksUrl)
        return response.status == HttpStatusCode.OK
    }

    /** Update a task
     * @param Task task with updated information
     * @return True upon proper update
     */
    suspend fun updateTask(task: Task): Boolean {

        val client: HttpClient by inject()
        val response : HttpResponse = client.put(putTasksUrl) {
            contentType(ContentType.Application.Json)
            setBody(task)
        }

        return response.status == HttpStatusCode.OK
    }

    /** Requests the tasks by date
     * @param date date of completed tasks
     * @return list of tasks
     */
    suspend fun getTasksByDate(date: String) : List<Task>? {
        val client : HttpClient by inject()
        val response : HttpResponse = client.get(searchTasksByDateUrl(date))

        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }
    }

    /** Request the tasks by type and date
     * @param type Type of task
     * @param date Date of task
     * @return Task or null if not found
     */
    suspend fun getTask(date: String, type: String): Task? {
        val client : HttpClient by inject()
        val response : HttpResponse = client.get(searchTasksByTypeAndDateUrl(type, date))
        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> null
            HttpStatusCode.BadRequest -> null
            else -> throw Exception("Server Error")
        }
    }

    /** requests a task by type
     * @param type Task type to request
     * @return list of tasks with given name
     */
    suspend fun getTasksByType(type: String) : List<Task>? {
        val client: HttpClient by inject()
        val response : HttpResponse = client.get(searchTasksByTypeUrl(type))

        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }
    }
}
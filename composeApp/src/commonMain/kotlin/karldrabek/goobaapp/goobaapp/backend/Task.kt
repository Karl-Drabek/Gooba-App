package karldrabek.goobaapp.goobaapp.backend

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import karldrabek.goobaapp.goobaapp.clearTasksUrl
import karldrabek.goobaapp.goobaapp.deleteTasksUrl
import karldrabek.goobaapp.goobaapp.getTasksUrl
import karldrabek.goobaapp.goobaapp.postTasksUrl
import karldrabek.goobaapp.goobaapp.putTasksUrl
import karldrabek.goobaapp.goobaapp.searchTaskUrl
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
     * @return True upon successful deletion
     */
    suspend fun removeTask(type: String) : Boolean {
        // Server expects JSON task
        val bufferTask = Task(type=type, userID=0, date="", time="")
        val client : HttpClient by inject()
        val response : HttpResponse = client.delete(deleteTasksUrl) {
            contentType(ContentType.Application.Json)
            setBody(bufferTask)
        }

        return response.status == HttpStatusCode.OK
    }

    /** Gets all completed tasks from the DB
     * @return List of tasks
     */
    suspend fun getAllTasks() : List<Task> {
        val client : HttpClient by inject()
        return client.get(getTasksUrl).body()
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

    /** requests a task
     * @param type Task type to request
     * @return the task
     */
    suspend fun getTask(type: String) : Task? {
        val client: HttpClient by inject()
        val response : HttpResponse = client.get(searchTaskUrl(type))

        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }
    }
}
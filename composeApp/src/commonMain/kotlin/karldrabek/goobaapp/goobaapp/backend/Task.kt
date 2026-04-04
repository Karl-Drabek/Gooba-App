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
import karldrabek.goobaapp.goobaapp.utils.EventCompletedData
import karldrabek.goobaapp.goobaapp.utils.TaskType
import karldrabek.goobaapp.goobaapp.utils.getDateAndTimeAsString
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Instant

// TODO : CRACK KEARNAN HELLA (HE NEVER GONNA SEE THIS FR)
// TODO : check the returns for errors

/** Stores data for a DB task request
 * @param type Activity ex Feeding/Scooping
 * @param userID associated ID of the user that completed the task
 * @param date String of the current date
 * @param time String of the current time
 */
@Serializable
data class Task(
    val type: String,
    val userID: Int,
    val date: String,
    val time: String,
) {
    /** used for alternate constructions */
    companion object {
        fun from(
            type: TaskType,
            user: User,
            time: Instant,
        ): Task {
            val dateTime = getDateAndTimeAsString(time)
            return Task(type.toString(), user.id, dateTime.date, dateTime.time)
        }

        fun from(
            type: TaskType,
            data: EventCompletedData,
        ): Task {
            val dateTime = getDateAndTimeAsString(data.time)
            return Task(type.toString(), data.user.id, dateTime.date, dateTime.time)
        }
    }
}

/** Don't instantiate. can be used to call various server functions */
object TaskRemoteManager : KoinComponent {
    /**
     * Registers a task to the DB
     *
     * @param task Task to add to the DB
     * @return true if the task was successfully added, false otherwise
     */
    suspend fun addTask(task: Task): Boolean {
        val client: HttpClient by inject()

        val response: HttpResponse =
            client.post(POST_TASKS_URL) {
                contentType(ContentType.Application.Json)
                setBody(task)
            }

        return response.status == HttpStatusCode.OK
    }

    /**
     * Deletes a task from the DB
     *
     * @param type the type of the task
     * @param date date task was completed
     * @return True upon successful deletion, false otherwise
     */
    suspend fun removeTask(
        type: String,
        date: String,
    ): Boolean {
        val client: HttpClient by inject()
        val response: HttpResponse = client.delete(deleteTasksUrl(date, type))
        return response.status == HttpStatusCode.OK
    }

    /**
     * Gets all completed tasks from the DB
     *
     * @return List of tasks. If there are no tasks this will be an empty list. If there is an error this returns null.
     */
    suspend fun getAllTasks(): List<Task>? {
        val client: HttpClient by inject()
        return client.get(GET_TASKS_URL).body()
    }

    /**
     * =======================================
     *                  WARNING
     *  =======================================
     *     CLEARS ALL TASKS IN THE DATABASE
     *
     * @return true after successful clear, otherwise false
     */
    suspend fun clearAllTasks(): Boolean {
        val client: HttpClient by inject()
        val response: HttpResponse = client.delete(CLEAR_TASKS_URL)
        return response.status == HttpStatusCode.OK
    }

    /**
     * Updates a task in the database
     *
     * @param task task with updated information
     * @return True if the task was successfully updated. false if there was not a task with the same type and date in the database which could be updated or there was another error.
     */
    suspend fun updateTask(task: Task): Boolean {
        val client: HttpClient by inject()
        val response: HttpResponse =
            client.put(PUT_TASKS_URL) {
                contentType(ContentType.Application.Json)
                setBody(task)
            }

        return response.status == HttpStatusCode.OK
    }

    /**
     * Requests the tasks by date
     *
     * @param date date of completed tasks
     * @return list of tasks. empty if there are no such tasks and null if there is an error.
     */
    suspend fun getTasksByDate(date: String): List<Task>? {
        val client: HttpClient by inject()
        val response: HttpResponse = client.get(searchTasksByDateUrl(date))

        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }
    }

    /** Request the tasks by type and date
     *
     * @param type Type of task
     * @param date Date of task
     * @return the task of the requested date and type. If the task is not found or there is an error this returns null.
     */
    suspend fun getTask(
        date: String,
        type: String,
    ): Task? {
        val client: HttpClient by inject()
        val response: HttpResponse = client.get(searchTasksByTypeAndDateUrl(type, date))
        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> null
            HttpStatusCode.BadRequest -> null
            else -> throw Exception("Server Error")
        }
    }

    /**
     * requests a list of tasks by type
     *
     * @param type Task type to request
     * @return list of tasks with given name. If no tasks of the given name are found this returns an empty list. If there is an error in the retrieval this returns null.
     */
    suspend fun getTasksByType(type: String): List<Task>? {
        val client: HttpClient by inject()
        val response: HttpResponse = client.get(searchTasksByTypeUrl(type))

        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            null
        }
    }
}

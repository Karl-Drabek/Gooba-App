package karldrabek.goobaapp.goobaapp.backend

import karldrabek.goobaapp.goobaapp.backend.TaskRemoteManager.getTasksByDate
import karldrabek.goobaapp.goobaapp.utils.EventCompletedData
import karldrabek.goobaapp.goobaapp.utils.TaskCompletionDay
import karldrabek.goobaapp.goobaapp.utils.TaskType
import karldrabek.goobaapp.goobaapp.utils.getDateAndTimeAsString
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Instant

// TODO delete this file

val taskManager = TaskRemoteManager
val userManager = UserRemoteManager

/** Constructs a task for the current time and date
 *
 * @param user User hat completed the task
 * @param taskType Task Type that was completed
 * @return Task object at current time and date
 */
suspend fun getTaskAtCurrentTime(
    user : User,
    taskType: TaskType
) : Task {
    val dateAndTime = getDateAndTimeAsString()
    val task =
        Task(
            type = taskType.toString(),
            userID = user.id,
            time = dateAndTime.date,
            date = dateAndTime.time,
        )

    return task
}

/**
 *
 * Sends a completed feed task to the database
 * @param user User that completed the task
 * @param task The completed task
 * @return True if the task was correctly added, otherwise false
 */
suspend fun feed(
    user: User,
    task: TaskType
) : Boolean = taskManager.addTask(getTaskAtCurrentTime(user, task))


/** Marks off the Scoop task with the given user
 *
 * @param user User who scooped
 * @return True if scoop day was checked off, otherwise false */

suspend fun scoop(
    user: User
) : Boolean {
    // Check the day
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    //TODO: Warning result function
    //if (user.scoopDay.uppercase() != today.dayOfWeek.toString().uppercase()) if (!warningResult) return false

    return taskManager.addTask(
        getTaskAtCurrentTime(user, TaskType.SCOOP_POOP)
    )
}

/**
 * Edits a task with the same taskID to a new user and time, sets time to time this function was called
 *
 * @param type the ID associated with the task
 * @param userID the ID associated with the new user who will be marked as having completed the task
 * @param date the date to be changed
 * @return True if task was successfully edited, otherwise false
 */
suspend fun editTask(
    type: String,
    userID: Int,
    date: String,
) : Boolean {
    val dateAndTime = getDateAndTimeAsString()
    val bufferTask =
        Task(
            type = type,
            userID = userID,
            date = date,
            time = dateAndTime.time,
        )

    // Update the task on the DB
    return taskManager.updateTask(bufferTask)
}

/**
 * deletes a task completion from the database entirely
 *
 * @param type the taskID associated with the task which will be deleted
 * @param date the date of task to be deleted
 * @return True on successful delete, false otherwise
 */
suspend fun deleteTask(
    type: String,
    date: String,
) : Boolean = taskManager.removeTask(type, date)


// TODO
/**
 * Gets a TaskCompletionDay with the three events in a day, possibly null
 *
 * @param date Date that should be queried, today's date by default
 * @return a TaskCompletionDay with the events of the day. They may be null.
 */
suspend fun getTasksDay(date : String = getDateAndTimeAsString().date): TaskCompletionDay {
    // TODO
    // Also consider other more efficient ways to get the tasks.
    // it might be faster to just pull everything and then sort it on
    // the clients side, or to offer different functions for how much
    // we desire to pull

    // TODO For scalability, task completion day should be a list of tasks abstracted from our use case

    // Get the tasks
    val tasksByDate : List<Task>? = taskManager.getTasksByDate(date)
    if(tasksByDate.isNullOrEmpty()) return TaskCompletionDay(null, null, null)
    var eventTasks : List<EventCompletedData> = mutableListOf()

    // TODO: Either Change implementation of EventCompletedData to be userId and type : String, or convert id to user
    for (task in tasksByDate) eventTasks += EventCompletedData(task.userID, task.time)

}

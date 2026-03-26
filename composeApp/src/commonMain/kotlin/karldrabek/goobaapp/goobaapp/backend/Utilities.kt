package karldrabek.goobaapp.goobaapp.backend

import karldrabek.goobaapp.goobaapp.utils.GoobaTask
import karldrabek.goobaapp.goobaapp.utils.TaskCompletionDay
import karldrabek.goobaapp.goobaapp.backend.Task
import karldrabek.goobaapp.goobaapp.backend.*
import kotlinx.datetime.*
import kotlin.time.Instant
import kotlin.time.Clock

val taskManager = TaskRemoteManager
val userManager = UserRemoteManager

fun getDateAndTimeAsString(): Pair<String, String> {
    val instant = Clock.System.now()
    val systemTimeZone = TimeZone.currentSystemDefault()
    val dateAndTime = instant.toLocalDateTime(systemTimeZone)
    val date = "${dateAndTime.day} : ${dateAndTime.month} : ${dateAndTime.year}"
    val time = "${dateAndTime.hour} : ${dateAndTime.minute} : ${dateAndTime.second}"
    return Pair(date, time)
}

/**
 *
 * Sends a completed feed task to the database
 * @param user User that completed the task
 * @param task The completed task
 */
suspend fun feed(user : User, task: GoobaTask) {

    // Crate Completed Task
    val dateAndTime = getDateAndTimeAsString()
    val dbTask = Task(
        type=task.toString(),
        userID=user.id,
        time=dateAndTime.second,
        date=dateAndTime.first
    )

    // Send to DB
    taskManager.addTask(dbTask)
}

// TODO
fun scoop(user: User) {

    // Check the day
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    if (user.scoopDay.uppercase() != today.dayOfWeek.toString().uppercase()) {
        // Prompt warning, return early if no
    }
}

/**
 * Edits a task with the same taskID to a new user and time, sets time to time this function was called
 *
 * @param type the ID associated with the task
 * @param userID the ID associated with the new user who will be marked as having completed the task
 * @param date the date to be changed
 */
suspend fun editTask(type: String, userID: Int, date: String){

    val dateAndTime = getDateAndTimeAsString()
    val bufferTask = Task(
        type=type,
        userID=userID,
        date=date,
        time=dateAndTime.second,
    )

    // Update the task on the DB
    taskManager.updateTask(bufferTask)
}

/**
 * deletes a task completion from the database entirely
 *
 * @param type the taskID associated with the task which will be deleted
 * @param date the date of task to be deleted
 */
suspend fun deleteTask(type: String, date: String) = taskManager.removeTask(type, date)


/**
 * Gets a TaskCompletionDay with the three events in a day, possibly null
 *
 * @param time time in the day which should be queried
 * @return a TaskCompletionDay with the events of the day. They may be null.
 */
fun getTasksDay(time: Instant): TaskCompletionDay {
    //TODO
    // Also consider other more efficient ways to get the tasks.
    // it might be faster to just pull everything and then sort it on
    // the clients side, or to offer different functions for how much\
    // we desire to pull
    return TaskCompletionDay(null, null, null)
}

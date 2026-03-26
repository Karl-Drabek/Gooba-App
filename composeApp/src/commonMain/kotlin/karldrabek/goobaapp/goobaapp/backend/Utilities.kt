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



fun getDateAndTimeAsString(time: Instant): Pair<String, String> {
    val systemTimeZone = TimeZone.currentSystemDefault()
    val dateAndTime = time.toLocalDateTime(systemTimeZone)
    val date = "${dateAndTime.day} : ${dateAndTime.month} : ${dateAndTime.year}"
    val time = "${dateAndTime.hour} : ${dateAndTime.minute} : ${dateAndTime.second}"
    return Pair(date, time)
}

/** @WARNING This needs to be launched in coroutine context
 *
 * Sends a completed feed task to the database
 * @param user User that completed the task
 * @param task The completed task
 */
suspend fun feed(user : User, task: GoobaTask) {
    val dateAndTime = getDateAndTimeAsString(Clock.System.now())
    val dbTask = Task(
        type=task.toString(),
        userID=user.id,
        time=dateAndTime.second,
        date=dateAndTime.first
    )

    taskManager.addTask(dbTask)
}

fun scoop(user: User) {
    // Check the day
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    if (user.scoopDay.uppercase() != today.dayOfWeek.toString().uppercase()) {
        // Prompt warning, return early if no
    }
}

/**
 * Edits a task with the same taskID to a new user and time
 *
 * @param taskID the ID associated with the task
 * @param userID the ID associated with the new user who will be marked as having completed the task
 * @param time the new time at which the task was completed
 * @TODO @param date
 */
fun editTask(taskID: Int, userID: Int, time: Instant){
    // TODO
}

/**
 * deletes a task completion from the database entirely
 *
 * @param taskID the taskID associated with the task which will be deleted
 */
fun deleteTask(taskID: Int){
    // TODO
}

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

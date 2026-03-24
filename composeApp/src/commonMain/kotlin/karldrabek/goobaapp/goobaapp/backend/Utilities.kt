package karldrabek.goobaapp.goobaapp.backend

import karldrabek.goobaapp.goobaapp.utils.Task
import karldrabek.goobaapp.goobaapp.utils.TaskCompletionDay
import kotlinx.datetime.*
import kotlin.time.Instant
import kotlin.time.Clock

enum class Mealtime {
    EVENING,
    MORNING
}

fun feed(user : User, mealtime: Mealtime, time: Instant = Clock.System.now()) {
    // TODO: add entry to the DB
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

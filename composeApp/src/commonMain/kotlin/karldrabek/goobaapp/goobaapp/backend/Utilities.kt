package karldrabek.goobaapp.goobaapp.backend

import karldrabek.goobaapp.goobaapp.utils.Task
import karldrabek.goobaapp.goobaapp.utils.TaskCompletionDay
import kotlin.time.Instant

/**
 * registers the name under a new user ID in the server or returns
 * the existing user ID if it exists.
 *
 * @param name the name to register or fetch
 * @return the user ID for the name
 */
fun registerUserID(name: String): Int {
    //TODO
    return -1
}

/**
 * Edits the user to have a new name or gooba schedule.
 *
 * @param userID the userID which will be edited
 * @param user the new name and gooba schedule for the user
 */
fun editUser(userID: Int, user: User){
    // TODO
}

/**
 * Makes the user as deleted in the database. This means they will not be
 * displayed as assigned for their gooba day, and cannot be selected in a
 * user dropdown menu, but their previous tasks will still be associated
 * with their name, and they will still exist in the database.
 *
 * @param userID the userID which will be deleted
 */
fun deleteUser(userID: Int){
    // TODO
}

/**
 * Gets a list of all the non deleted users in the database
 *
 * @return a list containing all the users in the database
 */
fun getUsers():List<User>{
    //TODO
    return listOf()
}

/**
 * completes a task under the current userID at a particular time.
 *
 * @param userID the userID associated with the user completing the task
 * @param task the type of task which was completed
 * @param time the time that the task was completed
 * @return the unique task ID for the new completed task.
 */
fun completeTask(userID: Int, task: Task, time: Instant): Int{
    // TODO
    return -1
}

/**
 * Edits a task with the same taskID to a new user and time
 *
 * @param taskID the ID associated with the task
 * @param userID the ID associated with the new user who will be marked as having completed the task
 * @param time the new time at which the task was completed
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

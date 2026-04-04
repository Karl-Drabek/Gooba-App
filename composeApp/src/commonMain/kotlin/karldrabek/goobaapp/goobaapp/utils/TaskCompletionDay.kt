package karldrabek.goobaapp.goobaapp.utils

import karldrabek.goobaapp.goobaapp.backend.Task
import karldrabek.goobaapp.goobaapp.backend.User

/**
 * Stores the three tasks of the day with EventCompletedData. If any of the
 * tasks have not been completed they will be null
 *
 * @property morning the [EventCompletedData][karldrabek.goobaapp.goobaapp.utils.EventCompletedData] for the morning food
 * @property evening the [EventCompletedData][karldrabek.goobaapp.goobaapp.utils.EventCompletedData] for the evening food
 * @property poop the [EventCompletedData][karldrabek.goobaapp.goobaapp.utils.EventCompletedData] for the litter-box
 */
data class TaskCompletionDay(
    val morning: EventCompletedData? = null,
    val evening: EventCompletedData? = null,
    val poop: EventCompletedData? = null,
) {
    fun updateTaskCopy(
        task: Task,
        user: User,
    ): TaskCompletionDay? {
        val eventCompletedData = EventCompletedData(user, textToTime(task.time))
        return when (task.type) {
            TaskType.MORNING_FOOD.toString() -> copy(morning = eventCompletedData)
            TaskType.EVENING_FOOD.toString() -> copy(evening = eventCompletedData)
            TaskType.SCOOP_POOP.toString() -> copy(poop = eventCompletedData)
            else -> null
        }
    }

    fun deleteTaskCopy(task: Task): TaskCompletionDay? =
        when (task.type) {
            TaskType.MORNING_FOOD.toString() -> copy(morning = null)
            TaskType.EVENING_FOOD.toString() -> copy(evening = null)
            TaskType.SCOOP_POOP.toString() -> copy(poop = null)
            else -> null
        }
}

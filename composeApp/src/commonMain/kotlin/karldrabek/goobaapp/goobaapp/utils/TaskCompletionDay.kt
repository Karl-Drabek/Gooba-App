package karldrabek.goobaapp.goobaapp.utils

/**
 * Stores the three tasks of the day with EventCompletedData. If any of the
 * tasks have not been completed they will be null
 *
 * @
 *
 */
data class TaskCompletionDay(
    val morning: EventCompletedData?,
    val evening: EventCompletedData?,
    val poop: EventCompletedData?,
)

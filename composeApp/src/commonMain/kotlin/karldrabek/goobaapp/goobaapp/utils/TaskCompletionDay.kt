package karldrabek.goobaapp.goobaapp.utils

/**
 * Stores the three tasks of the day with EventCompletedData. If any of the
 * tasks have not been completed they will be null
 *
 * @property morning the [EventCompletedData][karldrabek.goobaapp.goobaapp.utils.EventCompletedData] for the morning food
 * @property evening the [EventCompletedData][karldrabek.goobaapp.goobaapp.utils.EventCompletedData] for the evening food
 * @property poop the [EventCompletedData][karldrabek.goobaapp.goobaapp.utils.EventCompletedData] for the litter-box
 */
data class TaskCompletionDay(
    val morning: EventCompletedData?,
    val evening: EventCompletedData?,
    val poop: EventCompletedData?,
)

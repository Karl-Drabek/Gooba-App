package karldrabek.goobaapp.goobaapp.utils

import karldrabek.goobaapp.goobaapp.backend.User
import kotlin.time.Instant

/**
 * Data held by an element associated with a particular [taskType][karldrabek.goobaapp.goobaapp.utils.TaskType].
 * along with the task type it can be reconstructed into a [Task][karldrabek.goobaapp.goobaapp.backend.Task] with [Task.from()][karldrabek.goobaapp.goobaapp.backend.Task.from]
 *
 * @property user the user who completed the task
 * @property time the instant the task was completed
 */
data class EventCompletedData(
    val user: User,
    val time: Instant,
)

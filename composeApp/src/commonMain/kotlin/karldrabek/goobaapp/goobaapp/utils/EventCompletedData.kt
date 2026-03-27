package karldrabek.goobaapp.goobaapp.utils

import karldrabek.goobaapp.goobaapp.backend.User
import kotlin.time.Instant

data class EventCompletedData(val user: User, val time: Instant) {
}
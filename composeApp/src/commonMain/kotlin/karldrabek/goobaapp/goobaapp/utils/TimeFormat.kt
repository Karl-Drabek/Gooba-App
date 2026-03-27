package karldrabek.goobaapp.goobaapp.utils

/**
 * Used for the time dropdown to keep track of elements which compose the time for a particular date
 *
 * @property hour the hour 1-12
 * @property minute the minute 0-59
 * @property amPm AM or PM
 */
data class TimeFormat(
    val hour: Int,
    val minute: Int,
    val amPm: String,
)

package karldrabek.goobaapp.goobaapp.ui.utils

/**
 * Displays a distinct time selection dialog for android and ios users
 * From Mohammad Juned at [kotlinguide.com](https://www.kotlinguide.com/timepicker-in-kotlin-compose-multiplatform-kmp-kmm/)
 *
 *  @param context
 *  @param onTimePicked when the time is selected it calls onTimePicked with the current time in HH:MM
 */
expect fun pickTime(
    context: Any?,
    onTimePicked: (String) -> Unit,
)

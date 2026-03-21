package karldrabek.goobaapp.goobaapp

import kotlinx.datetime.*

// Or something along those lines
data class serverMessage(
    val user: User,
    val time: Instant = Clock.System.now(),
    val message: String = ""
)

fun feed(user : User) {

    // Remove UI element

    // Send the serverMessage to the server
}

fun scoop(user: User) {
    // Check the day
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    if (user.scoopDay != today.dayOfWeek){
        // Prompt warning
    }


}
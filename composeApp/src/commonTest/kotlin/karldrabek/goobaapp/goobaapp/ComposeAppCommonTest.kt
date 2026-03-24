package karldrabek.goobaapp.goobaapp

// Imports
import karldrabek.goobaapp.goobaapp.backend.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.*
import org.koin.core.component.KoinComponent

class randomUser(){
    val names = listOf<String>("Ashton", "Maddy", "Zak", "Luke", "Kearnan", "Karl")
    fun generateUser() : User {
        return User(name = names.random(), scoopDay = DayOfWeek.entries.random().toString().uppercase())
    }
}

class ComposeAppCommonTest {

    @Test
    fun userName() {
       val user = User(name = "Luke", scoopDay = "")
        assertEquals(user.name, "Luke")
    }

    @Test
    fun userScoopDay() {
        val user = User(name = "Luke", scoopDay = DayOfWeek.MONDAY.toString().uppercase())
        assertEquals(user.scoopDay, DayOfWeek.MONDAY.toString().uppercase())

        // Null check
        val nullUser = User(name = "Ashton", scoopDay = "")
        assertEquals(nullUser.scoopDay, "")

        // Change the scoop day
        user.scoopDay = DayOfWeek.FRIDAY.toString().uppercase()
        assertEquals(user.scoopDay, DayOfWeek.FRIDAY.toString().uppercase())
    }

}
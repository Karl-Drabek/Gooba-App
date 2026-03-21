package karldrabek.goobaapp.goobaapp

// Imports
import karldrabek.goobaapp.goobaapp.backend.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.*

class randomUser(){
    val names = listOf<String>("Ashton", "Maddy", "Zak", "Luke", "Kearnan", "Karl")
    fun generateUser() : User {
        return User(name = names.random(), scoopDay = DayOfWeek.entries.random())
    }
}

class ComposeAppCommonTest {

    @Test
    fun userName() {
       val user = User(name = "Luke", scoopDay = null)
        assertEquals(user.name, "Luke")
    }

    @Test
    fun userScoopDay() {
        val user = User(name = "Luke", scoopDay = DayOfWeek.MONDAY)
        assertEquals(user.scoopDay, DayOfWeek.MONDAY)

        // Null check
        val nullUser = User(name = "Ashton", scoopDay = null)
        assertEquals(nullUser.scoopDay, null)

        // Change the scoop day
        user.scoopDay = DayOfWeek.FRIDAY
        assertEquals(user.scoopDay, DayOfWeek.FRIDAY)
    }
}
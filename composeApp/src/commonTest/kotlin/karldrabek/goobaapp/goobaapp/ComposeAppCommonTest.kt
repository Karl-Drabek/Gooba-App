package karldrabek.goobaapp.goobaapp

import karldrabek.goobaapp.goobaapp.backend.User
import kotlinx.datetime.DayOfWeek
import kotlin.test.Test
import kotlin.test.assertEquals

class RandomUser {
    val names = listOf("Ashton", "Maddy", "Zak", "Luke", "Kearnan-Super-Gay-Butt-Fucker", "Karl")

    fun generateUser(): User =
        User(
            name = names.random(),
            scoopDay =
                DayOfWeek.entries
                    .random()
                    .toString()
                    .uppercase(),
        )
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

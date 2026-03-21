package karldrabek.goobaapp.goobaapp

// Imports
import kotlin.test.Test
import kotlin.test.assertEquals

class randomUser(){
    val names = listOf<String>("Ashton", "Maddy", "Zak", "Luke", "Kearnan", "Karl")
    fun generateUser() : User {
        return User(name=names.random(),scoopDay=Day.entries.random())
    }
}

class ComposeAppCommonTest {

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun userName() {
       val user = User(name="Luke", scoopDay=null)
        assertEquals(user.name, "Luke")
    }

    @Test
    fun userScoopDay() {
        val user = User(name="Luke", scoopDay=Day.MONDAY)
        assertEquals(user.scoopDay, Day.MONDAY)

        // Null check
        val nullUser = User(name="Ashton", scoopDay=null)
        assertEquals(nullUser.scoopDay, null)

        // Change the scoop day
        user.scoopDay = Day.FRIDAY
        assertEquals(user.scoopDay, Day.FRIDAY)
    }
}
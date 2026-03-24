package karldrabek.goobaapp.goobaapp

import karldrabek.goobaapp.goobaapp.backend.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.*
import org.koin.core.component.KoinComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import org.koin.test.KoinTest
import kotlin.test.BeforeTest
import kotlin.test.assertTrue


class DataBaseTests : KoinTest {

    private val remoteManager = UserRemoteManager
    // Warning!! CURRENT IMPLEMENTATION CLEARS ALL USERS BEFORE TESTING FOR DEBUGGING
    @BeforeTest
    fun setup() {

        startKoin {
            modules(networkModule)
        }

        runTest {
            remoteManager.clearAllUsers()
        }
    }

    @AfterTest
    fun tearDown() {

        runTest {
            remoteManager.clearAllUsers()
        }

        stopKoin()
    }

    /** Tests most DB functions */
    @Test
    fun userTest () = runTest {

        // Create a random user
        // Add them to the db
        var ashUser = remoteManager.registerUser(User(name = "Ashton"))
        assertEquals("Ashton", ashUser?.name, message = "Register User Test 2 ")

        // Verify users were added
        var users: List<User>? = remoteManager.getAllUsers()
        assertTrue(!users.isNullOrEmpty() && users.size == 1, message = "Get Users Test 1")

        // Add another user
        val lukeUser = remoteManager.registerUser(User(name="Luke"))
        assertEquals("Luke", lukeUser?.name, message = "Register User Test 2 ")

        // Verify users were added
        users = remoteManager.getAllUsers()
        assertTrue(!users.isNullOrEmpty() && users.size == 2, message = "Get Users Test 2 ")

        if(lukeUser != null) {
            // Try to add the same user twice
            remoteManager.registerUser(lukeUser)
        }

        users = remoteManager.getAllUsers()
        assertTrue(!users.isNullOrEmpty() && users.size == 2, message = "Repeat user test")

        // Delete a user
        val success: Boolean = remoteManager.deleteUser(lukeUser)
        assertTrue(success, message = "Delete User Test")

        // Verify user is not there
        if(lukeUser != null) {
            // Try to add the same user twice
            users = remoteManager.searchUser(lukeUser)
            assertTrue(users.isNullOrEmpty(), message = "Search for deleted user")
        }

        // Verify Ashton is there
        if(ashUser != null) {

            users = remoteManager.searchUser(ashUser)
            assertTrue(!users.isNullOrEmpty(), message = "Search for existing user")
        }

        // Change Ashtons information
        ashUser?.scoopDay = "WEDNESDAY"
        ashUser?.name = "Karl"

        if(ashUser != null) {
            ashUser = remoteManager.updateUser(ashUser)
        }

        assertEquals(ashUser?.name, "Karl", message = "Put Test 1")
        assertEquals(ashUser?.scoopDay, "WEDNESDAY", message = "Put Test 2")

    }
}
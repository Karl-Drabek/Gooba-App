package karldrabek.goobaapp.goobaapp

import karldrabek.goobaapp.goobaapp.backend.*
import kotlin.test.Test
import kotlin.test.*
import kotlinx.datetime.*
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import org.koin.test.KoinTest
import kotlin.test.BeforeTest


class DataBaseTests : KoinTest {

    private val userRemoteManager = UserRemoteManager
    private val taskRemoteManager = TaskRemoteManager
    // Warning!! CURRENT IMPLEMENTATION CLEARS ALL USERS BEFORE TESTING FOR DEBUGGING
    @BeforeTest
    fun setup() {

        startKoin {
            modules(networkModule)
        }


        runTest {
            taskRemoteManager.clearAllTasks()
            userRemoteManager.clearAllUsers()
        }
    }

    @AfterTest
    fun tearDown() {

        runTest {
            taskRemoteManager.clearAllTasks()
            userRemoteManager.clearAllUsers()
        }

        stopKoin()
    }

    /** Tests most DB functions */
    @Test
    fun userClientTest () = runTest {

        // Create a random user
        // Add them to the db
        var ashUser = userRemoteManager.registerUser(User(name = "Ashton"))
        assertEquals("Ashton", ashUser?.name, message = "Register User Test 2 ")

        // Verify users were added
        var users: List<User>? = userRemoteManager.getAllUsers()
        assertTrue(!users.isNullOrEmpty() && users.size == 1, message = "Get Users Test 1")

        // Add another user
        val lukeUser = userRemoteManager.registerUser(User(name="Luke"))
        assertEquals("Luke", lukeUser?.name, message = "Register User Test 2 ")

        // Verify users were added
        users = userRemoteManager.getAllUsers()
        assertTrue(!users.isNullOrEmpty() && users.size == 2, message = "Get Users Test 2 ")

        if(lukeUser != null) {
            // Try to add the same user twice
            userRemoteManager.registerUser(lukeUser)
        }

        users = userRemoteManager.getAllUsers()
        assertTrue(!users.isNullOrEmpty() && users.size == 2, message = "Repeat user test")

        // Delete a user
        val success: Boolean = userRemoteManager.deleteUser(lukeUser)
        assertTrue(success, message = "Delete User Test")

        // Verify user is not there
        if(lukeUser != null) {
            // Try to add the same user twice
            users = userRemoteManager.searchUser(lukeUser)
            assertTrue(users.isNullOrEmpty(), message = "Search for deleted user")
        }

        // Verify Ashton is there
        if(ashUser != null) {

            users = userRemoteManager.searchUser(ashUser)
            assertTrue(!users.isNullOrEmpty(), message = "Search for existing user")
        }

        // Change Ashtons information
        ashUser?.scoopDay = "WEDNESDAY"
        ashUser?.name = "Karl"

        if(ashUser != null) {
            ashUser = userRemoteManager.updateUser(ashUser)
        }

        assertEquals(ashUser?.name, "Karl", message = "Put Test 1")
        assertEquals(ashUser?.scoopDay, "WEDNESDAY", message = "Put Test 2")

    }

    @Test
    fun taskClientTest () = runTest {
        // Add some users to the DB
        val ashUser = userRemoteManager.registerUser(User(name = "Ashton"))
        val lukeUser = userRemoteManager.registerUser(User(name = "Luke"))

        // Verify no Tasks are on the DB
        var tasksOnDB = taskRemoteManager.getAllTasks()
        assertTrue(tasksOnDB.isEmpty(), message = "Get Tasks Test 1")

        // Add a task to the DB
        val result = taskRemoteManager.addTask(Task(
            type="Morning Food",
            userID = ashUser!!.id,
            time="TEMP",
            date="TEMP"
        ))

        assertTrue(result)

        // Verify there is a task on the DB
        tasksOnDB = taskRemoteManager.getAllTasks()
        assertTrue(!tasksOnDB.isEmpty(), message = "Get Tasks Test 2")

        // Get the task from the DB
        val task = taskRemoteManager.getTask(type="Morning Food")

        // Verify the task is non-null
        assertNotNull(task)

        // Verify task is correct
        assertEquals(task.type, "Morning Food")
        assertEquals(task.date, "TEMP")
        assertEquals(task.time, "TEMP")
        assertEquals(task.userID, ashUser.id)

        // Update the task on the DB
        val newTask = Task(
            type="Morning Food",
            userID = lukeUser!!.id,
            time="RN",
            date="RN"
        )

        // Send the task to the DB
        val updateResult = taskRemoteManager.updateTask(newTask)
        assertTrue(updateResult)

        // Retrieve the task
        val returnedTask = taskRemoteManager.getTask(type="Morning Food")

        assertNotNull(returnedTask)

        // Verify information
        assertEquals(returnedTask.type, "Morning Food")
        assertEquals(returnedTask.date, "RN")
        assertEquals(returnedTask.time, "RN")
        assertEquals(returnedTask.userID, lukeUser.id)

        // Delete the task from the DB
        taskRemoteManager.removeTask("Morning Food")

        // Verify that searching for the task returns null
        assertNull(taskRemoteManager.getTask(type="Morning Food"))

        // Verify the database is empty
        val taskList = taskRemoteManager.getAllTasks()
        assertTrue(taskList.isEmpty())

    }
}
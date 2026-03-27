package karldrabek.goobaapp.goobaapp

import karldrabek.goobaapp.goobaapp.backend.Task
import karldrabek.goobaapp.goobaapp.backend.TaskRemoteManager
import karldrabek.goobaapp.goobaapp.backend.User
import karldrabek.goobaapp.goobaapp.backend.UserRemoteManager
import karldrabek.goobaapp.goobaapp.backend.networkModule
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
    fun userClientTest() =
        runTest {
            // Create a random user
            // Add them to the db
            var ashUser = userRemoteManager.registerUser(User(name = "Ashton"))
            assertEquals("Ashton", ashUser?.name, message = "Register User Test 2 ")

            // Verify users were added
            var users: List<User>? = userRemoteManager.getAllUsers()
            assertTrue(!users.isNullOrEmpty() && users.size == 1, message = "Get Users Test 1")

            // Add another user
            val lukeUser = userRemoteManager.registerUser(User(name = "Luke"))
            assertEquals("Luke", lukeUser?.name, message = "Register User Test 2 ")

            // Verify users were added
            users = userRemoteManager.getAllUsers()
            assertTrue(!users.isNullOrEmpty() && users.size == 2, message = "Get Users Test 2 ")

            if (lukeUser != null) {
                // Try to add the same user twice
                userRemoteManager.registerUser(lukeUser)
            }

            users = userRemoteManager.getAllUsers()
            assertTrue(!users.isNullOrEmpty() && users.size == 2, message = "Repeat user test")

            // Delete a user
            val success: Boolean = userRemoteManager.deleteUser(lukeUser!!.id)
            assertTrue(success, message = "Delete User Test")

            // Verify user is not there by id
            var user = userRemoteManager.searchUserById(lukeUser.id)
            assertEquals(user, null, "Search for deleted user")

            ashUser!!

            // Verify Ashton is there by name
            users = userRemoteManager.searchUsersByName(ashUser.name)
            users!!
            assertTrue(users.isNotEmpty(), message = "Search for existing user")

            // Search Ashton by id, verify non-null
            user = userRemoteManager.searchUserById(ashUser.id)
            assertNotNull(user, "Search for existing user")

            // Change Ashtons information
            ashUser.scoopDay = "WEDNESDAY"
            ashUser.name = "Karl"
            ashUser = userRemoteManager.updateUser(ashUser)

            ashUser!!

            assertEquals(ashUser.name, "Karl", message = "Put Test 1")
            assertEquals(ashUser.scoopDay, "WEDNESDAY", message = "Put Test 2")
        }

    @Test
    fun taskClientTest() =
        runTest {
            // Add some users to the DB
            val ashUser = userRemoteManager.registerUser(User(name = "Ashton"))
            val lukeUser = userRemoteManager.registerUser(User(name = "Luke"))

            // Verify no Tasks are on the DB
            var tasksOnDB = taskRemoteManager.getAllTasks()
            assertNull(tasksOnDB, message = "Get Tasks Test 1")

            // Add a task to the DB
            val result =
                taskRemoteManager.addTask(
                    Task(
                        type = "Morning Food",
                        userID = ashUser!!.id,
                        time = "TEMP",
                        date = "TEMP",
                    ),
                )

            assertTrue(result)

            // Verify there is a task on the DB
            tasksOnDB = taskRemoteManager.getAllTasks()
            tasksOnDB!!
            assertTrue(!tasksOnDB.isEmpty(), message = "Get Tasks Test 2")

            // Get the task from the DB
            val task = taskRemoteManager.getTask(type = "Morning Food", date = "TEMP")

            // Verify the task is non-null
            assertNotNull(task)

            // Verify task is correct
            assertEquals(task.type, "Morning Food")
            assertEquals(task.date, "TEMP")
            assertEquals(task.time, "TEMP")
            assertEquals(task.userID, ashUser.id)

            // Update the task on the DB
            val newTask =
                Task(
                    type = "Morning Food",
                    userID = lukeUser!!.id,
                    time = "RN",
                    date = "TEMP",
                )

            // Send the task to the DB
            val updateResult = taskRemoteManager.updateTask(newTask)
            assertTrue(updateResult)

            // Retrieve the task
            val returnedTask = taskRemoteManager.getTask(type = "Morning Food", date = "TEMP")

            assertNotNull(returnedTask)

            // Verify information
            assertEquals(returnedTask.time, "RN")
            assertEquals(returnedTask.userID, lukeUser.id)

            // Delete the task from the DB
            taskRemoteManager.removeTask("Morning Food", date = "TEMP")

            // Verify that searching for the task returns null
            assertNull(taskRemoteManager.getTask(type = "Morning Food", date = "TEMP"))

            // Verify the database is empty
            val taskList = taskRemoteManager.getAllTasks()
            assertNull(taskList)
        }
}

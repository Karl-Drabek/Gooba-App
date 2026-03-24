package karldrabek.goobaapp.goobaapp

import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.*
import org.koin.dsl.module
import kotlinx.serialization.json.Json
import io.ktor.http.*

/** search URLS for database calls */
val serverUrl = "http://127.0.0.1:1738"
val getUsersUrl = "$serverUrl/users"
val deleteUsersUrl = "$getUsersUrl"
val postUsersUrl = "$getUsersUrl"

val putUsersUrl = "$getUsersUrl"
val clearUsersUrl = "$getUsersUrl/admin/clear"
fun searchUserUrl(name : String) : String = "$getUsersUrl/search/${name.encodeURLParameter()}"

val getTasksUrl = "$serverUrl/tasks"
val deleteTasksUrl = "$getTasksUrl"
val postTasksUrl = "$getTasksUrl"
val putTasksUrl = "$getTasksUrl"
val clearTasksUrl = "$deleteTasksUrl/clear"

fun searchTaskUrl(type: String) : String = "$getTasksUrl/search/${type.encodeURLParameter()}"


val networkModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }
}
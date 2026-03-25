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
fun deleteUsersUrl(id: Int) = "$getUsersUrl/${id.toString().encodeURLParameter()}"
val postUsersUrl = "$getUsersUrl"

val putUsersUrl = "$getUsersUrl"

val searchUserUrl = "$getUsersUrl/search"
val clearUsersUrl = "$getUsersUrl/clear"

fun searchUserByNameUrl(name : String) : String = "$searchUserUrl/name/${name.encodeURLParameter()}"
fun searchUserByIdUrl(id: Int) : String = "$searchUserUrl/id/${id.toString().encodeURLParameter()}"

val getTasksUrl = "$serverUrl/tasks"

val searchTasksUrl = "$getTasksUrl/search"
fun deleteTasksUrl(date:String, type:String) = "$getTasksUrl/${date.encodeURLParameter()}/${type.encodeURLParameter()}"
val postTasksUrl = "$getTasksUrl"
val putTasksUrl = "$getTasksUrl"
val clearTasksUrl = "$getTasksUrl/clear"

fun searchTasksByTypeUrl(type: String) : String = "$searchTasksUrl/type/${type.encodeURLParameter()}"
fun searchTasksByDateUrl(date: String) : String = "$searchTasksUrl/date/${date.encodeURLParameter()}"
fun searchTasksByTypeAndDateUrl(type: String, date: String) = "$searchTasksUrl/${date.encodeURLParameter()}/${type.encodeURLParameter()}"

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
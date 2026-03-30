package karldrabek.goobaapp.goobaapp.backend

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.encodeURLParameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/** search URLS for database calls */
const val SERVER_URL = "http://192.168.0.87:1738"
const val GET_USERS_URL = "$SERVER_URL/users"

fun deleteUsersUrl(id: Int) = "$GET_USERS_URL/${id.toString().encodeURLParameter()}"

const val POST_USERS_URL = GET_USERS_URL

const val PUT_USERS_URL = GET_USERS_URL

const val SEARCH_USERS_URL = "$GET_USERS_URL/search"
const val CLEAR_USERS_URL = "$GET_USERS_URL/clear"

fun searchUserByNameUrl(name: String): String = "$SEARCH_USERS_URL/name/${name.encodeURLParameter()}"

fun searchUserByIdUrl(id: Int): String = "$SEARCH_USERS_URL/id/${id.toString().encodeURLParameter()}"

const val GET_TASKS_URL = "$SERVER_URL/tasks"

const val SEARCH_TASK_URL = "$GET_TASKS_URL/search"

fun deleteTasksUrl(
    date: String,
    type: String,
) = "$GET_TASKS_URL/${date.encodeURLParameter()}/${type.encodeURLParameter()}"

const val POST_TASKS_URL = GET_TASKS_URL
const val PUT_TASKS_URL = GET_TASKS_URL
const val CLEAR_TASKS_URL = "$GET_TASKS_URL/clear"

fun searchTasksByTypeUrl(type: String): String = "$SEARCH_TASK_URL/type/${type.encodeURLParameter()}"

fun searchTasksByDateUrl(date: String): String = "$SEARCH_TASK_URL/date/${date.encodeURLParameter()}"

fun searchTasksByTypeAndDateUrl(
    type: String,
    date: String,
) = "$SEARCH_TASK_URL/${date.encodeURLParameter()}/${type.encodeURLParameter()}"

val networkModule =
    module {
        single {
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            encodeDefaults = true
                        },
                    )
                }

                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
            }
        }
    }

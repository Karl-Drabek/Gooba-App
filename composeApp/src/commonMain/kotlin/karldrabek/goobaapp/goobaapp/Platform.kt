package karldrabek.goobaapp.goobaapp

/**
 * Platform tells the common which platform they are in and the build version
 */
interface Platform {
    /** The name and build version of the platform */
    val name: String
}

/**
 * Actual function which returns the Android platform only if it is an Android launch
 *
 * @return The current platform
 */
expect fun getPlatform(): Platform

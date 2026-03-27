package karldrabek.goobaapp.goobaapp

import android.os.Build

/**
 * Platform tells the common which platform they are in and the build version
 */
class AndroidPlatform : Platform {
    /** The name and build version of the platform */
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

/**
 * returns the current platform, ie android or IOS
 *
 * @return The android platform
 */
actual fun getPlatform(): Platform = AndroidPlatform()

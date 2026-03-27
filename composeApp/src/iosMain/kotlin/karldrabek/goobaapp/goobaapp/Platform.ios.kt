package karldrabek.goobaapp.goobaapp

import platform.UIKit.UIDevice

/**
 * Platform tells the common which platform they are in and the build version
 */
class IOSPlatform : Platform {
    /** The name and build version of the platform */
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

/**
 * Actual function which returns the ios platform only if it is an ios launch
 *
 * @return The ios platform
 */
actual fun getPlatform(): Platform = IOSPlatform()

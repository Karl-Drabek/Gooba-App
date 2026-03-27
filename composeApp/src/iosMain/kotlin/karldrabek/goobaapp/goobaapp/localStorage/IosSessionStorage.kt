package karldrabek.goobaapp.goobaapp.localStorage

import karldrabek.goobaapp.goobaapp.ui.localStorage.SessionStorage
import platform.Foundation.NSUserDefaults

/**
 * IOS specific implementation of the SessionStorage interface
 *
 * @property userDefaults the system for which we add and remove key pair values for IOS.
 */
class IosSessionStorage(
    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults,
) : SessionStorage {
    /**
     * Attempt to retrieve the saved user id value from its key
     *
     * @return the user id saved in the local storage if it exists, otherwise null.
     */
    override fun getSavedUserId(): Int? {
        val value = userDefaults.integerForKey("saved_user_id")
        return if (value == 0L && userDefaults.objectForKey("saved_user_id") == null) {
            null
        } else {
            value.toInt()
        }
    }

    /**
     * Saves the userId in the local storage for this device. if one already exists it will override it.
     *
     * @param userId the userID to be associate with the current device
     */
    override fun saveUserId(userId: Int) {
        userDefaults.setObject(userId, forKey = "saved_user_id")
    }

    /**
     * If there is a saved userID, this clears it from the local storage
     */
    override fun clearSavedUserId() {
        userDefaults.removeObjectForKey("saved_user_id")
    }
}

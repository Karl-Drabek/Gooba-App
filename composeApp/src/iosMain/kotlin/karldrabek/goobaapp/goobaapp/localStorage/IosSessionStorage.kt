package karldrabek.goobaapp.goobaapp.localStorage

import karldrabek.goobaapp.goobaapp.ui.localStorage.SessionStorage
import platform.Foundation.NSUserDefaults

class IosSessionStorage(
    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults
) : SessionStorage {

    override fun getSavedUserId(): Int? {
        val value = userDefaults.integerForKey("saved_user_id")
        return if (value == 0L && userDefaults.objectForKey("saved_user_id") == null) {
            null
        } else {
            value.toInt()
        }
    }

    override fun saveUserId(userId: Int) {
        userDefaults.setObject(userId, forKey = "saved_user_id")
    }

    override fun clearSavedUserId() {
        userDefaults.removeObjectForKey("saved_user_id")
    }
}
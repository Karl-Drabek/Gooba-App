package karldrabek.goobaapp.goobaapp.localStorage

import android.content.SharedPreferences
import androidx.core.content.edit
import karldrabek.goobaapp.goobaapp.ui.localStorage.SessionStorage

/**
 * Android specific implementation of the SessionStorage interface
 *
 * @property SharedPreferences the shared preferences for the app.
 */
class AndroidSessionStorage(
    private val sharedPreferences: SharedPreferences,
) : SessionStorage {
    /**
     * Attempt to retrieve the saved user id value from its key
     *
     * @return the user id saved in the local storage if it exists, otherwise null.
     */
    override fun getSavedUserId(): Int? =
        if (!sharedPreferences.contains("saved_user_id")) {
            null
        } else {
            sharedPreferences.getInt("saved_user_id", 0)
        }

    /**
     * Saves the userId in the local storage for this device. if one already exists it will override it.
     *
     * @param userId the userID to be associate with the current device
     */
    override fun saveUserId(userId: Int) {
        sharedPreferences.edit {
            putInt("saved_user_id", userId)
        }
    }

    /**
     * If there is a saved userID, this clears it from the local storage
     */
    override fun clearSavedUserId() {
        sharedPreferences.edit {
            remove("saved_user_id")
        }
    }
}

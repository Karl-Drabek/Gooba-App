package karldrabek.goobaapp.goobaapp.localStorage

import android.content.SharedPreferences
import androidx.core.content.edit
import karldrabek.goobaapp.goobaapp.ui.localStorage.SessionStorage

class AndroidSessionStorage(
    private val sharedPreferences: SharedPreferences
) : SessionStorage {

    override fun getSavedUserId(): Int? {
        return if (!sharedPreferences.contains("saved_user_id")){
            null
        }else{
            sharedPreferences.getInt("saved_user_id", 0)
        }
    }

    override fun saveUserId(userId: Int) {
        sharedPreferences.edit {
            putInt("saved_user_id", userId)
        }
    }

    override fun clearSavedUserId() {
        sharedPreferences.edit {
            remove("saved_user_id")
        }
    }
}
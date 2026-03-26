package karldrabek.goobaapp.goobaapp.ui.localStorage

interface SessionStorage {
    fun getSavedUserId(): Int?
    fun saveUserId(userId: Int)
    fun clearSavedUserId()
}
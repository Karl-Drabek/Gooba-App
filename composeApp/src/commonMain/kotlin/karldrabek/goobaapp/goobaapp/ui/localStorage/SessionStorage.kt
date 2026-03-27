package karldrabek.goobaapp.goobaapp.ui.localStorage

/**
 * Handles local storage on different platforms
 */
interface SessionStorage {
    /**
     * Attempt to retrieve the saved user id value from its key
     *
     * @return the user id saved in the local storage if it exists, otherwise null.
     */
    fun getSavedUserId(): Int?

    /**
     * Saves the userId in the local storage for this device. if one already exists it will override it.
     *
     * @param userId the userID to be associate with the current device
     */
    fun saveUserId(userId: Int)

    /**
     * If there is a saved userID, this clears it from the local storage
     */
    fun clearSavedUserId()
}

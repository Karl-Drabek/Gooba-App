package karldrabek.goobaapp.goobaapp.backend

import kotlinx.datetime.DayOfWeek
/**
 * User stores all data for the user that is stored in the DB.
 *
 * @property name the name of the User.
 * @property scoopDay the DayofWeek which the user is assigned to scoop the poop.
 * @constructor Creates a user with a name who is assigned to no day to scoop poop.
 */
data class User(var name: String, var scoopDay: DayOfWeek? = null)
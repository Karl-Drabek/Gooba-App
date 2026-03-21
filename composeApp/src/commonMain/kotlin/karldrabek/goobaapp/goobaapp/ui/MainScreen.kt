package karldrabek.goobaapp.goobaapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import kotlin.time.Instant
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import karldrabek.goobaapp.goobaapp.backend.User
import kotlin.time.Clock

/**
 * MainScreen handles the main screen for the gooba app
 * which displays buttons to indicate a user fed Gooba in the
 * morning, the afternoon, or they scooped her poop.
 *
 *  @property user the user currently logged in.
 *  @property onSettingsClick settings button is clicked.
 *  @property onHistoryClick history button is clicked.
 */
@Composable
fun MainScreen( user: User,
    onSettingsClick: () -> Unit,
    onHistoryClick: () -> Unit,
) {
    /** State */
    var morningFoodData: Pair<User, Instant>? by remember { mutableStateOf(null) }
    var eveningFoodData: Pair<User, Instant>? by remember { mutableStateOf(null) }
    var poopScoopData: Pair<User, Instant>? by remember { mutableStateOf(null) }

    /** Make read only so we can verify they are non-null*/
    val morningFoodDataSnap = morningFoodData
    val eveningFoodDataSnap = eveningFoodData
    val poopScoopDataSnap = poopScoopData

    Column(modifier = Modifier.padding(16.dp)) {

        if(morningFoodDataSnap == null) {
            Button(onClick = {
                morningFoodData = user to Clock.System.now()
            }) {
                Text("Feed Morning")
            }
        }else{
            Text("${morningFoodDataSnap.first} fed Gooba at ${morningFoodDataSnap.second}")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if(eveningFoodDataSnap == null) {
            Button(onClick = {
                eveningFoodData = user to Clock.System.now()
            }) {
                Text("Feed Evening")
            }
        }else{
            Text("${eveningFoodDataSnap.first} fed Gooba at ${eveningFoodDataSnap.second}")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if(poopScoopDataSnap == null) {
            Button(onClick = {
                poopScoopData = user to Clock.System.now()
            }) {
                Text("Scoop Poop")
            }
        }else{
            Text("${poopScoopDataSnap.first} scooped Gooba's poop at ${poopScoopDataSnap.second}")
        }
    }
}
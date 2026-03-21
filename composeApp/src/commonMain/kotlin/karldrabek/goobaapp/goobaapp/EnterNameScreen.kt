import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Column

@Composable
fun EnterScreenName() {
    // 1. Define the state for the name (saves it)
    var name by remember { mutableStateOf("") }

    Column {
        TextField(
            value = name,
            onValueChange = { name = it }, // 'it' is the single paramater to the labda
            label = { Text("Enter your name") }
        )

        Button(onClick = {
            if(checkName(name)){
                registerName(name)

            }

        }) {
            Text("Save")
        }
    }
}
package karldrabek.goobaapp.goobaapp

import EnterScreenName
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    if(true){
        EnterScreenName()
    }else{
        EnterMainScreen()
    }
}
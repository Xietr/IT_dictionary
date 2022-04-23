package gordeev.it_dictionary.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import com.google.accompanist.insets.ProvideWindowInsets
import gordeev.it_dictionary.presentation.screens.MainContainer
import gordeev.it_dictionary.presentation.theme.ItDictionaryTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ItDictionaryTheme {
                ProvideWindowInsets {
                    MainContainer()
                }
            }
        }
    }
}
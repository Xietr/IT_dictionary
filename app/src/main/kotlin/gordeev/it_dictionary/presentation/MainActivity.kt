package gordeev.it_dictionary.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import com.google.accompanist.insets.ProvideWindowInsets
import gordeev.it_dictionary.presentation.screens.MainContainer

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ProvideWindowInsets {
                    MainContainer()
                }
            }
        }
    }
}
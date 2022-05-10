package gordeev.it_dictionary.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import gordeev.it_dictionary.presentation.screens.MainContainer
import gordeev.it_dictionary.presentation.theme.DictionaryTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryTheme {
                ProvideWindowInsets {
                    MainContainer()
                }
            }
        }
    }
}
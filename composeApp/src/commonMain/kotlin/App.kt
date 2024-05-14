import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import common.features.signin.presentation.SignInScreen
import common.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        Navigator(SignInScreen()) { navigator: Navigator ->
            SlideTransition(navigator)
        }
    }
}
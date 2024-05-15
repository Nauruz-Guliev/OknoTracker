import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import features.signin.SignInScreen
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import ru.kpfu.itis.utils.Strings
import theme.AppTheme

@Composable
fun App(
    strings: Strings
) {
    AppTheme {
        Navigator(SignInScreen(strings)) { navigator: Navigator ->
            SlideTransition(navigator)
        }
    }
}

fun initKoin(
    vararg modules: Module = arrayOf(module {}),
    appDeclaration: KoinAppDeclaration = {}
) {
    startKoin {
        appDeclaration()
        modules.forEach {
            modules(it)
        }
    }
}
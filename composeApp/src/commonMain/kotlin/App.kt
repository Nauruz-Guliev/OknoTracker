import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import common.features.di.signInModule
import common.features.signin.SignInScreen
import common.theme.AppTheme
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

@Composable
fun App() {
    AppTheme {
        Navigator(SignInScreen()) { navigator: Navigator ->
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
        signInModule()
        modules.forEach {
            modules(it)
        }
    }
}
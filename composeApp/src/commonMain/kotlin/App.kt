import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import features.signin.SignInScreen
import features.tasks.main.MainScreen
import kotlinx.coroutines.runBlocking
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import ru.kpfu.itis.features.task.data.store.UserStore
import theme.AppTheme

@Composable
fun App() {
    val store = koinInject<UserStore>()
    var userId by remember { mutableStateOf<Long?>(null) }

    runBlocking {
        userId = store.getUserId()
    }

    AppTheme {
        Navigator(SignInScreen()) {
            if (userId != null) {
                Navigator(MainScreen())
            } else {
                Navigator(SignInScreen())
            }
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
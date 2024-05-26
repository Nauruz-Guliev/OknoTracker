package features.signin

import flow_mvi.ConfigurationFactory
import flow_mvi.DefaultConfigurationFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import ru.kpfu.itis.common.mapper.ErrorMapper
import ru.kpfu.itis.features.user.data.repository.UserRepository
import ru.kpfu.itis.features.user.data.service.UserService

fun signInModule() = module {
    factory {
        ErrorMapper(get())
    }
    factory {
        UserRepository(get(), get())
    }
    single {
        SignInContainer(
            get(),
            get(),
            get(),
            get(),
            emailValidator = Validator("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"),
            passwordValidator = Validator("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        )
    }
    single {
        UserService(get(), get())
    }
    single<CoroutineDispatcher> {
        Dispatchers.IO
    }
    single<ConfigurationFactory> {
        DefaultConfigurationFactory()
    }
}
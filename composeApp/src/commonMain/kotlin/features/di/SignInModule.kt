package features.di

import features.signin.SignInContainer
import flow_mvi.ConfigurationFactory
import flow_mvi.DefaultConfigurationFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import ru.kpfu.itis.common.data.mapper.ErrorMapper
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
        SignInContainer(get(), get(), get())
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
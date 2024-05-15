package common.features.di

import common.features.signin.SignInContainer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import ru.kpfu.itis.common.data.mapper.ErrorMapper
import ru.kpfu.itis.features.user.data.repository.UserRepository
import ru.kpfu.itis.features.user.data.service.UserService

fun signInModule() = module {
    factory {
        ErrorMapper()
    }
    factory {
        UserRepository(get(), get())
    }
    factory {
        SignInContainer(get(), get())
    }
    single {
        UserService(get())
    }
    single<CoroutineDispatcher> {
        Dispatchers.IO
    }
}
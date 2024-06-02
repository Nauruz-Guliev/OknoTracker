package features.signin.di

import features.signin.mvi.SignInContainer
import org.koin.dsl.module
import ru.kpfu.itis.common.mapper.ErrorMapper
import ru.kpfu.itis.features.user.data.repository.UserRepository
import ru.kpfu.itis.features.user.data.service.UserService
import utils.validation.InputField
import utils.validation.Validator

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
            emailValidator = Validator(InputField.EMAIL.regex),
            passwordValidator = Validator(InputField.PASSWORD.regex)
        )
    }
    single {
        UserService(get(), get())
    }
}
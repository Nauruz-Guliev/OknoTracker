package features.signup.di

import features.fileds.InputField
import features.fileds.Validator
import features.signup.mvi.SignUpContainer
import org.koin.dsl.module

fun signUpModule() = module {
    single<SignUpContainer> {
        SignUpContainer(
            errorMapper = get(),
            userRepository = get(),
            configurationFactory = get(),
            userStore = get(),
            emailValidator = Validator(InputField.EMAIL.regex),
            passwordValidator = Validator(InputField.PASSWORD.regex)
        )
    }
}
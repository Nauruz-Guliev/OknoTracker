package features.signup.di

import features.signup.mvi.SignUpContainer
import org.koin.dsl.module
import utils.validation.InputField
import utils.validation.Validator

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
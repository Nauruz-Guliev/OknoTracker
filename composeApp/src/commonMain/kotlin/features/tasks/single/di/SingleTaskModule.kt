package features.tasks.single.di

import features.tasks.single.mvi.SingleTaskContainer
import org.koin.dsl.module
import utils.validation.InputField
import utils.validation.Validator

fun singleTaskModule() = module {
    factory {
        SingleTaskContainer(
            errorMapper = get(),
            repository = get(),
            configurationFactory = get(),
            userStore = get(),
            attachmentRepository = get(),
            nameValidator = Validator(InputField.NAME.regex),
            descriptionValidator = Validator(InputField.DESCRIPTION.regex)
        )
    }
}
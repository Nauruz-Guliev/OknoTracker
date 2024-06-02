package features.tasks.single.di

import features.tasks.single.SingleTaskContainer
import org.koin.dsl.module
import utils.validation.InputField
import utils.validation.Validator

fun singleTaskModule() = module {
    single {
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
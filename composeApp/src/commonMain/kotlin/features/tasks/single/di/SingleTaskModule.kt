package features.tasks.single.di

import features.fileds.InputField
import features.fileds.Validator
import features.tasks.single.SingleTaskContainer
import org.koin.dsl.module

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
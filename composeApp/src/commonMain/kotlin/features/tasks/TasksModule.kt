package features.tasks

import features.fileds.InputField
import features.fileds.Validator
import features.tasks.completed.CompletedTasksContainer
import features.tasks.home.HomeTasksContainer
import features.tasks.main.MainContainer
import features.tasks.single.SingleTaskContainer
import flow_mvi.DefaultConfigurationFactory
import org.koin.dsl.module
import ru.kpfu.itis.features.attachments.mapper.AttachmentMapper
import ru.kpfu.itis.features.task.data.mapper.TaskMapper

fun tasksModule() = module {
    single {
        CompletedTasksContainer(get(), get(), get(), get())
    }

    single {
        HomeTasksContainer(get(), get(), get(), get())
    }

    single {
        MainContainer(get(), get(), get(), get())
    }

    factory {
        SingleTaskContainer(
            errorMapper = get(),
            repository = get(),
            configurationFactory = get(),
            userStore = get(),
            attachmentRepository = get(),
            descriptionValidator = Validator(InputField.DESCRIPTION.regex),
            nameValidator = Validator(InputField.NAME.regex)
        )
    }

    factory {
        AttachmentMapper()
    }

    factory {
        TaskMapper()
    }

    single {
        DefaultConfigurationFactory()
    }
}
package features.tasks

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
        SingleTaskContainer(get(), get(), get(), get(), get())
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
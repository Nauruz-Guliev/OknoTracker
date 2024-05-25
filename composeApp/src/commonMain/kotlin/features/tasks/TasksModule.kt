package features.tasks

import features.tasks.completed.CompletedTasksContainer
import features.tasks.home.HomeTasksContainer
import features.tasks.main.MainContainer
import flow_mvi.DefaultConfigurationFactory
import org.koin.dsl.module
import ru.kpfu.itis.features.task.data.db.TaskDatabaseImpl
import ru.kpfu.itis.features.task.data.mapper.TaskMapper
import ru.kpfu.itis.features.task.data.repository.TaskRepository
import ru.kpfu.itis.features.task.data.service.TaskService

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

    single {
        TaskRepository(get(), get(), get(), get())
    }

    factory {
        TaskService(get(), get())
    }

    single {
        TaskDatabaseImpl(get(), get())
    }

    factory {
        TaskMapper()
    }

    single {
        DefaultConfigurationFactory()
    }
}
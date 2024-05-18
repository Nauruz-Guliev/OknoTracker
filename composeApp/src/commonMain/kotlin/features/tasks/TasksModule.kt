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
import kotlin.math.sin

fun tasksModule() = module {
    single {
        CompletedTasksContainer(get(), get(), get(), get())
    }

    single {
        HomeTasksContainer(get(), get(), get(), get())
    }

    single {
        MainContainer(get(), get())
    }

    factory {
        TaskRepository(get(), get(), get())
    }

    factory {
        TaskService(get(), get())
    }

    factory {
        TaskDatabaseImpl(get())
    }

    factory {
        TaskMapper()
    }

    single {
        DefaultConfigurationFactory()
    }
}
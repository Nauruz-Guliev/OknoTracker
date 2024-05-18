package features.tasks.main

import features.OTrackerState
import features.tasks.home.HomeTasksAction
import features.tasks.home.HomeTasksIntent
import flow_mvi.DefaultConfigurationFactory
import flow_mvi.configure
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import ru.kpfu.itis.common.mapper.ErrorMapper
import ru.kpfu.itis.features.task.data.repository.TaskRepository
import ru.kpfu.itis.features.task.data.store.UserStore
import ru.kpfu.itis.features.task.domain.model.TaskModel

class MainContainer(
    private val errorMapper: ErrorMapper,
    private val configurationFactory: DefaultConfigurationFactory,
) : Container<OTrackerState<List<TaskModel>>, MainIntent, MainAction> {

    override val store: Store<OTrackerState<List<TaskModel>>, MainIntent, MainAction> =
        store(OTrackerState.Initial) {

            configure(configurationFactory, "Tasks")

            recover { exception ->
                updateState { OTrackerState.Error(errorMapper.map(exception = exception)) }
                null
            }

            reduce { intent ->
                when (intent) {
                    MainIntent.FloatingButtonClicked -> {
                        action(MainAction.OpenTaskBottomSheet())
                    }
                    is MainIntent.ErrorOccurred -> {
                       action(MainAction.ShowSnackbar(intent.errorModel.title))
                    }
                }
            }
        }
}
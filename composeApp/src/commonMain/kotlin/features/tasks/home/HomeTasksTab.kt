package features.tasks.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import design_system.card.OTaskCard
import design_system.screens.EmptyTasksState
import design_system.screens.OErrorScreen
import extensions.startFlowMvi
import features.OTrackerState
import features.signin.SignInScreen
import features.tasks.create.TaskBottomSheet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.features.task.domain.model.TaskModel

object HomeTasksTab : Tab {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() = with(koinInject<HomeTasksContainer>().store) {

        startFlowMvi()

        val navigator = LocalNavigator.current
        var showBottomSheet by rememberSaveable { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()
        var selectedTaskId by rememberSaveable { mutableStateOf<Long?>(null) }
        val snackbarHostState = remember { SnackbarHostState() }
        var taskModel by rememberSaveable { mutableStateOf<TaskModel?>(null) }
        val itemList = remember { mutableStateListOf<TaskModel>() }

        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(it)
            ) {

                val state by subscribe { action ->

                    when (action) {
                        is HomeTasksAction.OpenTaskBottomSheet -> {
                            showBottomSheet = true
                        }

                        is HomeTasksAction.ShowSnackbar -> {
                            action.message?.let { it1 -> snackbarHostState.showSnackbar(it1) }
                        }

                        HomeTasksAction.SignOut -> {
                            navigator?.replace(SignInScreen())
                        }
                    }
                }

                if (itemList.isEmpty()) {
                    EmptyTasksState()
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(itemList) { task ->
                            OTaskCard(
                                onCheckedAction = { isChecked, taskId ->
                                    intent(HomeTasksIntent.TaskChecked(isChecked, taskId))
                                },
                                task
                            )

                    }
                }

                when (state) {
                    is OTrackerState.Initial -> {
                        intent(HomeTasksIntent.LoadActiveTasks)
                    }
                    is OTrackerState.Success -> {
                        LaunchedEffect(Unit) {
                            (state as OTrackerState.Success<Flow<List<TaskModel>>>).data.collectLatest {
                                itemList.clear()
                                itemList.addAll(it)
                            }
                        }
                    }

                    is OTrackerState.Loading -> {}
                    is OTrackerState.Error -> {
                        itemList.clear()
                        OErrorScreen(
                            errorModel = (state as OTrackerState.Error).error,
                            onClickAction = {
                                intent(HomeTasksIntent.LoadAllTasks)
                            }
                        )
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                TaskBottomSheet(
                    taskId = selectedTaskId,
                    taskDataAction = { model ->
                        taskModel = model
                        showBottomSheet = false
                    }
                )
            }
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = "Home"
            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}
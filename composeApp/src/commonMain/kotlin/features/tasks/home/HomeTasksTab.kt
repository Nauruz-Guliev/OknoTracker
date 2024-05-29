package features.tasks.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import design_system.card.OTaskCard
import design_system.screens.OErrorScreen
import design_system.screens.OLoadingScreen
import extensions.convertToString
import extensions.startFlowMvi
import features.OTrackerState
import features.TaskPriority
import features.mapToColor
import features.signin.SignInScreen
import features.tasks.single.TaskBottomSheet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.LocalDateTime
import org.koin.compose.koinInject
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.features.task.domain.model.TaskModel

object HomeTasksTab : Tab {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() = with(koinInject<HomeTasksContainer>().store) {
        startFlowMvi()
        val navigator = LocalNavigator.current
        var showBottomSheet by rememberSaveable { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()
        var selectedTaskId by rememberSaveable { mutableStateOf<Long?>(null) }
        val snackbarHostState = remember { SnackbarHostState() }
        val itemList = remember { mutableStateListOf<TaskModel>() }
        var isAllTasksEnabled by rememberSaveable { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {

            val state by subscribe { action ->
                when (action) {
                    is HomeTasksAction.OpenTaskBottomSheet -> {
                        selectedTaskId = action.taskId
                        showBottomSheet = true
                    }

                    is HomeTasksAction.ShowSnackbar -> {
                        action.message?.let { it1 -> snackbarHostState.showSnackbar(it1) }
                        intent(HomeTasksIntent.LoadTasks)
                    }

                    HomeTasksAction.SignOut -> {
                        navigator?.replace(SignInScreen())
                    }
                }
            }

            when (state) {
                is OTrackerState.Success -> {
                    LaunchedEffect(Unit) {
                        (state as OTrackerState.Success<Flow<List<TaskModel>>>).data.collectLatest { tasks ->
                            val list = when (!isAllTasksEnabled) {
                                true -> tasks.filter { !it.isCompleted }
                                false -> tasks
                            }
                            itemList.clear()
                            itemList.addAll(list)
                        }
                    }
                    SuccessState(
                        itemList = itemList,
                        isAllTasksEnabled = isAllTasksEnabled,
                        allTaskEnabledAction = {
                            isAllTasksEnabled = !isAllTasksEnabled
                            intent(HomeTasksIntent.LoadTasks)
                        },
                        store = this@with,
                    )
                }

                is OTrackerState.Initial -> {
                    intent(HomeTasksIntent.LoadTasks)
                }

                is OTrackerState.Loading -> {
                    OLoadingScreen(true)
                }

                is OTrackerState.Error -> {
                    itemList.clear()
                    OErrorScreen(
                        errorModel = (state as OTrackerState.Error).error,
                        onClickAction = {
                            intent(HomeTasksIntent.LoadTasks)
                        }
                    )
                }
            }

            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState
            )
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
                    closeAction = {
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
            val icon = rememberVectorPainter(Icons.Filled.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}

@Composable
fun SuccessState(
    itemList: SnapshotStateList<TaskModel>,
    allTaskEnabledAction: () -> Unit,
    isAllTasksEnabled: Boolean,
    store: Store<OTrackerState<Flow<List<TaskModel>>>, HomeTasksIntent, HomeTasksAction>,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            FilterChip(
                selected = isAllTasksEnabled,
                label = {
                    Text("All tasks")
                },
                onClick = allTaskEnabledAction
            )
        }
        TasksList(itemList, store)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TasksList(
    itemList: SnapshotStateList<TaskModel>,
    store: Store<OTrackerState<Flow<List<TaskModel>>>, HomeTasksIntent, HomeTasksAction>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            items = itemList,
            key = {
                it.id
            }
        ) { task ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToStart) {
                        itemList.remove(task)
                        store.intent(HomeTasksIntent.DeleteTask(task.id))
                    }
                    true
                }
            )

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    val color = when (dismissState.dismissDirection) {
                        DismissDirection.EndToStart -> MaterialTheme.colorScheme.errorContainer
                        else -> Color.Transparent
                    }
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(color)
                            .padding(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                },
                dismissContent = {
                    OTaskCard(
                        onCheckedAction = { isChecked, taskId ->
                            store.intent(HomeTasksIntent.TaskChecked(isChecked, taskId))
                        },
                        task = task,
                        labels = buildList {
                            add(task.priority to TaskPriority[task.priority].mapToColor())
                            task.deadlineTime?.let {
                                add(
                                    LocalDateTime.parse(it)
                                        .convertToString() to MaterialTheme.colorScheme.surfaceContainerLow
                                )
                            }
                        },
                        onItemClicked = {
                            store.intent(HomeTasksIntent.EditTask(it))
                        }
                    )
                }
            )
        }
    }
}
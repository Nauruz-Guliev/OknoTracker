package features.tasks.completed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import design_system.card.OTaskCard
import design_system.screens.EmptyTasksState
import design_system.screens.OErrorScreen
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
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.features.task.domain.model.TaskModel
import theme.secondaryContainerLight

object CompletedTasksTab : Tab {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() = with(koinInject<CompletedTasksContainer>().store) {
        startFlowMvi()
        val navigator = LocalNavigator.current
        var showBottomSheet by rememberSaveable { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()
        var selectedTaskId by rememberSaveable { mutableStateOf<Long?>(null) }
        val snackbarHostState = remember { SnackbarHostState() }
        var itemList by rememberSaveable { mutableStateOf(listOf<TaskModel>()) }
        var showEmptyState by rememberSaveable { mutableStateOf(true) }

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
                        is CompletedTasksAction.OpenTaskBottomSheet -> {
                            selectedTaskId = action.taskId
                            showBottomSheet = true
                        }

                        CompletedTasksAction.SignOut -> {
                            navigator?.replace(SignInScreen())
                        }

                        is CompletedTasksAction.ShowSnackbar -> {
                            action.message?.let { it1 -> snackbarHostState.showSnackbar(it1) }
                        }
                    }
                }

                if (itemList.isEmpty() && showEmptyState) {
                    EmptyTasksState()
                }

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
                                    intent(CompletedTasksIntent.DeleteTask(task.id))
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
                                        intent(CompletedTasksIntent.TaskChecked(isChecked, taskId))
                                    },
                                    task = task,
                                    labels = buildList {
                                        add(
                                            task.priority to TaskPriority.get(task.priority)
                                                .mapToColor()
                                        )
                                        task.deadlineTime?.let {
                                            add(
                                                LocalDateTime.parse(it)
                                                    .convertToString() to secondaryContainerLight
                                            )
                                        }
                                    }
                                ) {
                                    intent(CompletedTasksIntent.EditTask(it))
                                }
                            }
                        )
                    }
                }

                when (state) {
                    is OTrackerState.Initial -> {
                        intent(CompletedTasksIntent.LoadTasks)
                    }

                    is OTrackerState.Success -> {
                        showEmptyState = false
                        LaunchedEffect(Unit) {
                            (state as OTrackerState.Success<Flow<List<TaskModel>>>).data.collectLatest {
                                itemList = it
                            }
                        }
                    }

                    is OTrackerState.Loading -> {}
                    is OTrackerState.Error -> {
                        showEmptyState = false
                        OErrorScreen(
                            errorModel = (state as OTrackerState.Error).error,
                            onClickAction = {
                                intent(CompletedTasksIntent.LoadTasks)
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
            val title = "Closed Tasks"
            val icon = rememberVectorPainter(Icons.Default.Check)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }
}
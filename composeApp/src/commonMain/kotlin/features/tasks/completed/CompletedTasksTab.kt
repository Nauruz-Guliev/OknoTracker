package features.tasks.completed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import design_system.card.OTaskCard
import design_system.screens.EmptyTasksState
import design_system.screens.OErrorScreen
import extensions.startFlowMvi
import features.OTrackerState
import features.signin.SignInScreen
import features.tasks.single.TaskBottomSheet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.features.task.domain.model.TaskModel

object CompletedTasksTab : Tab {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() = with(koinInject<CompletedTasksContainer>().store) {

        startFlowMvi()

        val navigator = LocalNavigator.current
        var showBottomSheet by rememberSaveable { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()
        var selectedTaskId by rememberSaveable { mutableStateOf<Long?>(null) }
        val snackbarHostState = remember { SnackbarHostState() }
        val refreshState = rememberPullToRefreshState()
        var itemList by rememberSaveable { mutableStateOf(listOf<TaskModel>()) }
        var showEmptyState by rememberSaveable { mutableStateOf(true) }

        if (refreshState.isRefreshing) {
            intent(CompletedTasksIntent.LoadTasks)
        }

        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            modifier = Modifier.nestedScroll(refreshState.nestedScrollConnection)
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(it)
            ) {

                val state by subscribe { action ->
                    when (action) {
                        is CompletedTasksAction.OpenTaskBottomSheet -> {
                            showBottomSheet = true
                        }

                        CompletedTasksAction.SignOut -> {
                            navigator?.replace(SignInScreen())
                        }

                        is CompletedTasksAction.ShowSnackbar -> {
                            action.message.let { it1 -> snackbarHostState.showSnackbar(it1) }
                        }
                    }
                }

                if (itemList.isEmpty() && showEmptyState) {
                    EmptyTasksState()
                }



                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (!refreshState.isRefreshing) {
                        items(itemList) { task ->
                            OTaskCard(
                                onCheckedAction = { isChecked, taskId ->
                                    intent(CompletedTasksIntent.TaskChecked(isChecked, taskId))
                                },
                                task
                            ) {
                                intent(CompletedTasksIntent.EditTask(it))
                            }
                        }
                    }
                }

                PullToRefreshContainer(
                    modifier = Modifier.align(Alignment.TopCenter),
                    state = refreshState,
                )

                when (state) {
                    is OTrackerState.Initial -> {
                        refreshState.startRefresh()
                        intent(CompletedTasksIntent.LoadTasks)
                    }

                    is OTrackerState.Success -> {
                        showEmptyState = false
                        LaunchedEffect(Unit) {
                            (state as OTrackerState.Success<Flow<List<TaskModel>>>).data.collectLatest {
                                itemList = it
                            }
                        }
                        refreshState.endRefresh()
                    }

                    is OTrackerState.Loading -> {
                        showEmptyState = true
                    }

                    is OTrackerState.Error -> {
                        showEmptyState = false
                        refreshState.endRefresh()
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
package features.tasks.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import design_system.screens.OErrorScreen
import design_system.screens.OInitialScreen
import features.OTrackerState
import features.signin.SignInScreen
import features.tasks.create.TaskBottomSheet
import org.koin.compose.koinInject
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.features.task.domain.model.TaskModel

object MainTasksTab : Tab {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() = with(koinInject<MainTasksContainer>().store) {
        val navigator = LocalNavigator.current
        var showBottomSheet by rememberSaveable { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()
        var selectedTaskId by rememberSaveable { mutableStateOf<Long?>(null) }
        val snackbarHostState = remember { SnackbarHostState() }
        var isRefreshing by rememberSaveable { mutableStateOf(true) }

        val refreshState = rememberPullRefreshState(
            onRefresh = {
                isRefreshing = true
            },
            refreshing = isRefreshing
        )

        Scaffold(
            modifier = Modifier.pullRefresh(refreshState),
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                PullRefreshIndicator(
                    refreshing = true,
                    backgroundColor = Color.Blue,
                    contentColor = Color.Red,
                    state = refreshState,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center)
                        .padding(it)
                )

                val state by subscribe { action ->
                    when (action) {
                        is MainTasksAction.OpenTaskBottomSheet -> {
                            showBottomSheet = true
                        }

                        is MainTasksAction.OpenErrorScreen -> {
                            navigator?.push(
                                OErrorScreen(
                                    errorModel = action.errorModel,
                                    onClickAction = {
                                        navigator.pop()
                                    }
                                )
                            )
                        }

                        is MainTasksAction.ShowSnackbar -> {
                            snackbarHostState.showSnackbar(action.message)
                        }

                        MainTasksAction.SignOut -> {
                            navigator?.replace(SignInScreen())
                        }
                    }
                }

                when (state) {
                    is OTrackerState.Initial -> {
                        isRefreshing = false
                        OInitialScreen()
                    }

                    is OTrackerState.Success -> {
                        isRefreshing = false
                        SuccessScreen(state as OTrackerState.Success<List<TaskModel>>)
                    }

                    is OTrackerState.Loading -> {
                        isRefreshing = true
                    }

                    is OTrackerState.Error -> {
                        isRefreshing = false
                        navigator?.push(
                            OErrorScreen(
                                errorModel = (state as OTrackerState.Error).error,
                                onClickAction = {
                                    TODO()
                                }
                            )
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
                    taskId = selectedTaskId
                )
            }
        }
    }

    @Composable
    private fun SuccessScreen(state: OTrackerState.Success<List<TaskModel>>) {
        LazyColumn {
            items(state.data) { item ->
                OTaskCard(
                    onCheckedAction = { isChecked ->
                        TODO()
                    },
                    title = item.name
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
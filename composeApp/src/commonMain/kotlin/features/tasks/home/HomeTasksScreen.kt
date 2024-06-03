package features.tasks.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import base.BaseScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import design_system.card.OTaskCard
import design_system.screens.OErrorScreen
import design_system.screens.OLoadingScreen
import design_system.swipedismiss.OSwipeToDismiss
import extensions.createLabels
import features.OTrackerState
import features.signin.SignInScreen
import features.tasks.DialogsOpener
import features.tasks.home.mvi.HomeTasksAction
import features.tasks.home.mvi.HomeTasksContainer
import features.tasks.home.mvi.HomeTasksIntent
import features.tasks.single.SingleTaskScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import pro.respawn.flowmvi.api.Store
import ru.kpfu.itis.features.task.domain.model.TaskModel
import theme.LocalPaddingValues

object HomeTasksScreen :
    BaseScreen<OTrackerState<Flow<ImmutableList<TaskModel>>>, HomeTasksIntent, HomeTasksAction>(),
    Tab {
    @Composable
    override fun Content() = withStore<HomeTasksContainer> {
        val navigator = LocalNavigator.current
        val dialogsOpener = remember { mutableStateOf<DialogsOpener>(DialogsOpener.None) }
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        Box(modifier = Modifier.fillMaxSize()) {
            action = { action ->
                handleAction(
                    action = action,
                    dialogsOpenerChangeAction = {
                        dialogsOpener.value = it
                    },
                    navigator = navigator,
                    bottomSheetNavigator = bottomSheetNavigator
                )
            }
            currentState = { state ->
                HandleState(state)
            }
            HandleDialogRequest(
                dialog = dialogsOpener.value,
                onDismissAction = {
                    dialogsOpener.value = DialogsOpener.None
                }
            )
        }
    }

    private fun Store<*, HomeTasksIntent, *>.handleAction(
        action: HomeTasksAction,
        dialogsOpenerChangeAction: (DialogsOpener) -> Unit,
        navigator: Navigator?,
        bottomSheetNavigator: BottomSheetNavigator,
    ) {
        when (action) {
            is HomeTasksAction.OpenTaskBottomSheet -> {
                bottomSheetNavigator.show(
                    SingleTaskScreen(
                        taskId = action.taskId,
                        closeAction = {
                            intent(HomeTasksIntent.LoadTasks)
                            bottomSheetNavigator.hide()
                        }
                    )
                )
            }

            is HomeTasksAction.ShowSnackbar -> {
                action.message?.let { dialogsOpenerChangeAction(DialogsOpener.SnackBar(action.message)) }
                intent(HomeTasksIntent.LoadTasks)
            }

            HomeTasksAction.SignOut -> {
                navigator?.replace(SignInScreen())
            }
        }
    }
    @Composable
    private fun Store<*, HomeTasksIntent, *>.HandleState(
        state: OTrackerState<Flow<List<TaskModel>>>,
    ) {
        val itemList = mutableStateListOf<TaskModel>()
        val isAllTasksEnabled = rememberSaveable { mutableStateOf(false) }

        when (state) {
            is OTrackerState.Success -> {
                val data = state.data.collectAsState(emptyList())
                val list = when {
                    isAllTasksEnabled.value -> {
                        data.value
                    }

                    else -> data.value.filter { !it.isCompleted }
                }
                itemList.clear()
                itemList.addAll(list)
                SuccessScreen(
                    itemList = itemList.toPersistentList(),
                    isAllTasksEnabled = isAllTasksEnabled.value,
                    allTaskEnabledAction = {
                        isAllTasksEnabled.value = !isAllTasksEnabled.value
                    },
                )
            }

            is OTrackerState.Initial -> {
                intent(HomeTasksIntent.LoadTasks)
            }

            is OTrackerState.Loading -> OLoadingScreen()
            is OTrackerState.Error -> OErrorScreen(
                errorModel = state.error,
                onClickAction = {
                    intent(HomeTasksIntent.LoadTasks)
                }
            )
        }
    }
    @Composable
    private fun Store<*, HomeTasksIntent, *>.SuccessScreen(
        itemList: ImmutableList<TaskModel>,
        allTaskEnabledAction: () -> Unit,
        isAllTasksEnabled: Boolean,
        taskDeleteAction: (taskId: Long) -> Unit = { },
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = LocalPaddingValues.current.medium)
            ) {
                FilterChip(
                    selected = isAllTasksEnabled,
                    label = {
                        Text("All tasks")
                    },
                    onClick = allTaskEnabledAction
                )
            }
            TasksList(itemList, taskDeleteAction)
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun Store<*, HomeTasksIntent, *>.TasksList(
        itemList: ImmutableList<TaskModel>,
        taskDeleteAction: (taskId: Long) -> Unit,
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                items = itemList,
                key = { it.id }
            ) { task ->
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            taskDeleteAction(task.id)
                        }
                        true
                    }
                )
                OSwipeToDismiss(
                    dismissState = dismissState,
                    content = {
                        OTaskCard(
                            onCheckedAction = { isChecked, taskId ->
                                intent(HomeTasksIntent.TaskChecked(isChecked, taskId))
                            },
                            task = task,
                            labels = task.createLabels(),
                            onItemClicked = {
                                intent(HomeTasksIntent.EditTask(it))
                            }
                        )
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
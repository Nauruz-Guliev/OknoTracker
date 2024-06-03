package features.tasks.completed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import features.tasks.completed.mvi.CompletedTasksAction
import features.tasks.completed.mvi.CompletedTasksContainer
import features.tasks.completed.mvi.CompletedTasksIntent
import features.tasks.single.SingleTaskScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import pro.respawn.flowmvi.api.Store
import ru.kpfu.itis.features.task.domain.model.TaskModel

object CompletedTasksScreen :
    BaseScreen<OTrackerState<Flow<List<TaskModel>>>, CompletedTasksIntent, CompletedTasksAction>(),
    Tab {

    @Composable
    override fun Content() = withStore<CompletedTasksContainer> {
        val navigator = LocalNavigator.current
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val dialogsOpener = remember { mutableStateOf<DialogsOpener>(DialogsOpener.None) }

        Box(modifier = Modifier.fillMaxSize()) {
            currentState = { state ->
                HandleState(state)
            }
            action = { action ->
                handleAction(
                    action = action,
                    dialogsOpenerChangeAction = { dialog ->
                        dialogsOpener.value = dialog
                    },
                    navigator = navigator,
                    bottomSheetNavigator = bottomSheetNavigator,
                )
            }
            HandleDialogRequest(
                dialog = dialogsOpener.value,
                onDismissAction = {
                    dialogsOpener.value = DialogsOpener.None
                }
            )
        }
    }

    private fun Store<*, CompletedTasksIntent, *>.handleAction(
        action: CompletedTasksAction,
        dialogsOpenerChangeAction: (DialogsOpener) -> Unit,
        navigator: Navigator?,
        bottomSheetNavigator: BottomSheetNavigator,
    ) {
        when (action) {
            is CompletedTasksAction.OpenTaskBottomSheet -> {
                bottomSheetNavigator.show(
                    SingleTaskScreen(
                        taskId = action.taskId,
                        closeAction = {
                            bottomSheetNavigator.hide()
                        }
                    )
                )
            }

            is CompletedTasksAction.ShowSnackbar -> {
                intent(CompletedTasksIntent.LoadTasks)
                action.message?.let {
                    dialogsOpenerChangeAction(DialogsOpener.SnackBar(it))
                }
            }

            CompletedTasksAction.SignOut -> navigator?.parent?.replace(SignInScreen())
        }
    }

    @Composable
    private fun Store<*, CompletedTasksIntent, *>.HandleState(state: OTrackerState<Flow<List<TaskModel>>>) {
        when (state) {
            is OTrackerState.Initial -> intent(CompletedTasksIntent.LoadTasks)
            is OTrackerState.Success<*> -> {
                val successState =
                    (state as OTrackerState.Success<Flow<List<TaskModel>>>).data
                        .distinctUntilChanged()
                        .collectAsState(
                            emptyList()
                        )
                SuccessScreen(
                    itemList = successState.value.toPersistentList(),
                    onTaskCheckedAction = { isChecked, taskId ->
                        intent(CompletedTasksIntent.TaskChecked(isChecked, taskId))
                    },
                    onTaskDeletedAction = { taskId ->
                        intent(CompletedTasksIntent.DeleteTask(taskId))
                    },
                    onItemClickedAction = { taskId ->
                        intent(CompletedTasksIntent.EditTask(taskId))
                    }
                )
            }
            is OTrackerState.Loading -> OLoadingScreen()
            is OTrackerState.Error ->
                OErrorScreen(
                    errorModel = state.error,
                    onClickAction = {
                        intent(CompletedTasksIntent.LoadTasks)
                    }
                )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun SuccessScreen(
        itemList: ImmutableList<TaskModel>,
        onTaskDeletedAction: (taskId: Long) -> Unit = { },
        onTaskCheckedAction: (isChecked: Boolean, taskId: Long) -> Unit = { _, _ -> },
        onItemClickedAction: (taskId: Long) -> Unit = {},
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                items = itemList,
                key = { it.id }
            ) { task ->
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            onTaskDeletedAction(task.id)
                        }
                        true
                    }
                )
                OSwipeToDismiss(
                    dismissState = dismissState,
                    content = {
                        OTaskCard(
                            onCheckedAction = onTaskCheckedAction,
                            task = task,
                            labels = task.createLabels(),
                            onItemClicked = onItemClickedAction
                        )
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

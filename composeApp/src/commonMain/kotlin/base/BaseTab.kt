package base

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import extensions.startFlowMvi
import features.tasks.DialogsOpener
import features.tasks.single.TaskBottomSheet
import org.koin.compose.koinInject
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.compose.dsl.subscribe

abstract class BaseTab<S : MVIState, I : MVIIntent, A : MVIAction> : Tab {

    lateinit var container: Container<S, I, A>

    @Composable
    inline fun <reified T : Container<S, I, A>> withStore(block: @Composable StoreHolder<S, I, A>.() -> Unit) {
        container = koinInject<T>().apply {
            store.startFlowMvi()
        }
        val storeHolder = StoreHolder(container.store)
        return storeHolder.block().also {
            storeHolder.apply {
                val state by subscribe {
                    action(it)
                }
                currentState(state)
            }
        }
    }

    class StoreHolder<S : MVIState, I : MVIIntent, A : MVIAction>(
        store: Store<S, I, A>
    ) : Store<S, I, A> by store {

        lateinit var action: suspend (A) -> Unit
        lateinit var currentState: @Composable (S) -> Unit

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun BoxScope.HandleDialogRequest(
            dialog: DialogsOpener,
            onDismissAction: () -> Unit,
        ) {
            val sheetState = rememberModalBottomSheetState()
            val snackbarHostState = remember { SnackbarHostState() }

            when (dialog) {
                is DialogsOpener.SnackBar -> {
                    LaunchedEffect(Unit) {
                        snackbarHostState.showSnackbar(dialog.message)
                    }
                }

                is DialogsOpener.DatePicker -> {

                }

                DialogsOpener.None -> {}

                is DialogsOpener.FilePicker -> {

                }

                is DialogsOpener.TaskBottomSheet -> {
                    ModalBottomSheet(
                        onDismissRequest = onDismissAction,
                        sheetState = sheetState
                    ) {
                        TaskBottomSheet(
                            taskId = dialog.taskId,
                            closeAction = onDismissAction
                        )
                    }
                }
            }
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState
            )
        }
    }
}


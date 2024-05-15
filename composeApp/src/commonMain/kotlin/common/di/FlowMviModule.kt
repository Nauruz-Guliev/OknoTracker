package common.di

import androidx.compose.runtime.Composable
import org.koin.compose.koinInject
import org.koin.core.parameter.ParametersDefinition
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.FlowMVIDSL
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState

@FlowMVIDSL
@Composable
inline fun <reified T : Container<S, I, A>, S : MVIState, I : MVIIntent, A : MVIAction> container(
    noinline params: ParametersDefinition? = null,
) = koinInject<T>(parameters = params).store
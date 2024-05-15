package flow_mvi

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.dsl.StoreBuilder
import pro.respawn.flowmvi.savedstate.api.NullRecover
import pro.respawn.flowmvi.savedstate.api.Saver
import pro.respawn.flowmvi.savedstate.dsl.CompressedFileSaver
import pro.respawn.flowmvi.savedstate.dsl.JsonSaver
import pro.respawn.flowmvi.savedstate.plugins.saveStatePlugin

class DefaultConfigurationFactory : ConfigurationFactory {
    override fun <S : MVIState> saver(
        serializer: KSerializer<S>,
        fileName: String,
    ) = CompressedFileSaver(
        dir = "/",
        recover = NullRecover,
        fileName = fileName
    ).let { JsonSaver(Json.Default, serializer, it) }

    override operator fun <S : MVIState, I : MVIIntent, A : MVIAction> StoreBuilder<S, I, A>.invoke(
        name: String,
        saver: Saver<S>?,
    ) {
        if (saver != null) install(
            saveStatePlugin(
                saver = saver,
                name = "${name}SavedStatePlugin",
                context = Dispatchers.Default,
            )
        )
    }
}
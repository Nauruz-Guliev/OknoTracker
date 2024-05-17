package features.signup

import features.OTrackerState
import flow_mvi.ConfigurationFactory
import flow_mvi.configure
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import ru.kpfu.itis.common.mapper.ErrorMapper
import ru.kpfu.itis.features.task.data.store.UserStore
import ru.kpfu.itis.features.user.data.repository.UserRepository

class SignUpContainer(
    private val errorMapper: ErrorMapper,
    private val userRepository: UserRepository,
    private val configurationFactory: ConfigurationFactory,
    private val userStore: UserStore,
) : Container<OTrackerState<Nothing>, SignUpIntent, SignUpAction> {

    override val store: Store<OTrackerState<Nothing>, SignUpIntent, SignUpAction> =
        store(OTrackerState.Initial) {
            configure(configurationFactory, "SignUp")

            recover { exception ->
                updateState {
                    OTrackerState.Error(errorMapper.map(exception = exception))
                }
                null
            }

            reduce { intent ->
                when (intent) {
                    is SignUpIntent.SignInClicked -> {
                        action(SignUpAction.OpenSignInScreen)
                        updateState { OTrackerState.Initial }
                    }

                    is SignUpIntent.SignUpClicked -> {
                        updateState { OTrackerState.Loading }

                    }
                }
            }
        }

}
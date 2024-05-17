package features.signin

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

class SignInContainer(
    private val errorMapper: ErrorMapper,
    private val userRepository: UserRepository,
    private val configurationFactory: ConfigurationFactory,
    private val userStore: UserStore,
) : Container<SignInState, SignInIntent, SignInAction> {

    override val store: Store<SignInState, SignInIntent, SignInAction> =
        store(SignInState.Initial) {
            configure(configurationFactory, "SignIn")

            recover { exception ->
                updateState {
                    SignInState.Error(errorMapper.map(exception = exception))
                }
                null
            }

            reduce { intent ->
                when (intent) {
                    is SignInIntent.SignInClicked -> {
                        updateState { SignInState.Loading }
                        val data = userRepository.signIn(intent.email, intent.password)
                        when {
                            data.data != null -> {
                                userStore.setUserID(data.data!!.id)
                                action(SignInAction.OpenMainScreen(data.data!!.id))
                                updateState { SignInState.Initial }
                            }

                            else -> {
                                updateState { SignInState.Error(errorMapper.map(data.error)) }
                            }
                        }
                    }
                    is SignInIntent.ErrorOccured -> {
                        action(SignInAction.OpenErrorScreen(intent.errorModel))
                        updateState { SignInState.Initial }
                    }

                    SignInIntent.SignUpClicked -> {
                        updateState { SignInState.Loading }
                        action(SignInAction.OpenRegisterScreen)
                        updateState { SignInState.Initial }
                    }
                }
            }
        }
}
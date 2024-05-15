package common.features.signin

import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import ru.kpfu.itis.common.data.mapper.ErrorMapper
import ru.kpfu.itis.features.user.data.repository.UserRepository

class SignInContainer(
    private val errorMapper: ErrorMapper,
    private val userRepository: UserRepository,
) : Container<SignInState, SignInIntent, SignInAction> {

    override val store: Store<SignInState, SignInIntent, SignInAction> =
        store(SignInState.Initial) {

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
                            data.isSuccess && data.data != null -> {
                                action(SignInAction.OpenMainScreen(data.data!!.id))
                                updateState { SignInState.Initial }
                            }

                            else -> {
                                updateState { SignInState.Error(errorMapper.map(data.error)) }
                            }
                        }
                    }

                    SignInIntent.SignUpClicked -> {
                        updateState { SignInState.Loading }
                        action(SignInAction.OpenRegisterScreen)
                    }
                }
            }
        }
}
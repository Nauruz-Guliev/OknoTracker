package features.signin.mvi

import features.fileds.InputField
import features.fileds.Validator
import flow_mvi.ConfigurationFactory
import flow_mvi.configure
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
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
    private val emailValidator: Validator,
    private val passwordValidator: Validator,
) : Container<SignInState, SignInIntent, SignInAction> {

    override val store: Store<SignInState, SignInIntent, SignInAction> =
        store(SignInState.Initial) {
            configure(configurationFactory, "SignIn")

            recover { exception ->
                updateState {
                    SignInState.InternalError(errorMapper.map(exception = exception))
                }
                null
            }

            reduce { intent ->
                when (intent) {
                    is SignInIntent.Outer.Login -> {
                        login(intent.email, intent.password)
                    }

                    SignInIntent.Outer.SignUp -> {
                        action(SignInAction.OpenSignUpScreen)
                        updateState { SignInState.Initial }
                    }

                    SignInIntent.Outer.TryAgain -> {
                        withState {
                            when(this){
                                is SignInState.NetworkError ->{
                                    login(
                                        email = this.email,
                                        password = this.password
                                    )
                                }
                                is SignInState.InternalError ->{
                                    updateState { SignInState.Initial }
                                }
                                else -> Unit
                            }
                        }
                    }
                }
            }
        }

    private suspend fun PipelineContext<SignInState, SignInIntent, SignInAction>.login(
        email: String,
        password: String
    ) {
        val isEmailValid = emailValidator.validate(email)
        val isPasswordValid = passwordValidator.validate(password)
        // TODO() remove validation here for successful login if needed
        if (isPasswordValid && isEmailValid) {
            updateState { SignInState.Loading }
            val data = userRepository.signIn(email, password)
            when {
                data.data != null -> {
                    userStore.setUserID(data.data!!.id)
                    action(SignInAction.OpenMainScreen(data.data!!.id))
                    updateState { SignInState.Initial }
                }

                data.error != null -> {
                    updateState {
                        SignInState.NetworkError(email = email, password = password)
                    }
                    action(
                        SignInAction.ShowSnackbar(
                            text = data.error?.title.orEmpty(),
                            actionLabel = "Try again",
                        )
                    )
                }

                else -> Unit
            }
        } else {
            updateState {
                val fieldErrors = buildList {
                    if (!isEmailValid) {
                        add(InputField.EMAIL)
                    }
                    if (!isPasswordValid) {
                        add(InputField.PASSWORD)
                    }
                }
                SignInState.ValidationError(fieldErrors)
            }
        }
    }
}
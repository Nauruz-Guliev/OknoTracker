package features.signup.mvi

import features.fileds.InputField
import features.fileds.Validator
import features.signin.mvi.SignInAction
import features.signin.mvi.SignInState
import features.signup.SignUpState
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

class SignUpContainer(
    private val errorMapper: ErrorMapper,
    private val userRepository: UserRepository,
    private val configurationFactory: ConfigurationFactory,
    private val userStore: UserStore,
    private val emailValidator: Validator,
    private val passwordValidator: Validator,
) : Container<SignUpState, SignUpIntent, SignUpAction> {

    override val store: Store<SignUpState, SignUpIntent, SignUpAction> =
        store(SignUpState.Initial) {
            configure(configurationFactory, "SignUp")

            recover { exception ->
                updateState {
                    SignUpState.InternalError(errorMapper.map(exception = exception))
                }
                null
            }

            reduce { intent ->
                when (intent) {

                    is SignUpIntent.SignUp -> signup(intent.email, intent.password)

                    SignUpIntent.TryAgain -> withState {
                        when(this){
                            is SignUpState.NetworkError ->{
                                signup(
                                    email = this.email,
                                    password = this.password
                                )
                            }
                            is SignUpState.InternalError ->{
                                updateState { SignUpState.Initial }
                            }
                            else -> Unit
                        }
                    }

                    SignUpIntent.SignIn -> action(SignUpAction.OpenSignInScreen)
                }
            }
        }

    private suspend fun PipelineContext<SignUpState, SignUpIntent, SignUpAction>.signup(
        email: String,
        password: String
    ) {
        val isEmailValid = emailValidator.validate(email)
        val isPasswordValid = passwordValidator.validate(password)

        if (isPasswordValid && isEmailValid) {
            updateState { SignUpState.Loading }
            val data = userRepository.signUp(email, password)
            when {
                data.data != null -> {
                    userStore.setUserID(data.data!!.id)
                    action(SignUpAction.OpenMainScreen(data.data!!.id))
                    updateState { SignUpState.Initial }
                }

                data.error != null -> {
                    updateState {
                        SignUpState.NetworkError(email = email, password = password)
                    }
                    action(
                        SignUpAction.ShowSnackbar(
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
                SignUpState.ValidationError(fieldErrors)
            }
        }
    }

}
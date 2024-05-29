package features.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import design_system.button.OButton
import design_system.screens.OErrorScreen
import design_system.screens.OLoadingScreen
import design_system.textfield.OTextField
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import extensions.startFlowMvi
import features.fileds.InputField
import features.signin.mvi.SignInAction
import features.signin.mvi.SignInContainer
import features.signin.mvi.SignInIntent
import features.signin.mvi.SignInState
import features.signup.SignUpScreen
import features.tasks.main.MainScreen
import org.koin.compose.koinInject
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.OResources

class SignInScreen : Screen {

    @Composable
    override fun Content() = with(koinInject<SignInContainer>().store) {
        startFlowMvi()

        val navigator = LocalNavigator.current

        val scaffoldState = rememberScaffoldState()

        val state by subscribe { action ->
            when (action) {
                is SignInAction.OpenSignUpScreen -> {
                    navigator?.replace(SignUpScreen())
                }

                is SignInAction.OpenMainScreen -> {
                    navigator?.apply {
                        replaceAll(MainScreen())
                    }
                }

                is SignInAction.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    message = action.text,
                    actionLabel = action.actionLabel
                )
            }
        }

        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier.fillMaxSize()
        ) {
            HandleSignInState(state)
        }
    }


    @Composable
    private fun Store<SignInState, SignInIntent, SignInAction>.HandleSignInState(state: SignInState) {
        when (state) {
            SignInState.Loading -> {
                OLoadingScreen()
            }

            is SignInState.InternalError -> {
                OErrorScreen(
                    errorModel = state.error,
                    onClickAction = { intent(SignInIntent.Outer.TryAgain) },
                    isTryAgain = true
                )
            }

            is SignInState.ValidationError, is SignInState.NetworkError, SignInState.Initial -> InitialContent(
                state
            )
        }
    }
}


@Composable
private fun IntentReceiver<SignInIntent>.InitialContent(
    state: SignInState,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            var email by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }

            Spacer(
                modifier = Modifier.height(80.dp)
            )

            Image(
                painter = painterResource(OResources.Login.icon()),
                modifier = Modifier.size(200.dp),
                contentDescription = "Login icon: girl with a laptop"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            OTextField(
                onValueChange = { email = it },
                label = stringResource(InputField.EMAIL.labelText),
                text = email,
                characterMaxCount = InputField.PASSWORD.maxLength,
                errorText = state.findFieldError(InputField.EMAIL)?.let{ stringResource(it) }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            OTextField(
                onValueChange = { password = it },
                label = stringResource(InputField.PASSWORD.labelText),
                text = password,
                characterMaxCount = InputField.PASSWORD.maxLength,
                errorText = state.findFieldError(InputField.PASSWORD)?.let { stringResource(it) },
                isPassword = true
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            OButton(
                text = stringResource(OResources.Login.title()),
                onClickAction = {
                    intent(
                        SignInIntent.Outer.Login(
                            email, password
                        )
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OButton(
                text = "Sign up",
                isMainButton = false,
                onClickAction = {
                    intent(SignInIntent.Outer.SignUp)
                }
            )
        }
    }
}
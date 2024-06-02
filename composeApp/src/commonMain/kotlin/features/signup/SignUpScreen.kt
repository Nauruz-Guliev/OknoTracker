package features.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import features.signin.SignInScreen
import features.signup.mvi.SignUpAction
import features.signup.mvi.SignUpContainer
import features.signup.mvi.SignUpIntent
import features.tasks.main.MainScreen
import org.koin.compose.koinInject
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.OResources
import utils.validation.InputField

class SignUpScreen : Screen {

    @Composable
    override fun Content() = with(koinInject<SignUpContainer>().store) {
        startFlowMvi()

        val navigator = LocalNavigator.current
        val scaffoldState = rememberScaffoldState()

        val state by subscribe { action ->
            when (action) {
                is SignUpAction.OpenMainScreen ->
                    navigator?.replaceAll(MainScreen())

                SignUpAction.OpenSignInScreen -> navigator?.replace(SignInScreen())
                is SignUpAction.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    message = action.text,
                    actionLabel = action.actionLabel
                )
            }
        }

        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier.fillMaxSize()
        ) {
            HandleSignUpState(state)
        }
    }

    @Composable
    private fun Store<SignUpState, SignUpIntent, SignUpAction>.HandleSignUpState(state: SignUpState) {
        when (state) {
            SignUpState.Loading -> {
                OLoadingScreen()
            }

            is SignUpState.InternalError -> {
                OErrorScreen(
                    errorModel = state.error,
                    onClickAction = { intent(SignUpIntent.TryAgain) },
                    isTryAgain = true
                )
            }

            is SignUpState.ValidationError, is SignUpState.NetworkError, SignUpState.Initial -> InitialContent(
                state
            )
        }
    }

    @Composable
    private fun IntentReceiver<SignUpIntent>.InitialContent(state: SignUpState) {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                var email by rememberSaveable { mutableStateOf("") }
                var password by rememberSaveable { mutableStateOf("") }

                Spacer(
                    modifier = Modifier.height(120.dp)
                )

                Image(
                    painter = painterResource(OResources.SignUp.icon()),
                    modifier = Modifier.size(160.dp),
                    contentDescription = "Signup icon: girl with a laptop"
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OTextField(
                    onValueChange = { email = it },
                    label = stringResource(InputField.EMAIL.labelText),
                    text = email,
                    characterMaxCount = InputField.EMAIL.maxLength,
                    errorText = state.findFieldError(InputField.EMAIL)?.let { stringResource(it) },
                    initialIcon = Icons.Default.Email
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OTextField(
                    onValueChange = { password = it },
                    label = stringResource(InputField.PASSWORD.labelText),
                    text = password,
                    characterMaxCount = InputField.PASSWORD.maxLength,
                    isPassword = true,
                    initialIcon = Icons.Default.Lock,
                    errorText = state.findFieldError(InputField.PASSWORD)
                        ?.let { stringResource(it) },
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OButton(
                    text = "Sign up",
                    onClickAction = { intent(SignUpIntent.SignUp(email, password)) }
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                OButton(
                    text = stringResource(OResources.Login.title()),
                    isMainButton = false,
                    onClickAction = {
                        intent(SignUpIntent.SignIn)
                    }
                )
            }
        }
    }
}
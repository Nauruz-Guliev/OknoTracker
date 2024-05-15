package common.features.signin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import common.design_system.button.OButton
import common.design_system.textfield.OTextField
import common.di.container
import common.features.signup.SignUpScreen
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.subscribe

class SignInScreen : Screen {

    @Composable
    override fun Content() =
        with(container<SignInContainer, SignInState, SignInIntent, SignInAction>()) {

            val navigator = LocalNavigator.current

            val state by subscribe { action ->
                when (action) {
                    is SignInAction.OpenRegisterScreen -> {
                        navigator?.apply {
                            replace(SignUpScreen())
                        }
                    }

                    is SignInAction.OpenMainScreen -> {
                        navigator?.apply {
                            //   replace(SignUpScreen())
                        }
                    }
                }
            }

            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    var email by rememberSaveable { mutableStateOf("") }
                    var password by rememberSaveable { mutableStateOf("") }

                    Spacer(
                        modifier = Modifier.height(80.dp)
                    )

                    CoilImage(
                        imageModel = { "https://s3-alpha-sig.figma.com/img/6704/ab02/52886121ea67657e459860dab9ae1ade?Expires=1716768000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=IGPYUslolkAyOTZ0bZK1MY5dURI7Sc4YB1dSQR9cBQx-EemgRVQZSlb-6yg~8BDGAjhe50qq-l4WadOeAZtnPQLoBDkx6ViDdE7rPYlP3PZGcAccL6UFYWskOtv9w7begjko233UwaVKTTszzxQJqJJeW-Nt4jVlrUWjrIEJvMnMlMvbaTjdmyikKjmd3WyDS57Rkw3Ax3ZS4xNgE-1yrcnNC3REd9S3gu2jH740EtTQXSErP~4B3XAeJfcKEDvRvSJmkuVfHpBKXCCTCo29UaEgjrysq-VBDbqm4HHihuaHJ~4Q0SOwwUbrVyBUHOSFhaY5d1dnXwPj07K56EnGcw__" },
                        modifier = Modifier.size(200.dp),
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Fit
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    OTextField(
                        onValueChange = { email = it },
                        label = "Login",
                        text = email,
                        characterMaxCount = 20
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    OTextField(
                        onValueChange = { password = it },
                        label = "Password",
                        text = password,
                        characterMaxCount = 20
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    OButton(
                        text = "Login"
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    OButton(
                        text = "Register",
                        isMainButton = false,
                        onClickAction = {
                            navigator?.apply {
                                replace(SignUpScreen())
                            }
                        }
                    )
                }
            }
        }
}

@Composable
private fun IntentReceiver<SignInIntent>.SignInContent(
    state: SignInState,
) {
    when (state) {
        SignInState.Loading -> {

        }

        is SignInState.Error -> {

        }

        is SignInState.Initial -> {

        }
    }
}
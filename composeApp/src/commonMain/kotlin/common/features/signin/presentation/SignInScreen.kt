package common.features.signin.presentation

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
import common.design_system.button.MainButton
import common.design_system.textfield.MainTextField
import common.features.signup.presentation.SignUpScreen

class SignInScreen : Screen {
    @Composable
    override fun Content() {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                var email by rememberSaveable { mutableStateOf("") }
                var password by rememberSaveable { mutableStateOf("") }
                val navigator = LocalNavigator.current

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

                MainTextField(
                    onValueChange = { email = it },
                    label = "Login",
                    text = email,
                    characterMaxCount = 20
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                MainTextField(
                    onValueChange = { password = it },
                    label = "Password",
                    text = password,
                    characterMaxCount = 20
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                MainButton(
                    text = "Login"
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                MainButton(
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
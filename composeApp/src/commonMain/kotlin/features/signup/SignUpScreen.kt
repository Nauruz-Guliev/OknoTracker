package features.signup

import androidx.compose.foundation.Image
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
import design_system.button.OButton
import design_system.textfield.OTextField
import dev.icerock.moko.resources.compose.painterResource
import features.signin.SignInScreen
import ru.kpfu.itis.OResources

class SignUpScreen : Screen {

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

                Image(
                    painter = painterResource(OResources.SignUp.icon()),
                    modifier = Modifier.size(200.dp),
                    contentDescription = "Signup icon: girl with a laptop"
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
                    text = "Register",
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                OButton(
                    text = "Login",
                    isMainButton = false,
                    onClickAction = {
                        navigator?.apply {
                            replace(SignInScreen())
                        }
                    }
                )
            }
        }
    }
}
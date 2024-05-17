package features.signup

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
import features.signin.SignInScreen

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

                CoilImage(
                    imageModel = { "https://s3-alpha-sig.figma.com/img/bf8e/4923/90c261ed8639b249faddaf86d551f5c6?Expires=1716768000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=MjA~rq5~z5Jnc0LX861Q2Vki2ffdEo1LN~m416MratviEFW4INKo0xfrWFNOkQvqpeFppJ4YoDdEmnuXcTt0OayRRUmqNKOPCpJjefmjTL4mMaFax5jXoG5P645av~GJmo2UrkDP6GpzPA8Ud3xpUJcT61-JI7JilZ-UvJXUPXsrvnmULJpVxRVlwBGRXFdHI7uY~jPOP2I1J65pbpDIjdmP6e62IA6-zJR-u9vBr3vyjNv6fGoXKw97bJrg1GkqWEVa4sMSkk~0kUxGcBIyGRDlZlBpIgRYNTDiIVfQhDxFdVPdHRRm9pJpuQr9t1rntvlROQETSli-aHGkMZxW4g__" },
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
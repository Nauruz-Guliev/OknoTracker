package ru.kpfu.itis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import common.design_system.textfield.MainTextField
import ru.kpfu.itis.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                var text by rememberSaveable { mutableStateOf("") }
                MainTextField(
                    onValueChange = {
                        text = it
                    },
                    text = text,
                    label = "text",
                )
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AppTheme {

    }
}
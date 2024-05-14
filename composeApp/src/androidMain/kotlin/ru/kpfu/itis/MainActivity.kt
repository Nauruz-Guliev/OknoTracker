package ru.kpfu.itis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import common.design_system.button.LargeButton
import ru.kpfu.itis.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Button(onClick = { }) {
                    Text("Privet")
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AppTheme {
        Column {
            LargeButton(text = "Click me")
            LargeButton(text = "Click me", isMainButton = false)
        }
    }
}
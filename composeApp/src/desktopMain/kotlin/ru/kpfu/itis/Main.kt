package ru.kpfu.itis

import App
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ru.kpfu.itis.utils.Strings

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "OknoTracker",
    ) {
        App(Strings())
    }
}
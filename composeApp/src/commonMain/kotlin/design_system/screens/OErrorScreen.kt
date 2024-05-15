package design_system.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import design_system.button.OButton
import ru.kpfu.itis.common.domain.model.ErrorModel

data class OErrorScreen(
    private val errorModel: ErrorModel,
    private val icon: ImageVector = Icons.Rounded.Warning,
    private val iconSize: Int = 120,
    private val isTryAgain: Boolean = true,
    private val buttonPadding: PaddingValues = PaddingValues(horizontal = 40.dp),
    private val onClickAction: () -> Unit,
) : Screen {

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            val navigator = LocalNavigator.current

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(iconSize.dp)
                        .background(MaterialTheme.colorScheme.inverseSurface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Warning",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(iconSize.dp)
                            .padding(16.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = if (errorModel.title.length > 30) {
                        errorModel.title.substringBefore(".")
                    } else {
                        errorModel.title
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = errorModel.details,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OButton(
                    text = if (isTryAgain) {
                        "Try again"
                    } else {
                        "Close"
                    },
                    onClickAction = onClickAction,
                    outerPadding = buttonPadding
                )
            }
        }
    }
}

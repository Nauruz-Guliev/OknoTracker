package design_system.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import design_system.button.OButton
import dev.icerock.moko.resources.compose.painterResource
import ru.kpfu.itis.OResources
import ru.kpfu.itis.common.model.ErrorModel
import theme.LocalPaddingValues

@Composable
fun OErrorScreen(
    errorModel: ErrorModel,
    isTryAgain: Boolean = true,
    buttonPadding: PaddingValues = PaddingValues(horizontal = 40.dp),
    onClickAction: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(LocalPaddingValues.current.extraLarge),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(LocalPaddingValues.current.medium)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(OResources.Common.errorIcon()),
                contentDescription = "Warning",
                modifier = Modifier
                    .size(140.dp)
                    .padding(16.dp)
            )

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
                textAlign = TextAlign.Center,
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = errorModel.details,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (isTryAgain) {
                OButton(
                    text = "Try again",
                    onClickAction = onClickAction,
                    outerPadding = buttonPadding,
                )
            }
        }
    }
}

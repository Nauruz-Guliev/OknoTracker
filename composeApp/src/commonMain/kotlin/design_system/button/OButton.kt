package design_system.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun OButton(
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit = { },
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    innerPadding: PaddingValues = PaddingValues(vertical = 8.dp),
    outerPadding: PaddingValues = PaddingValues(horizontal = 20.dp),
    isMainButton: Boolean = true
) {
    if (isMainButton) {
        Button(
            modifier = modifier.fillMaxWidth()
                .padding(outerPadding),
            onClick = onClickAction
        ) {
            Text(
                text, style = textStyle,
                modifier = modifier.padding(innerPadding)
            )
        }
    } else {
        OutlinedButton(
            modifier = modifier.fillMaxWidth()
                .padding(outerPadding),
            onClick = onClickAction
        ) {
            Text(
                text, style = textStyle,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}
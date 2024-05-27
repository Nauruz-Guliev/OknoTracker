package design_system.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ORadioButton(
    text: String,
    onPrioritySelected: () -> Unit,
    selected: Boolean = false,
    color: Color
) {
    Row(
        modifier = Modifier.clickable {
            onPrioritySelected()
        }.fillMaxWidth()
            .background(color),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = {
                onPrioritySelected()
            }
        )
        Text(
            text,
            modifier = Modifier.padding(4.dp)
        )
    }
}
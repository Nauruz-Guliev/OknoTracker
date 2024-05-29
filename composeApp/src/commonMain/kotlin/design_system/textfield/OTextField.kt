package design_system.textfield

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun OTextField(
    onValueChange: (String) -> Unit,
    label: String,
    text: String,
    modifier: Modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
    isPassword: Boolean = false,
    errorText: String? = null,
    errorIcon: ImageVector = Icons.Default.Warning,
    initialIcon: ImageVector? = null,
    characterMaxCount: Int? = null,
    maxLines: Int = 1,
    isEnabled: Boolean = true,
) {
    TextField(
        modifier = modifier,
        value = text,
        enabled = isEnabled,
        onValueChange = { onValueChange.invoke(it) },
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        label = { Text(label) },
        supportingText = {
            if (errorText != null) {
                Row {
                    Text(
                        text = errorText,
                        Modifier.weight(1f)
                    )
                    characterMaxCount?.let {
                        Text(
                            text = "${text.length} / $characterMaxCount",
                            maxLines = 1
                        )
                    }
                }
            }
        },
        maxLines = maxLines,
        isError = errorText != null,
        trailingIcon = {
            when {
                errorText != null ->{
                    Icon(
                        imageVector = errorIcon,
                        contentDescription = errorText,
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                initialIcon != null -> {
                    Icon(imageVector = initialIcon, contentDescription = initialIcon.toString())
                }
            }
        }
    )
}

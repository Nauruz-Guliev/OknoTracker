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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import javax.swing.Icon

@Composable
fun OTextField(
    onValueChange: (String) -> Unit,
    label: String,
    text: String,
    modifier: Modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
    isPassword: Boolean = false,
    errorText: String? = null,
    icon: Icon? = null,
    characterMaxCount: Int? = null,
    maxLines: Int = 1,
) {
    TextField(
        modifier = modifier,
        value = text,
        onValueChange = {
            if (characterMaxCount != null && it.length > characterMaxCount) {
                onValueChange.invoke(it.take(characterMaxCount))
            } else {
                onValueChange.invoke(it)
            }
        },
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        label = { Text(label) },
        supportingText = {
            Row {
                Text(
                    text = errorText ?: "",
                    Modifier.weight(1f)
                )
                characterMaxCount?.let {
                    Text(
                        text = "${text.length} / $characterMaxCount",
                        maxLines = 1
                    )
                }
            }
        },
        maxLines = maxLines,
        isError = errorText != null,
        trailingIcon = {
            errorText?.let {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = errorText,
                    tint = MaterialTheme.colorScheme.error
                )
            } ?: icon
        }
    )
}

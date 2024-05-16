package design_system.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OLoadingScreen(
    isLoading: Boolean,
    backgroundColor: Color = Color.Gray,
    alpha: Float = 0.25f,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isLoading) alpha else 0f,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isLoading) {
            Surface(
                color = backgroundColor.copy(alpha = animatedAlpha),
                modifier = Modifier.fillMaxSize()
            ) {}

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(108.dp),
                    color = progressColor,
                    strokeWidth = 10.dp,
                    trackColor = trackColor
                )
            }
        }
    }
}
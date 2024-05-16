package features.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object StatisticsTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Home"
            val icon = rememberVectorPainter(Icons.Outlined.Info)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center content vertically
        ) {
            // Example statistics - replace with your actual data
            StatisticItem(label = "Total Users", value = "15,285", color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(16.dp)) // Spacing between items
            StatisticItem(label = "Active Users", value = "8,742", color = Color(0xFF2196F3))
            Spacer(modifier = Modifier.height(16.dp))
            StatisticItem(label = "New Users (Today)", value = "235", color = Color(0xFF9C27B0))
        }
    }

    @Composable
    fun StatisticItem(label: String, value: String, color: Color) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp)) // Small space between value and label
            Text(text = label, color = Color.Gray)
        }
    }
}
package features.tasks

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import design_system.card.OTaskCard

object ClosedTasksTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Closed Tasks"
            val icon = rememberVectorPainter(Icons.Default.Check)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        LazyColumn {
            val list = mutableListOf<String>()
            repeat(20) {
                list.apply {
                    add("Number $it")
                }
            }
            items(list) {
                OTaskCard(title = it)
            }
        }
    }
}
package features.tasks.completed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import design_system.card.OTaskCard
import kotlinx.coroutines.delay

object CompletedTasksTab : Tab {

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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        // startFlowMvi needed

        var itemCount by remember { mutableStateOf(15) }
        val state = rememberPullToRefreshState()
        if (state.isRefreshing) {
            LaunchedEffect(true) {
                // fetch something
                delay(1500)
                itemCount += 5
                state.endRefresh()
            }
        }
        Scaffold(
            modifier = Modifier.nestedScroll(state.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text("TopAppBar") },
                    // Provide an accessible alternative to trigger refresh.
                    actions = {
                        IconButton(onClick = { state.startRefresh() }) {
                            Icon(Icons.Filled.Refresh, "Trigger Refresh")
                        }
                    }
                )
            }
        ) {
            Box(Modifier.padding(it)) {
                LazyColumn(Modifier.fillMaxSize()) {
                    if (!state.isRefreshing) {
                        items(itemCount) {
                            ListItem({ Text(text = "Item ${itemCount - it}") })
                        }
                    }
                }
                PullToRefreshContainer(
                    modifier = Modifier.align(Alignment.TopCenter),
                    state = state,
                )
            }
        }

        /*LazyColumn {
            val list = mutableListOf<String>()
            repeat(20) {
                list.apply {
                    add("Number $it")
                }
            }
            items(list) {
                OTaskCard(
                    title = it,
                    labels = list
                )
            }
        }*/
    }
}